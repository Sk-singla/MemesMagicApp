package com.samarth.memesmagic.repository

import android.content.Context
import android.util.Log
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.Consumer
import com.amplifyframework.storage.StorageAccessLevel
import com.amplifyframework.storage.StorageException
import com.amplifyframework.storage.options.StorageListOptions
import com.amplifyframework.storage.options.StorageUploadFileOptions
import com.amplifyframework.storage.result.StorageUploadFileResult
import com.samarth.data.models.request.LoginRequest
import com.samarth.data.models.request.PostRequest
import com.samarth.data.models.request.RegisterUserRequest
import com.samarth.memesmagic.data.remote.ImageFlipApi
import com.samarth.memesmagic.data.remote.MemeApi
import com.samarth.memesmagic.data.remote.MemeMakerApi
import com.samarth.memesmagic.data.remote.models.MemeTemplate
import com.samarth.memesmagic.data.remote.request.CommentRequest
import com.samarth.memesmagic.data.remote.response.Comment
import com.samarth.memesmagic.data.remote.response.Post
import com.samarth.memesmagic.data.remote.response.User
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.util.Constants.MAXIMUM_MEME_MAKER_PAGE_NUMBER
import com.samarth.memesmagic.util.Constants.NO_MEME
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.TokenHandler.saveJwtToken
import dagger.hilt.android.scopes.ActivityScoped
import java.io.File


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


    override suspend fun findUsers(token: String, searchKeyWord: String): Resource<List<UserInfo>> {
        return try{
            val response = memeApi.findUsers("Bearer $token",searchKeyWord)
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: "Some Problem Occurred!!")
        }
    }

    override suspend fun followUser(token: String, email: String): Resource<UserInfo> {
        return try {
            val response = memeApi.followUser("Bearer $token",email)
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: "Some Problem Occurred!!")
        }
    }

    override suspend fun unFollowUser(token: String, email: String): Resource<UserInfo> {
        return try {
            val response = memeApi.unFollowUser("Bearer $token",email)
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: "Some Problem Occurred!!")
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

    override suspend fun likePost(token: String,postId:String):Resource<UserInfo>{
        return try{
            val response = memeApi.likePost("Bearer $token",postId = postId)
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else{
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: "Error!")
        }
    }

    override suspend fun dislikePost(token: String,postId:String):Resource<UserInfo>{
        return try{
            val response = memeApi.likePost("Bearer $token",postId = postId)
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


    override suspend fun uploadFileOnAwsS3(
        context: Context,
        fileName: String,
        onSuccess: (String) -> Unit,
        onFail: (String) -> Unit
    ){
        try {
            val files = context.filesDir.listFiles()
            val file = files?.find { it.canRead() && it.isFile && it.name == fileName }

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
                    onFail(it.message ?: "Some Problem Occurred!!")
                }
            )
        } catch (error: StorageException) {
            onFail(error.message ?: "Some Problem Occurred!")
        }
    }


    override suspend fun uploadPost(
        token:String,
        postRequest: PostRequest
    ):Resource<String>{
        return try {
            val response = memeApi.uploadPost("Bearer $token",postRequest)
            if(response.success && response.data!=null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: "Some Problem Occurred!!")
        }
    }


    override suspend fun addComment(
        token: String,
        postId: String,
        commentRequest: CommentRequest
    ): Resource<Comment> {
        return try {
            val response = memeApi.uploadComment("Bearer $token",postId,commentRequest)
            if(response.success && response.data!=null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: "Some Problem Occurred!!")
        }
    }

    override suspend fun likeComment(
        token: String,
        postId: String,
        commentId: String
    ): Resource<UserInfo> {
        return try {
            val response = memeApi.likeComment("Bearer $token",postId,commentId)
            if(response.success && response.data!=null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: "Some Problem Occurred!!")
        }
    }

    override suspend fun dislikeComment(
        token: String,
        postId: String,
        commentId: String
    ): Resource<UserInfo> {
        return try {
            val response = memeApi.dislikeComment("Bearer $token",postId,commentId)
            if(response.success && response.data!=null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: "Some Problem Occurred!!")
        }
    }
}