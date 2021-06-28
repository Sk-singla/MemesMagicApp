package com.samarth.memesmagic.repository

import android.content.Context
import com.samarth.data.models.request.LoginRequest
import com.samarth.data.models.request.PostRequest
import com.samarth.data.models.request.RegisterUserRequest
import com.samarth.memesmagic.data.remote.models.MemeTemplate
import com.samarth.memesmagic.data.remote.response.Post
import com.samarth.memesmagic.data.remote.response.SimpleResponse
import com.samarth.memesmagic.data.remote.response.User
import com.samarth.memesmagic.util.Resource

interface MemeRepo {

    suspend fun registerUser(
        userRegisterRequest:RegisterUserRequest
    ):Resource<String>

    suspend fun loginUser(
        loginRequest: LoginRequest
    ):Resource<String>


    suspend fun getFeed(token:String):Resource<List<Post>>

    suspend fun getUser(token: String,email:String):Resource<User>

    suspend fun getPosts(token: String,email:String):Resource<List<Post>>

    suspend fun getMemeTemplates(memeMakerPageNumber:Int):Resource<List<MemeTemplate>>

    suspend fun uploadFileOnAwsS3(context: Context, fileName:String, onSuccess:(String)->Unit, onFail:(String)->Unit)

    suspend fun uploadPost(token:String,postRequest: PostRequest):Resource<String>



}