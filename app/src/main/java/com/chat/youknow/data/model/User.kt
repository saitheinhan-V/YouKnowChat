package com.chat.youknow.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(
    tableName = "user"
)
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = 0,
    val userId: String ?= "",
    val username: String ?= "",
    val gender: Int ?= 0,
    val countryId: Int ?= -1,
    val mobile: String ?= "",
    val accessToken: String ?= "",
    val refreshToken: String ?= "",
): Serializable
