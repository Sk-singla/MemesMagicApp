package com.samarth.memesmagic.repository

import android.content.Context
import com.samarth.data.models.request.LoginRequest
import com.samarth.data.models.request.PostRequest
import com.samarth.data.models.request.RegisterUserRequest
import com.samarth.memesmagic.data.remote.models.MemeTemplate
import com.samarth.memesmagic.data.remote.request.CommentRequest
import com.samarth.memesmagic.data.remote.response.*
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

    suspend fun likePost(token: String,postId:String):Resource<UserInfo>
    suspend fun dislikePost(token: String,postId:String):Resource<UserInfo>

    suspend fun findUsers(token:String, searchKeyWord:String):Resource<List<UserInfo>>

    suspend fun followUser(token: String,email: String):Resource<UserInfo>
    suspend fun unFollowUser(token: String,email: String):Resource<UserInfo>


    suspend fun addComment(token: String, postId: String, commentRequest: CommentRequest):Resource<Comment>
    suspend fun likeComment(token: String,postId: String,commentId:String):Resource<UserInfo>
    suspend fun dislikeComment(token: String,postId: String,commentId:String):Resource<UserInfo>


    /**
     * 1. Share meme on other platforms -> Done
     * 2. send friend request / follow , unfollow
     * 3. comments
     * todo -> 4. add stickers while editing, and make everything working
     * todo -> 5. Rewards
     *
     * todo -> 6. posts  lazy column in profile when click on post item,  single post when screen for feed post
     * todo -> 7. feed algo
     * todo -> 8. Make default fn calls in EFFECTS
     * todo -> 9. Landing page
     * todo -> 10. Logo
     * todo -> 11. light Mode -> primary colour
     *
     */


}