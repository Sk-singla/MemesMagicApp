package com.samarth.memesmagic.data.remote.response

data class Comment(
    val userInfo: UserInfo,
    val text:String,
    val time:Long,
    val likedBy:MutableList<UserInfo>,
    val id:String = "${System.currentTimeMillis()}id"
)
