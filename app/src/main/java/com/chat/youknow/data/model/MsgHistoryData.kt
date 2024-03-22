package com.chat.youknow.data.model

data class MsgHistoryData(
    val userId: String,
    val roomId: String,
    val isGroup: Boolean ?= false,
    val prev: String ?= "",
    val next: String ?= "",
    val limit: Int ?= 100,
    val locale: Int ?= 0

)
