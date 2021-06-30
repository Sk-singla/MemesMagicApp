package com.samarth.memesmagic.data.remote.response



data class User(
    val userInfo:UserInfo,
    val hashPassword:String,
    val rewards: MutableList<Reward> = mutableListOf(),
    val postCount:Int = 0,
    val followings:MutableList<UserInfo> = mutableListOf(),
    val followers:MutableList<UserInfo> = mutableListOf(),
    val id:String
)
