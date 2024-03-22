package com.chat.youknow.data.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.chat.youknow.data.database.RoomDB
import com.chat.youknow.data.database.RoomDao
import com.chat.youknow.data.model.ChatMessage
import com.chat.youknow.data.model.ChatMsgData
import com.chat.youknow.data.model.MsgHistoryData
import com.chat.youknow.data.repository.BaseRepository
import com.chat.youknow.data.response.MessageHistoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private var roomDB: RoomDB
    private var roomDao: RoomDao
    lateinit var chatMessageList: LiveData<List<ChatMessage>>

    init {
        roomDB = RoomDB.getInstance(application)
        roomDao = roomDB.getDao()


    }

    private val repository = BaseRepository(application)

    var messageHistoryData = MessageHistoryResponse()
    var messageList = mutableListOf<MessageHistoryResponse.MessageInfo>()

    fun getMessageHistory(data: MsgHistoryData) = repository.messageHistory(data)

    fun sendMessage(data: ChatMsgData) = repository.sendMessage(data)

    fun saveSingleChatMessage(info: MessageHistoryResponse.MessageInfo) = viewModelScope.launch {
        val message = ChatMessage()
        message.time = info.timeMillisecond
        message.mainContentType = info.mainContentType
        message.deleteStatus = info.deleteStatus
        message.roomId = info.roomId
        message.fromUserId = info.mediaData.sender.id
        message.fromUserName = info.mediaData.sender.username
//        message.fromUserProfileUrl = info.mediaData.sender.url
        message.subContentType = info.mediaData.content.subContentType
        message.messageId = info.mediaData.content.messageId
        message.uuid = info.mediaData.content.uuid
        message.content = info.mediaData.content.data.text

//        roomDao.saveSingleChatMessage(message)
        repository.saveSingleChatMessage(message)
    }

     fun getSingleChatMessage(roomId: String) = repository.getSingleChatMessage(roomId)


}