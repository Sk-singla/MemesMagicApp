package com.samarth.memesmagic.data.remote.request

data class RegisterUserRequest(
    val name:String,
    val email:String,
    val password:String,
    val profilePic:String?=null
)
