package com.samarth.memesmagic.data.remote

import com.samarth.data.models.request.LoginRequest
import com.samarth.data.models.request.RegisterUserRequest
import com.samarth.memesmagic.data.remote.response.SimpleResponse
import com.samarth.memesmagic.util.Constants.USERS
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MemeApi {


    @Headers("Content-Type: application/json")
    @POST("$USERS/register")
    suspend fun userRegister(
        @Body registerUserRequest: RegisterUserRequest
    ): SimpleResponse<String>

    @Headers("Content-Type: application/json")
    @POST("$USERS/login")
    suspend fun userLogin(
        @Body loginRequest: LoginRequest
    ): SimpleResponse<String>















}