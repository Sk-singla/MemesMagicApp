package com.samarth.memesmagic.repository

import android.content.Context
import com.samarth.data.models.request.LoginRequest
import com.samarth.data.models.request.PostRequest
import com.samarth.data.models.request.RegisterUserRequest
import com.samarth.memesmagic.data.remote.models.MemeTemplate
import com.samarth.memesmagic.data.remote.request.CommentRequest
import com.samarth.memesmagic.data.remote.request.UserInfoRequest
import com.samarth.memesmagic.data.remote.response.*
import com.samarth.memesmagic.data.remote.response.meme_api_github.MemeApiGithub
import com.samarth.memesmagic.util.Resource
import java.io.File

interface MemeRepo {

    suspend fun registerUser(
        userRegisterRequest:RegisterUserRequest
    ):Resource<String>

    suspend fun loginUser(
        loginRequest: LoginRequest
    ):Resource<String>


    suspend fun getFeed(token:String):Resource<List<Post>>

    suspend fun getUser(token: String,email:String):Resource<User>
    suspend fun updateUserInfo(token: String,userInfoRequest: UserInfoRequest):Resource<UserInfo>

    suspend fun getPosts(token: String,email:String):Resource<List<Post>>

    suspend fun getMemeTemplates(memeMakerPageNumber:Int):Resource<List<MemeTemplate>>

    suspend fun uploadFileOnAwsS3(context: Context, fileName:String,file:File?, onSuccess:(String)->Unit, onFail:(String)->Unit)

    suspend fun uploadPost(token:String,postRequest: PostRequest):Resource<String>

    suspend fun likePost(token: String,postId:String):Resource<UserInfo>
    suspend fun dislikePost(token: String,postId:String):Resource<UserInfo>

    suspend fun findUsers(token:String, searchKeyWord:String):Resource<List<UserInfo>>

    suspend fun followUser(token: String,email: String):Resource<UserInfo>
    suspend fun unFollowUser(token: String,email: String):Resource<UserInfo>


    suspend fun addComment(token: String, postId: String, commentRequest: CommentRequest):Resource<Comment>
    suspend fun likeComment(token: String,postId: String,commentId:String):Resource<UserInfo>
    suspend fun dislikeComment(token: String,postId: String,commentId:String):Resource<UserInfo>

    suspend fun getCurrentMonthReward(token: String):Resource<Reward>
    suspend fun getMyRewards(token: String):Resource<List<Reward>>
    suspend fun getLastYearReward(token:String):Resource<Reward>

    suspend fun getMemesFromGithubApi():Resource<MemeApiGithub>
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
     * todo -> 8. Make default fn calls in EFFECTS
     * todo -> 9. Landing page
     * todo -> 10. Logo
     * light Mode -> primary colour
     * todo -> user experience
     * todo -> comment box background not transparent
     * todo -> documentation, repository public
     *
     */


}