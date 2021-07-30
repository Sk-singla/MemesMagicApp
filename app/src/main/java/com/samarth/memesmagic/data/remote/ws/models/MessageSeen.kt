package com.samarth.memesmagic.data.remote.ws.models

import com.samarth.memesmagic.util.Constants.TYPE_MESSAGE_SEEN


data class MessageSeen(
    val msgId: String,
    val msgSender:String
): BaseModel(TYPE_MESSAGE_SEEN)
