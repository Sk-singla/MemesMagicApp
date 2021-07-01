package com.samarth.memesmagic.data.remote

import com.samarth.memesmagic.data.remote.response.meme_api_github.MemeApiGithub
import retrofit2.http.GET
import retrofit2.http.Headers

interface MemeGithubApi {


    @Headers("Content-Type: application/json")
    @GET("/gimme/30")
    suspend fun getMemes():MemeApiGithub
}