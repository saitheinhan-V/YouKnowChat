package com.chat.youknow.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.chat.youknow.MyApplication
import com.chat.youknow.R
import com.chat.youknow.data.adapter.MessageAdapter
import com.chat.youknow.data.model.*
import com.chat.youknow.data.response.MessageHistoryResponse
import com.chat.youknow.data.viewmodels.ChatViewModel
import com.chat.youknow.data.viewmodels.LoginViewModel
import com.chat.youknow.databinding.ActivityChatBinding
import com.chat.youknow.network.Resource
import com.chat.youknow.utils.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.util.UUID

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var sharedPreference: AppSharedPreference

    private lateinit var friend: Friend
    private val loginViewModel: LoginViewModel by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()
    private lateinit var user: User
    private lateinit var userId: String
    private var chatMessageList: List<ChatMessage> = listOf()

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: MutableList<MessageHistoryResponse.MessageInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)

        loadingDialog = LoadingDialog(this)
        sharedPreference = AppSharedPreference(this)

        binding.ivBack.setOnClickListener {
            hideKeyboard(binding.root)
            finish()
        }

        if (intent != null) {
            friend = intent.getSerializableExtra("friend") as Friend
        }

        binding.tvTitle.text = friend.name

        messageList = ArrayList()
        userId = sharedPreference.getValueString("user_id")!!

        binding.sendButton.setOnClickListener {
            val msg = binding.edtMessage.text.toString().trim()

            if (!isConnected(this)) {
                showToast(resources.getString(R.string.network_error))
                return@setOnClickListener
            }
            if (msg.isNotEmpty()) {
                //call api
                sendMessageAPI(msg)
            }
        }

        loginViewModel.getUser().observe(this, Observer {
            if (it != null) {
                user = it

                loadMsgHistory()
            }
        })

        setContentView(binding.root)
    }

    private fun loadMsgHistory() {


        chatViewModel.getSingleChatMessage(friend.userId)
            .observe(this@ChatActivity, Observer { localMessageList ->
                chatMessageList = localMessageList!!

                if (chatMessageList.isEmpty()) {
                    val data = MsgHistoryData(user.userId!!, friend.userId)
                    chatViewModel.getMessageHistory(data).observe(this, Observer {
                        when (it.status) {
                            Resource.Status.LOADING -> {
                                loadingDialog.showDialog("")
                            }
                            Resource.Status.ERROR -> {
                                loadingDialog.hideDialog()
                                binding.root.showSnackBar(it.errorMsg.toString(),false)
                                if(it.errorMsg.toString() == AppConstant.TOKEN_EXPIRE){
                                    lifecycleScope.launch(Dispatchers.IO) {
                                        loginViewModel.clearTable()
                                    }
                                    sharedPreference.clearSharedPreference()
                                    moveToLogin(this@ChatActivity)
                                }
                            }
                            Resource.Status.SUCCESS -> {
                                loadingDialog.hideDialog()

                                chatViewModel.messageHistoryData = it.data!!
                                val list = it.data.messageInfo
                                list.reverse()
                                chatViewModel.messageList = list

                                for (info in chatViewModel.messageList)
                                    chatViewModel.saveSingleChatMessage(info)

                                setData()

                            }
                        }
                    })
                } else {
                    chatViewModel.messageList = mutableListOf()
                    for (msg in chatMessageList) {
                        val info = MessageHistoryResponse.MessageInfo()
                        info.timeMillisecond = msg.time
                        info.mainContentType = msg.mainContentType
                        info.deleteStatus = msg.deleteStatus
                        info.roomId = msg.roomId

                        val sender = MessageHistoryResponse.Sender()
                        sender.id = msg.fromUserId!!
                        sender.username = msg.fromUserName!!
//                    sender.url = msg.fromUserProfileUrl!!

                        val content = MessageHistoryResponse.Content()
                        content.subContentType = msg.subContentType!!
                        content.messageId = msg.messageId!!
                        content.uuid = msg.uuid!!
                        content.data = MessageHistoryResponse.Data(msg.content!!)

                        info.mediaData = MessageHistoryResponse.MediaData(sender, content)

                        chatViewModel.messageList.add(info)

                        setData()
                    }
                }
            })

    }

    private fun setData() {
        messageAdapter = MessageAdapter(
            this,
            userId,
            chatViewModel.messageHistoryData,
            chatViewModel.messageList
        )
        binding.rvChat.apply {
            adapter = messageAdapter
            layoutManager =
                LinearLayoutManager(this@ChatActivity, LinearLayoutManager.VERTICAL, false)
        }

        if (chatViewModel.messageList.size > 0)
            binding.rvChat.smoothScrollToPosition(chatViewModel.messageList.size - 1)
    }

    private fun sendMessageAPI(msg: String) {
        val data =
            ChatMsgData(msg, userId, friend.userId, UUID.randomUUID().toString().replace("-", ""))
        chatViewModel.sendMessage(data).observe(this@ChatActivity, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {

                }
                Resource.Status.ERROR -> {
                    loadingDialog.hideDialog()
                    binding.root.showSnackBar(it.errorMsg.toString(),false)
                    if(it.errorMsg.toString() == AppConstant.TOKEN_EXPIRE){
                        lifecycleScope.launch(Dispatchers.IO) {
                            loginViewModel.clearTable()
                        }
                        sharedPreference.clearSharedPreference()
                        moveToLogin(this@ChatActivity)
                    }
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.hideDialog()

                    val infoList = it.data!!.messageInfo
                    chatViewModel.messageList.addAll(infoList)

                    for (msg in infoList)
                        chatViewModel.saveSingleChatMessage(msg)

                    messageAdapter.notifyDataSetChanged()

                    binding.edtMessage.setText("")
                    if (chatViewModel.messageList.size > 0)
                        binding.rvChat.smoothScrollToPosition(chatViewModel.messageList.size - 1)
                }
            }
        })
    }

}