package com.chat.youknow.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "chatMessage"
)
data class ChatMessage(
    @PrimaryKey(autoGenerate = true)
    var id: Int= 0,
    var roomId: String ?= "",
    var time: Long ?=null,
    var mainContentType: Int ?=0,
    var deleteStatus: String ?="",
    var fromUserId: String ?="",
    var fromUserName: String ?="",
    var fromUserProfileUrl: String ?=null,
    var messageId: String?="",
    var uuid: String?="",
    var subContentType: Int?=0,
    var content: String?=""

): Serializable
