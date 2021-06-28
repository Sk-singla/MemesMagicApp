package com.samarth.memesmagic.data.remote

import com.samarth.memesmagic.data.remote.response.imageflip.ImageFlipResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface ImageFlipApi {


    @Headers("Content-Type: application/json")
    @GET("/get_memes")
    suspend fun getImageFlipTemplates():ImageFlipResponse
}