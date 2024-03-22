package com.chat.youknow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.chat.youknow.data.adapter.FriendAdapter
import com.chat.youknow.data.database.RoomDB
import com.chat.youknow.data.model.Friend
import com.chat.youknow.data.model.User
import com.chat.youknow.data.viewmodels.LoginViewModel
import com.chat.youknow.databinding.ActivityMainBinding
import com.chat.youknow.network.Resource
import com.chat.youknow.ui.authenticate.LoginActivity
import com.chat.youknow.ui.chat.ChatActivity
import com.chat.youknow.utils.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var sharedPreference: AppSharedPreference

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var friendList: ArrayList<Friend>
    private lateinit var friendAdapter: FriendAdapter
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadingDialog = LoadingDialog(this)
        sharedPreference = AppSharedPreference(this)

        binding = ActivityMainBinding.inflate(layoutInflater)

        loginViewModel.getUser().observe(this, Observer {
            if (it != null) {
                user = it

                initFriend()
            }
        })



        setContentView(binding.root)
    }

    private fun initFriend() {
        friendList = arrayListOf()
        if (user.userId?.equals(AppConstant.userOne)!!) {
            friendList.add(Friend("User-(2)", AppConstant.userTwo))
        } else {
            friendList.add(Friend("User-(1)", AppConstant.userOne))
        }

        friendAdapter = FriendAdapter(friendList)
        binding.rvChatList.apply {
            adapter = friendAdapter
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        }
//        friendAdapter.notifyDataSetChanged()

        friendAdapter.setOnItemClickListener {
            val intent = Intent(this@MainActivity, ChatActivity::class.java)
            intent.putExtra("friend", it)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            // Logic for logout
            loginViewModel.logout(loginViewModel.user.userId!!).observe(this, Observer {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        loadingDialog.showDialog("Logging out")
                    }
                    Resource.Status.ERROR -> {
                        loadingDialog.hideDialog()
                        binding.root.showSnackBar(it.errorMsg.toString(), false)
                        if(it.errorMsg.toString() == AppConstant.TOKEN_EXPIRE){
                            lifecycleScope.launch(Dispatchers.IO) {
                                loginViewModel.clearTable()
                            }
                            sharedPreference.clearSharedPreference()
                            moveToLogin(this@MainActivity)
                        }
                    }
                    Resource.Status.SUCCESS -> {
                        loadingDialog.hideDialog()

                        sharedPreference.clearSharedPreference()
                        lifecycleScope.launch(Dispatchers.IO) {
                            loginViewModel.clearTable()
                        }
                        moveToLogin(this@MainActivity)

                    }
                }
            })

            return true
        }
        return true
    }
}