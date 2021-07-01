package com.samarth.memesmagic.data.remote.request

data class UserInfoRequest(
    val name:String,
    val profilePic:String?=null,
    val bio:String?=null
)
