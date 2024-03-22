package com.chat.youknow.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.TypeConverters
import com.chat.youknow.data.model.ChatMessage
import com.chat.youknow.data.model.User


@Database(entities = [User::class,ChatMessage::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
 abstract class RoomDB : RoomDatabase(){

     abstract fun getDao() : RoomDao

    companion object {

        @Volatile
        private var INSTANCE: RoomDB? = null

        fun getInstance(context: Context): RoomDB {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDB::class.java,
                    "youknow_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

//        private var instance: NewsRoomDatabase? = null
//
//        @Synchronized
//        fun getInstance(ctx: Context): NewsRoomDatabase {
//            if(instance == null)
//                instance = Room.databaseBuilder(ctx.applicationContext, NewsRoomDatabase::class.java,
//                    "news_db")
////                    .fallbackToDestructiveMigration()
//                    .addCallback(roomCallback)
//                    .build()
//
//            return instance!!
//
//        }

//        fun getDatabase(context: Context): NewsRoomDatabase =
//            instance ?: synchronized(this) {
//                instance ?: buildDatabase(context).also {
//                    instance = it
//                }
//            }
//
//        //Build a local database to store data
//        private fun buildDatabase(appContext: Context) =
//            Room.databaseBuilder(appContext, NewsRoomDatabase::class.java, "news_db")
//                .fallbackToDestructiveMigration()
//                .addCallback(roomCallback)
//                .build()

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }
        }
    }
 }