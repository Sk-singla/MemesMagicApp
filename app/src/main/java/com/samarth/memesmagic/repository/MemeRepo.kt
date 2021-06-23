package com.samarth.memesmagic.repository

import com.samarth.data.models.request.LoginRequest
import com.samarth.data.models.request.RegisterUserRequest
import com.samarth.memesmagic.data.remote.response.SimpleResponse
import com.samarth.memesmagic.util.Resource

interface MemeRepo {

    suspend fun registerUser(
        userRegisterRequest:RegisterUserRequest
    ):Resource<String>

    suspend fun loginUser(
        loginRequest: LoginRequest
    ):Resource<String>



}