package com.samarth.memesmagic.data.remote.ws.models

import androidx.compose.runtime.MutableState
import com.samarth.memesmagic.data.remote.response.UserInfo

data class PrivateChatRoom(
    val userEmail:String,
    val name: String,
    val profilePic: String?,
    var lastMessage: PrivateChatMessage? = null
)
