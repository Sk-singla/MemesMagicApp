package com.samarth.memesmagic.data.remote.ws.models

import com.samarth.memesmagic.util.Constants.TYPE_MESSAGE_RECEIVED_ON_SERVER_ACK

data class MessageReachedServerAcknowledgement(
    val msgId:String
): BaseModel(TYPE_MESSAGE_RECEIVED_ON_SERVER_ACK)