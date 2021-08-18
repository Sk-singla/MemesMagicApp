package com.samarth.memesmagic.data.remote.ws.models

import com.samarth.memesmagic.util.Constants.TYPE_DELETE_MESSAGE

data class DeleteMessage(
    val msgId:String,
    val messageSender:String
): BaseModel(TYPE_DELETE_MESSAGE)
