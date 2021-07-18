package com.samarth.memesmagic.data.remote.response



data class User(
    var userInfo:UserInfo,
    val hashPassword:String,
    val rewards: MutableList<Reward> = mutableListOf(),
    var postCount:Int = 0,
    val followings:MutableList<UserInfo> = mutableListOf(),
    val followers:MutableList<UserInfo> = mutableListOf(),
    val id:String
)
