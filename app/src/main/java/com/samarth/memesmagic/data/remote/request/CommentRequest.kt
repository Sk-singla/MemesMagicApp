package com.samarth.memesmagic.data.remote.request

data class CommentRequest(
    val text:String,
    val time:Long,
    val id:String = "${System.currentTimeMillis()}id"
)
