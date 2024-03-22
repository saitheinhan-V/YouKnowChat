package com.chat.youknow.network

import com.chat.youknow.data.model.ChatMsgData
import com.chat.youknow.data.model.LoginData
import com.chat.youknow.data.model.MsgHistoryData
import com.chat.youknow.data.response.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NetAPI {

    @GET("/user-service/master/countries")
    fun getCountry(
        @Query("locale") locale: Int ?= 0
    ): Call<MutableList<Country>>

    @POST("/user-service/login")
    fun loginAsync(@Body loginData: LoginData): Call<LoginResponse>

    @POST("/chat-service/message/history")
    fun getMessageHistory(@Body data: MsgHistoryData): Call<MessageHistoryResponse>

    @POST("/chat-service/message/text")
    fun sendMessage(@Body data: ChatMsgData): Call<SendMessageResponse>

    @POST("/user-service/logout")
    fun logout(
        @Query("userId") userId: String,
        @Query("deviceStatus") deviceStatus: String?= "mobile",
        @Query("locale") locale: Int ?= 0
    ): Call<LogoutResponse>
}