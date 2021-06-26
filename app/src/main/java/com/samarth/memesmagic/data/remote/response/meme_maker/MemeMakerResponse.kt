package com.samarth.memesmagic.data.remote.response.meme_maker

data class MemeMakerResponse(
    val code: Int,
    val data: List<Data>,
    val message: String,
    val next: String
)