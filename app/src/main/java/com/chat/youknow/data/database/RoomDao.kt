/*
 * *
 *  * Created by Rafsan Ahmad on 9/27/21, 5:30 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *
 */

package com.chat.youknow.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.chat.youknow.data.model.ChatMessage
import com.chat.youknow.data.model.User

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: User)

    @Query("SELECT * FROM user")
    fun getUser(): LiveData<User>

    @Delete(entity = User::class)
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM user")
    suspend fun deleteAllUser()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveSingleChatMessage(msg: ChatMessage)

    @Query("SELECT * from chatMessage WHERE roomId = :id")
    fun getSingleMessage(id: String): LiveData<List<ChatMessage>>
}