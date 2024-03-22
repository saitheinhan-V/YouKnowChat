package com.chat.youknow.data.response

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val profileInfo: ProfileInfo = ProfileInfo()
){
    data class ProfileInfo(
        val userId: String = "",
        val username: String = "",
        val gender: Int = 0,
        val countryId: Int = -1,
        val mobile: String = "",

    )
}

data class LogoutResponse(
    val isSuccess: Boolean ?= false
)

