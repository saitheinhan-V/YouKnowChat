package com.chat.youknow.utils

import okhttp3.ResponseBody
import org.json.JSONObject

class ErrorHandler {

    fun apiError(response: ResponseBody?): String? {
        val jsonObject: JSONObject
        var message = ""
        try {
            val errorBody = response?.string()
            jsonObject = JSONObject(errorBody)
            message = jsonObject.getString("message")
        } catch (e: Exception) {
            e.printStackTrace()
            message = "Server Response Error"
        }
        return message
    }

    fun networkError(message: String?): String? {
        return message
    }

}