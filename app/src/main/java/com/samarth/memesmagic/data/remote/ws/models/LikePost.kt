package com.samarth.memesmagic.data.remote.ws.models

import com.samarth.memesmagic.util.Constants.TYPE_LIKE_POST

data class LikePost(
    val postId: String
): BaseModel(TYPE_LIKE_POST) {
}