package com.chat.youknow

import android.app.Application
import android.content.Context
import com.chat.youknow.data.database.RoomDB
import com.chat.youknow.data.database.RoomDao
import java.util.*

class MyApplication : Application() {

    companion object {
        fun newInstance() = MyApplication()
    }

    lateinit var context: Context
    lateinit var roomDB: RoomDB
    lateinit var roomDao: RoomDao
    var uid = 0

    override fun onCreate() {
        super.onCreate()

        context = applicationContext

        uid = Random().nextInt(100000)

//        roomDB = RoomDB.getInstance(this)
//        roomDao = roomDB.getDao()

    }
}