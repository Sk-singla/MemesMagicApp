package com.samarth.memesmagic.data.remote

import com.samarth.data.models.request.LoginRequest
import com.samarth.data.models.request.PostRequest
import com.samarth.data.models.request.RegisterUserRequest
import com.samarth.memesmagic.data.remote.request.CommentRequest
import com.samarth.memesmagic.data.remote.request.UserInfoRequest
import com.samarth.memesmagic.data.remote.response.*
import com.samarth.memesmagic.util.Constants.COMMENTS
import com.samarth.memesmagic.util.Constants.FEED
import com.samarth.memesmagic.util.Constants.POSTS
import com.samarth.memesmagic.util.Constants.REWARDS
import com.samarth.memesmagic.util.Constants.USERS
import retrofit2.http.*

interface MemeApi {


    @Headers("Content-Type: application/json")
    @POST("$USERS/register")
    suspend fun userRegister(
        @Body registerUserRequest: RegisterUserRequest
    ): SimpleResponse<String>

    @Headers("Content-Type: application/json")
    @POST("$USERS/login")
    suspend fun userLogin(
        @Body loginRequest: LoginRequest
    ): SimpleResponse<String>


    @Headers("Content-Type: application/json")
    @GET(FEED)
    suspend fun getFeed(
        @Header("Authorization") token:String
    ):SimpleResponse<List<Post>>


    @Headers("Content-Type: application/json")
    @GET("$USERS/get/{email}")
    suspend fun getUser(
        @Header("Authorization") token:String,
        @Path("email") email:String
    ):SimpleResponse<User>

    @Headers("Content-Type: application/json")
    @GET("$USERS/search/{searchKeyWord}")
    suspend fun findUsers(
        @Header("Authorization") token:String,
        @Path("searchKeyWord") searchKeyWord:String
    ):SimpleResponse<List<UserInfo>>


    @Headers("Content-Type: application/json")
    @POST("$USERS/follow/{email}")
    suspend fun followUser(
        @Header("Authorization") token:String,
        @Path("email") email: String
    ):SimpleResponse<UserInfo>

    @Headers("Content-Type: application/json")
    @POST("$USERS/unfollow/{email}")
    suspend fun unFollowUser(
        @Header("Authorization") token:String,
        @Path("email") email: String
    ):SimpleResponse<UserInfo>


    @Headers("Content-Type: application/json")
    @GET("$POSTS/get/{email}")
    suspend fun getPosts(
        @Header("Authorization") token:String,
        @Path("email") email: String
    ):SimpleResponse<List<Post>>



    @Headers("Content-Type: application/json")
    @POST("$POSTS/create")
    suspend fun uploadPost(
        @Header("Authorization") token:String,
        @Body postRequest: PostRequest
    ):SimpleResponse<String>

    @Headers("Content-Type: application/json")
    @POST("$POSTS/like/{postId}")
    suspend fun likePost(
        @Header("Authorization") token:String,
        @Path("postId") postId:String
    ):SimpleResponse<UserInfo>

    @Headers("Content-Type: application/json")
    @POST("$POSTS/dislike/{postId}")
    suspend fun dislikePost(
        @Header("Authorization") token:String,
        @Path("postId") postId:String
    ):SimpleResponse<UserInfo>



    @Headers("Content-Type: application/json")
    @POST("$COMMENTS/add/{postId}")
    suspend fun uploadComment(
        @Header("Authorization") token:String,
        @Path("postId") postId: String,
        @Body commentRequest: CommentRequest
    ):SimpleResponse<Comment>

    @Headers("Content-Type: application/json")
    @POST("$COMMENTS/like/{postId}/{commentId}")
    suspend fun likeComment(
        @Header("Authorization") token:String,
        @Path("postId") postId:String,
        @Path("commentId") commentId:String
    ):SimpleResponse<UserInfo>

    @Headers("Content-Type: application/json")
    @POST("$COMMENTS/dislike/{postId}/{commentId}")
    suspend fun dislikeComment(
        @Header("Authorization") token:String,
        @Path("postId") postId:String,
        @Path("commentId") commentId:String
    ):SimpleResponse<UserInfo>


    @Headers("Content-Type: application/json")
    @GET("$REWARDS/get/month")
    suspend fun getCurrentMonthReward(
        @Header("Authorization") token:String,
    ):SimpleResponse<Reward>

    @Headers("Content-Type: application/json")
    @GET("$REWARDS/get/year")
    suspend fun getLastYearReward(
        @Header("Authorization") token:String,
    ):SimpleResponse<Reward>



    @Headers("Content-Type: application/json")
    @GET("$REWARDS/get/user/{email}")
    suspend fun getRewards(
        @Header("Authorization") token:String,
        @Path("email") email: String
    ):SimpleResponse<List<Reward>>


    @Headers("Content-Type: application/json")
    @POST("$USERS/update")
    suspend fun updateUserInfo(
        @Header("Authorization") token:String,
        @Body userInfoRequest: UserInfoRequest
    ):SimpleResponse<UserInfo>

    @Headers("Content-Type: application/json")
    @DELETE("$POSTS/delete/single/{postId}")
    suspend fun deletePost(
        @Header("Authorization") token:String,
        @Path("postId") postId: String
    ):SimpleResponse<String>

    @Headers("Content-Type: application/json")
    @POST("$USERS/fcmToken/{fcmToken}")
    suspend fun updateFcmToken(
        @Header("Authorization") token:String,
        @Path("fcmToken") fcmToken: String
    ):SimpleResponse<String>


















}