package com.samarth.memesmagic.repository

import android.content.Context
import android.util.Log
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.StorageAccessLevel
import com.amplifyframework.storage.StorageException
import com.amplifyframework.storage.options.StorageUploadFileOptions
import com.samarth.memesmagic.data.local.dao.MemeDao
import com.samarth.memesmagic.data.local.entities.models.LocalNotification
import com.samarth.memesmagic.data.remote.*
import com.samarth.memesmagic.data.remote.request.LoginRequest
import com.samarth.memesmagic.data.remote.request.PostRequest
import com.samarth.memesmagic.data.remote.request.RegisterUserRequest
import com.samarth.memesmagic.data.remote.models.MemeTemplate
import com.samarth.memesmagic.data.remote.models.PrivateChatMessageStatus
import com.samarth.memesmagic.data.remote.request.CommentRequest
import com.samarth.memesmagic.data.remote.request.UserInfoRequest
import com.samarth.memesmagic.data.remote.response.*
import com.samarth.memesmagic.data.remote.response.meme_api_github.MemeApiGithub
import com.samarth.memesmagic.data.remote.ws.models.MessageReceived
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatMessage
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatRoom
import com.samarth.memesmagic.util.Constants.BEARER
import com.samarth.memesmagic.util.Constants.BUCKET_OBJECT_URL_PREFIX
import com.samarth.memesmagic.util.Constants.MAXIMUM_MEME_MAKER_PAGE_NUMBER
import com.samarth.memesmagic.util.Constants.NETWORK_UNKNOWN_PROBLEM
import com.samarth.memesmagic.util.Constants.NO_MEME
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.TokenHandler.getJwtToken
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.Flow
import java.io.File


@ActivityScoped
class MemeRepository(
    val memeApi: MemeApi,
    val imageFlipApi: ImageFlipApi,
    val memeMakerApi: MemeMakerApi,
    val memeGithubApi: MemeGithubApi,
    val webSocketApi: WebSocketApi,
    val context: Context,
    val memeDao:MemeDao
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

    override suspend fun registerWithGoogle(registerUserRequest: RegisterUserRequest): Resource<String> {
        return try {
            Log.d("TASK","=====================")
            val response = memeApi.userRegisterWithGoogle(registerUserRequest)
            if(response.success && response.data != null){
                Resource.Success(response.data)
            }  else {
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


    override suspend fun findUsers(searchKeyWord: String): Resource<List<UserInfo>> {
        return try{
            val response = memeApi.findUsers("$BEARER ${ getJwtToken(context) }",searchKeyWord)
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }

    override suspend fun followUser(email: String): Resource<UserInfo> {
        return try {
            val response = memeApi.followUser("$BEARER ${ getJwtToken(context) }",email)
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }

    override suspend fun unFollowUser(email: String): Resource<UserInfo> {
        return try {
            val response = memeApi.unFollowUser("$BEARER ${ getJwtToken(context) }",email)
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }

    override suspend fun getFeed():Resource<List<Post>> {

        return try{
            val response = memeApi.getFeed("$BEARER ${ getJwtToken(context) }")
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else{
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }

    }

    override suspend fun likePost(postId:String):Resource<UserInfo>{
        return try{
            val response = memeApi.likePost("$BEARER ${ getJwtToken(context) }",postId = postId)
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else{
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }

    override suspend fun dislikePost(postId:String):Resource<UserInfo>{
        return try{
            val response = memeApi.likePost("$BEARER ${ getJwtToken(context) }",postId = postId)
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else{
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }


    override suspend fun getUser(email: String): Resource<User> {
        return try{
            val response = memeApi.getUser("$BEARER ${ getJwtToken(context) }",email)
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
        userInfoRequest: UserInfoRequest
    ): Resource<UserInfo> {
        return try{
            val response = memeApi.updateUserInfo("$BEARER ${ getJwtToken(context) }",userInfoRequest)
            if(response.success && response.data != null){
                Resource.Success(response.data)
            } else{
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }

    override suspend fun getPosts(email: String): Resource<List<Post>> {
        return try {
            val response = memeApi.getPosts("$BEARER ${ getJwtToken(context) }",email)
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
        fileName: String,
        file:File?,
        onSuccess: (String) -> Unit,
        onFail: (String) -> Unit
    ){
        try {
            Log.d("video",fileName)

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
        postRequest: PostRequest
    ):Resource<String>{
        return try {
            val response = memeApi.uploadPost("$BEARER ${ getJwtToken(context) }",postRequest)
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
        postId: String,
        commentRequest: CommentRequest
    ): Resource<Comment> {
        return try {
            val response = memeApi.uploadComment("$BEARER ${ getJwtToken(context) }",postId,commentRequest)
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
        postId: String,
        commentId: String
    ): Resource<UserInfo> {
        return try {
            val response = memeApi.likeComment("$BEARER ${ getJwtToken(context) }",postId,commentId)
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
        postId: String,
        commentId: String
    ): Resource<UserInfo> {
        return try {
            val response = memeApi.dislikeComment("$BEARER ${ getJwtToken(context) }",postId,commentId)
            if(response.success && response.data!=null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }

    override suspend fun getCurrentMonthReward(): Resource<Reward> {
        return try {
            val response = memeApi.getCurrentMonthReward("$BEARER ${ getJwtToken(context) }")
            if(response.success && response.data!=null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }

    override suspend fun getLastYearReward(): Resource<Reward> {
        return try {
            val response = memeApi.getLastYearReward("$BEARER ${ getJwtToken(context) }")
            if(response.success && response.data!=null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }

    override suspend fun getRewards(email: String): Resource<List<Reward>> {
        return try {
            val response = memeApi.getRewards("$BEARER ${ getJwtToken(context) }",email)
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


    override suspend fun deletePost(post:Post): Resource<String> {
        return try {
            val response = memeApi.deletePost("$BEARER ${ getJwtToken(context) }",post.id)
            if(response.success && response.data!=null){

                val key = post.mediaLink.takeLast(post.mediaLink.length - BUCKET_OBJECT_URL_PREFIX.length)
                Log.d("post delete",post.mediaLink)
                Log.d("post delete",key)
                Amplify.Storage.remove(
                    key,
                    {},
                    {}
                )
                Resource.Success(response.data)

            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }

    override suspend fun updateFcmToken(fcmToken: String): Resource<String> {
        return try {
            val response = memeApi.updateFcmToken("$BEARER ${ getJwtToken(context) }",fcmToken)
            if(response.success && response.data!=null){
                Resource.Success(response.data)
            } else {
                Resource.Error(response.message)
            }
        } catch (e:Exception){
            Resource.Error(e.message ?: NETWORK_UNKNOWN_PROBLEM)
        }
    }


    override suspend fun saveNotification(localNotification: LocalNotification): Resource<String> {
        return try {
            memeDao.addNotification(localNotification)
            Resource.Success("Notification Added to db!")
        } catch (e:Exception){
            e.printStackTrace()
            Resource.Error("Exception: ${e.message}")
        }
    }

    override suspend fun seenNotification(notificationId: String): Resource<String> {
        return try {
            memeDao.seenNotification(notificationId)
            Resource.Success("Notification Updated(Seen)!")
        } catch (e:Exception){
            e.printStackTrace()
            Resource.Error("Exception: ${e.message}")
        }
    }

    override fun getAllNotifications(): Flow<List<LocalNotification>> =
        memeDao.getAllNotificationsOrderedByDate()

    override fun getAllUnseenNotificationCount(): Flow<Int> =
        memeDao.getAllUnSeenNotificationCount()

    override suspend fun savePrivateChatMessage(message: PrivateChatMessage) {
        memeDao.savePrivateMessage(message)
        memeDao.insertPrivateChatRoom(
            PrivateChatRoom(
                message.otherUserEmail,
                message.name,
                message.profilePic
            )
        )
    }

    override suspend fun messageReceived(msgId: String, messageSender: String) {
        webSocketApi.sendBaseModel(
            MessageReceived(
                msgId = msgId,
                msgSender = messageSender
            )
        )
        memeDao.updatePrivateChatMessageStatus(msgId, PrivateChatMessageStatus.RECEIVED.name)
    }
}