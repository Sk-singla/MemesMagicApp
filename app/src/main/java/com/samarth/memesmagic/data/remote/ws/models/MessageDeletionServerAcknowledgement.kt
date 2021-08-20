package com.samarth.memesmagic.data.remote.ws.models

import com.samarth.memesmagic.util.Constants.TYPE_MESSAGE_DELETE_SERVER_ACK

/**
 * Just an acknowledgment that Message delete request has been reached on server or not
 */
data class MessageDeletionServerAcknowledgement(
    val msgId:String
):BaseModel(TYPE_MESSAGE_DELETE_SERVER_ACK)
