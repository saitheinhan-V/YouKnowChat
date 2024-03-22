package com.chat.youknow.data.model

data class LoginData(
    val countryId: Int,
    val mobile: String,
    val password: String,
    val deviceStatus: String ?= "mobile",
    val locale: Int ?= 0,
    val pushServiceType: String ?= "firebase",
    val deviceType: String ?= "android",
    val pushToken: String ?= "",
    val deviceId: String ?= "1",
    val deviceName: String ?= "xiaomi"
)
