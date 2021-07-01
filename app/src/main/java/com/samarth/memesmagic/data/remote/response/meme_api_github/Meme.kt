package com.samarth.memesmagic.data.remote.response.meme_api_github

data class Meme(
    val author: String,
    val nsfw: Boolean,
    val postLink: String,
    val preview: List<String>,
    val spoiler: Boolean,
    val subreddit: String,
    val title: String,
    val ups: Int,
    val url: String
)