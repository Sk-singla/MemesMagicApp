package com.samarth.memesmagic.data.remote.ws.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PrivateChatRoom(
    @PrimaryKey(autoGenerate = false)
    val userEmail:String,
    val name: String,
    val profilePic: String?,
)
