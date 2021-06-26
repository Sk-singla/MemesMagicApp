package com.samarth.memesmagic.repository

import com.samarth.data.models.request.LoginRequest
import com.samarth.data.models.request.RegisterUserRequest
import com.samarth.memesmagic.data.remote.ImageFlipApi
import com.samarth.memesmagic.data.remote.MemeApi
import com.samarth.memesmagic.data.remote.MemeMakerApi
import com.samarth.memesmagic.data.remote.models.MemeTemplate
import com.samarth.memesmagic.data.remote.response.Post
import com.samarth.memesmagic.data.remote.response.User
import com.samarth.memesmagic.util.Constants.MAXIMUM_MEME_MAKER_PAGE_NUMBER
import com.samarth.memesmagic.util.Constants.NO_MEME
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.TokenHandler.saveJwtToken
import dagger.hilt.android.scopes.ActivityScoped


@ActivityScoped
class MemeRepository(
    val memeApi: MemeApi,
    val imageFlipApi: ImageFlipApi,
    val memeMakerApi: MemeMakerApi
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

    override suspend fun getMemeTemplates(memeMakerPageNumber:Int):Resource<List<MemeTemplate>> {
        return try{

            val imageFlipResponse = if(memeMakerPageNumber == 0){
                imageFlipApi.getImageFlipTemplates()
            } else null

            val memeMakerResponse = if(memeMakerPageNumber in 1..MAXIMUM_MEME_MAKER_PAGE_NUMBER){
                memeMakerApi.getMemeTemplates(memeMakerPageNumber)
            } else null

            if(imageFlipResponse == null && memeMakerResponse == null){
                Resource.Error(NO_MEME)
            } else if(imageFlipResponse != null){
               Resource.Success(imageFlipResponse.data.memes.map { MemeTemplate(url = it.url,id = it.id) }.shuffled())
            } else {
                Resource.Success(memeMakerResponse!!.data.map { MemeTemplate(url=it.image,id = "${it.ID}") })
            }
        }catch (e:Exception){
            Resource.Error(e.message ?: "Error!")
        }
    }
}