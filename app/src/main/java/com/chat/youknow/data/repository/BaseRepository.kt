package com.chat.youknow.data.repository

import android.app.Application
import com.chat.youknow.data.database.RoomDB
import com.chat.youknow.data.database.RoomDao
import com.chat.youknow.data.model.*
import com.chat.youknow.data.response.*
import com.chat.youknow.network.NetAPI
import com.chat.youknow.network.NetworkCall
import com.chat.youknow.network.RequestBuilder
import com.chat.youknow.utils.AppConstant
import com.chat.youknow.utils.AppSharedPreference
import kotlinx.coroutines.runBlocking

class BaseRepository(application: Application) {

    private val sharedPref = AppSharedPreference(application)
    private val token = sharedPref.getValueString(AppConstant.TOKEN)

    private var roomDB: RoomDB
    private var roomDao: RoomDao

    init {
        roomDB = RoomDB.getInstance(application)
        roomDao = roomDB.getDao()
    }

    private val apiAuth = RequestBuilder.createService(NetAPI::class.java, AppConstant.BASE_URL, token)

    fun getCountry() = NetworkCall<MutableList<Country>>().makeCall(apiAuth.getCountry())

    fun userLogin(loginData: LoginData) = NetworkCall<LoginResponse>().makeCall(apiAuth.loginAsync(loginData))

    fun messageHistory(data: MsgHistoryData) = NetworkCall<MessageHistoryResponse>().makeCall(apiAuth.getMessageHistory(data))

    fun sendMessage(data: ChatMsgData) = NetworkCall<SendMessageResponse>().makeCall(apiAuth.sendMessage(data))

    fun logout(userId: String) = NetworkCall<LogoutResponse>().makeCall(apiAuth.logout(userId,AppConstant.DEVICE_STATUS,0))

    suspend fun saveUser(data: User) = roomDao.saveUser(data)

    fun getUser() = roomDao.getUser()

    suspend fun deleteUser(data: User) = roomDao.deleteUser(data)

    suspend fun clearDataTable(){
        roomDB.runInTransaction{
            runBlocking {
                roomDB.clearAllTables()
            }
        }
    }


    fun getSingleChatMessage(roomId: String) = roomDao.getSingleMessage(roomId)

    suspend fun saveSingleChatMessage(msg: ChatMessage) = roomDao.saveSingleChatMessage(msg)

}