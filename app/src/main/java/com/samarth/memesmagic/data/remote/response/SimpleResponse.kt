package com.samarth.memesmagic.data.remote.response

data class SimpleResponse<T>(
    val success:Boolean,
    val message:String,
    val data:T?
)
