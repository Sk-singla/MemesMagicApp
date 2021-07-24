package com.samarth.memesmagic.data.remote.ws.models

import com.samarth.memesmagic.util.Constants.TYPE_PRIVATE_CHAT_MESSAGE

data class PrivateChatMessage(
    val from: String,
    val to: String,
    val message: String,
    val timeStamp: Long,
    val id: String,
    val replyOf: String?=null
): BaseModel(TYPE_PRIVATE_CHAT_MESSAGE)