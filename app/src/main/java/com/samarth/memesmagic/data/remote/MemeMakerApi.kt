package com.samarth.memesmagic.data.remote

import com.samarth.memesmagic.data.remote.response.meme_maker.MemeMakerResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface MemeMakerApi {


    @Headers("Content-Type: application/json")
    @GET("/{page}")
    suspend fun getMemeTemplates(
        @Path("page") pageNumber:Int
    ):MemeMakerResponse
}