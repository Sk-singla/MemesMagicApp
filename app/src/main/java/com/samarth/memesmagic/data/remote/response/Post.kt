package com.samarth.memesmagic.data.remote.response

import com.samarth.memesmagic.data.remote.models.PostResource
import com.samarth.memesmagic.data.remote.models.PostType

data class Post(
    val id:String,
    val createdBy: UserInfo,
    val postType: PostType,
    val time:Long,
    val likedBy:MutableList<UserInfo> = mutableListOf(),
    var comments:MutableList<Comment> = mutableListOf(),
    val tags:List<String>?=null,
    val mediaLink:String,
    val description:String?=null,
    val postResource: PostResource = PostResource.MY_API
)
