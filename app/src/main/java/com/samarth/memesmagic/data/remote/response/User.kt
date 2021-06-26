package com.samarth.memesmagic.data.remote.response



data class User(
    val userInfo:UserInfo,
    val hashPassword:String,
    val memeBadges: MutableList<MemeBadge> = mutableListOf(),
    val postCount:Int = 0,
    val followings:MutableList<UserInfo> = mutableListOf(),
    val followers:MutableList<UserInfo> = mutableListOf(),
    val id:String
)
