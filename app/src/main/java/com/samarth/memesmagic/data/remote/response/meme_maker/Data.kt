package com.samarth.memesmagic.data.remote.response.meme_maker

data class Data(
    val ID: Int,
    val bottomText: String,
    val image: String,
    val name: String,
    val rank: Int,
    val tags: String,
    val topText: String
)