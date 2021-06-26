package com.samarth.memesmagic.repository

import com.samarth.data.models.request.LoginRequest
import com.samarth.data.models.request.RegisterUserRequest
import com.samarth.memesmagic.data.remote.MemeApi
import com.samarth.memesmagic.data.remote.response.Post
import com.samarth.memesmagic.data.remote.response.User
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.TokenHandler.saveJwtToken
import dagger.hilt.android.scopes.ActivityScoped


@ActivityScoped
class MemeRepository(
    val memeApi: MemeApi
): MemeRepo{

    override suspend fun registerUser(userRegisterRequest: RegisterUserRequest): Resource<String> {
        return try {
            val response = memeApi.userRegister(userRegisterRequest)
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: "Some Problem Occurred!")
        }
    }


    override suspend fun loginUser(loginRequest: LoginRequest): Resource<String> {
        return try{
            val response = memeApi.userLogin(loginRequest)
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        }catch (e:Exception){
            Resource.Error(e.message ?: "Some Problem Occurred!")
        }
    }



    override suspend fun getFeed(token:String):Resource<List<Post>> {

        return try{
            val response = memeApi.getFeed("Bearer $token")
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else{
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: "Error!")
        }

    }


    override suspend fun getUser(token: String,email: String): Resource<User> {
        return try{
            val response = memeApi.getUser("Bearer $token",email)
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else{
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: "Error!")
        }
    }

    override suspend fun getPosts(token: String, email: String): Resource<List<Post>> {
        return try {
            val response = memeApi.getPosts("Bearer $token",email)
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else{
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: "Error!")
        }
    }
















}