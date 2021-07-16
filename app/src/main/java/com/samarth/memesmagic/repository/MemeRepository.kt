package com.samarth.memesmagic.repository

import android.content.Context
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.StorageAccessLevel
import com.amplifyframework.storage.StorageException
import com.amplifyframework.storage.options.StorageUploadFileOptions
import com.samarth.memesmagic.data.remote.request.LoginRequest
import com.samarth.memesmagic.data.remote.request.PostRequest
import com.samarth.memesmagic.data.remote.request.RegisterUserRequest
import com.samarth.memesmagic.data.remote.ImageFlipApi
import com.samarth.memesmagic.data.remote.MemeApi
import com.samarth.memesmagic.data.remote.MemeGithubApi
import com.samarth.memesmagic.data.remote.MemeMakerApi
import com.samarth.memesmagic.data.remote.models.MemeTemplate
import com.samarth.memesmagic.data.remote.request.CommentRequest
import com.samarth.memesmagic.data.remote.request.UserInfoRequest
import com.samarth.memesmagic.data.remote.response.*
import com.samarth.memesmagic.data.remote.response.meme_api_github.MemeApiGithub
import com.samarth.memesmagic.util.Constants.BEARER
import com.samarth.memesmagic.util.Constants.MAXIMUM_MEME_MAKER_PAGE_NUMBER
import com.samarth.memesmagic.util.Constants.NETWORK_UNKNOWN_PROBLEM
import com.samarth.memesmagic.util.Constants.NO_MEME
import com.samarth.memesmagic.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import java.io.File


@ActivityScoped
class MemeRepository(
    val memeApi: MemeApi,
    val imageFlipApi: ImageFlipApi,
    val memeMakerApi: MemeMakerApi,
    val memeGithubApi: MemeGithubApi
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
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
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
            Resource.Error(e.message ?:NETWORK_UNKNOWN_PROBLEM)
        }
    }


    override suspend fun findUsers(token: String, searchKeyWord: String): Resource<List<UserInfo>> {
        return try{
            val response = memeApi.findUsers("$BEARER $token",searchKeyWord)
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }

    override suspend fun followUser(token: String, email: String): Resource<UserInfo> {
        return try {
            val response = memeApi.followUser("$BEARER $token",email)
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }

    override suspend fun unFollowUser(token: String, email: String): Resource<UserInfo> {
        return try {
            val response = memeApi.unFollowUser("$BEARER $token",email)
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }

    override suspend fun getFeed(token:String):Resource<List<Post>> {

        return try{
            val response = memeApi.getFeed("$BEARER $token")
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else{
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }

    }

    override suspend fun likePost(token: String,postId:String):Resource<UserInfo>{
        return try{
            val response = memeApi.likePost("$BEARER $token",postId = postId)
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else{
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }

    override suspend fun dislikePost(token: String,postId:String):Resource<UserInfo>{
        return try{
            val response = memeApi.likePost("$BEARER $token",postId = postId)
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else{
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }


    override suspend fun getUser(token: String,email: String): Resource<User> {
        return try{
            val response = memeApi.getUser("$BEARER $token",email)
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else{
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }

    override suspend fun updateUserInfo(
        token: String,
        userInfoRequest: UserInfoRequest
    ): Resource<UserInfo> {
        return try{
            val response = memeApi.updateUserInfo("$BEARER $token",userInfoRequest)
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else{
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }

    override suspend fun getPosts(token: String, email: String): Resource<List<Post>> {
        return try {
            val response = memeApi.getPosts("$BEARER $token",email)
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else{
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
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
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }


    override suspend fun uploadFileOnAwsS3(
        context: Context,
        fileName: String,
        file:File?,
        onSuccess: (String) -> Unit,
        onFail: (String) -> Unit
    ){
        try {


            if(file == null){
                onFail("File Not Found!")
                return
            }

            val options = StorageUploadFileOptions.builder()
                .accessLevel(StorageAccessLevel.PUBLIC)
                .targetIdentityId("Posts")
                .build()


            Amplify.Storage.uploadFile(
                fileName,
                file,
                options,
                {
                    onSuccess(it.key)
                },
                {
                    onFail(it.message ?: NETWORK_UNKNOWN_PROBLEM)
                }
            )
        } catch (error: StorageException) {
            onFail(error.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }


    override suspend fun uploadPost(
        token:String,
        postRequest: PostRequest
    ):Resource<String>{
        return try {
            val response = memeApi.uploadPost("$BEARER $token",postRequest)
            if(response.success && response.data!=null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?:NETWORK_UNKNOWN_PROBLEM)
        }
    }


    override suspend fun addComment(
        token: String,
        postId: String,
        commentRequest: CommentRequest
    ): Resource<Comment> {
        return try {
            val response = memeApi.uploadComment("$BEARER $token",postId,commentRequest)
            if(response.success && response.data!=null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }

    override suspend fun likeComment(
        token: String,
        postId: String,
        commentId: String
    ): Resource<UserInfo> {
        return try {
            val response = memeApi.likeComment("$BEARER $token",postId,commentId)
            if(response.success && response.data!=null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?:NETWORK_UNKNOWN_PROBLEM)
        }
    }

    override suspend fun dislikeComment(
        token: String,
        postId: String,
        commentId: String
    ): Resource<UserInfo> {
        return try {
            val response = memeApi.dislikeComment("$BEARER $token",postId,commentId)
            if(response.success && response.data!=null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }

    override suspend fun getCurrentMonthReward(token: String): Resource<Reward> {
        return try {
            val response = memeApi.getCurrentMonthReward("$BEARER $token")
            if(response.success && response.data!=null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }

    override suspend fun getLastYearReward(token: String): Resource<Reward> {
        return try {
            val response = memeApi.getLastYearReward("$BEARER $token")
            if(response.success && response.data!=null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }

    override suspend fun getMyRewards(token: String,email: String): Resource<List<Reward>> {
        return try {
            val response = memeApi.getRewards("$BEARER $token",email)
            if(response.success && response.data!=null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }

    override suspend fun getMemesFromGithubApi():Resource<MemeApiGithub>{
        return try {
            Resource.Success(memeGithubApi.getMemes())
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }


    override suspend fun deletePost(token: String, postId: String): Resource<String> {
        return try {
            val response = memeApi.deletePost("$BEARER $token",postId)
            if(response.success && response.data!=null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }

    override suspend fun updateFcmToken(token: String, fcmToken: String): Resource<String> {
        return try {
            val response = memeApi.updateFcmToken("$BEARER $token",fcmToken)
            if(response.success && response.data!=null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }
}