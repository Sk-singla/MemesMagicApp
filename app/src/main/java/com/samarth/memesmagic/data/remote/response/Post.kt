package com.samarth.memesmagic.data.remote.response

import com.samarth.memesmagic.data.remote.models.PostType

data class Post(
    val id:String,
    val createdBy: UserInfo,
    val postType: PostType,
    val time:Long,
    val likedBy:MutableList<UserInfo> = mutableListOf(),
    val comments:MutableList<Comment> = mutableListOf(),
    val tags:List<String>?=null,
    val mediaLink:String?=null,
    val text:String?=null
)
