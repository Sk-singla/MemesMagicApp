package com.samarth.memesmagic.data.remote.ws.models

import com.samarth.memesmagic.util.Constants.TYPE_MESSAGE_RECEIVED


data class MessageReceived(
    val msgId: String,
    val msgSender:String
): BaseModel(TYPE_MESSAGE_RECEIVED)
