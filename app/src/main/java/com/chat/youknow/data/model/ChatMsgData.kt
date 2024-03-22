package com.chat.youknow.data.model

data class ChatMsgData(
    val text: String ="",
    val userId: String = "",
    val roomId: String = "",
    val uuid: String = "",
    val isGroup: Boolean ?= false,
    val locale: Int ?= 0,
    val deviceStatus: String ?= "mobile",
)
