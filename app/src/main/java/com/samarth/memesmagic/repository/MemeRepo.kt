package com.samarth.memesmagic.repository

import android.content.Context
import com.samarth.memesmagic.data.remote.request.LoginRequest
import com.samarth.memesmagic.data.remote.request.PostRequest
import com.samarth.memesmagic.data.remote.request.RegisterUserRequest
import com.samarth.memesmagic.data.remote.models.MemeTemplate
import com.samarth.memesmagic.data.remote.request.CommentRequest
import com.samarth.memesmagic.data.remote.request.UserInfoRequest
import com.samarth.memesmagic.data.remote.response.*
import com.samarth.memesmagic.data.remote.response.meme_api_github.MemeApiGithub
import com.samarth.memesmagic.util.Resource
import java.io.File

interface MemeRepo {

    suspend fun registerUser(
        userRegisterRequest: RegisterUserRequest
    ):Resource<String>

    suspend fun loginUser(
        loginRequest: LoginRequest
    ):Resource<String>


    suspend fun getFeed():Resource<List<Post>>

    suspend fun getUser(email:String):Resource<User>
    suspend fun updateUserInfo(userInfoRequest: UserInfoRequest):Resource<UserInfo>

    suspend fun getPosts(email:String):Resource<List<Post>>

    suspend fun getMemeTemplates(memeMakerPageNumber:Int):Resource<List<MemeTemplate>>

    suspend fun uploadFileOnAwsS3(fileName:String,file:File?, onSuccess:(String)->Unit, onFail:(String)->Unit)

    suspend fun uploadPost(postRequest: PostRequest):Resource<String>

    suspend fun likePost(postId:String):Resource<UserInfo>
    suspend fun dislikePost(postId:String):Resource<UserInfo>

    suspend fun findUsers( searchKeyWord:String):Resource<List<UserInfo>>

    suspend fun followUser(email: String):Resource<UserInfo>
    suspend fun unFollowUser(email: String):Resource<UserInfo>


    suspend fun addComment( postId: String, commentRequest: CommentRequest):Resource<Comment>
    suspend fun likeComment(postId: String,commentId:String):Resource<UserInfo>
    suspend fun dislikeComment(postId: String,commentId:String):Resource<UserInfo>

    suspend fun getCurrentMonthReward():Resource<Reward>
    suspend fun getRewards(email: String):Resource<List<Reward>>
    suspend fun getLastYearReward():Resource<Reward>

    suspend fun getMemesFromGithubApi():Resource<MemeApiGithub>

    suspend fun deletePost(postId: String):Resource<String>

    suspend fun updateFcmToken(fcmToken:String): Resource<String>
    /**
     * 1. Share meme on other platforms -> Done
     * 2. send friend request / follow , unfollow
     * 3. comments
     * 5. Rewards
     *
     * rewards -> make a table in table in ktor -> reward type, email who won reward, time,
     *
     * posts  lazy column in profile when click on post item,  single post when screen for feed post
     * 7. feed algo
     * 8. Make default fn calls in EFFECTS
     * Landing page
     * 10. Logo
     * light Mode -> primary colour
     * todo -> user experience, clone photo editor repo and do something
     * comment box background not transparent
     *
     */


}