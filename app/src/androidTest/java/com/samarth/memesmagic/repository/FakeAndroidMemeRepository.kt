package com.samarth.memesmagic.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.samarth.memesmagic.data.remote.models.MemeBadgeType
import com.samarth.memesmagic.data.remote.models.MemeTemplate
import com.samarth.memesmagic.data.remote.request.*
import com.samarth.memesmagic.data.remote.response.*
import com.samarth.memesmagic.data.remote.response.meme_api_github.Meme
import com.samarth.memesmagic.data.remote.response.meme_api_github.MemeApiGithub
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.utils.FAKE_EMAIL
import com.samarth.memesmagic.utils.FAKE_NAME
import com.samarth.memesmagic.utils.FAKE_PASSWORD
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import java.io.File
import java.util.*

@ExperimentalCoroutinesApi
class FakeAndroidMemeRepository(): MemeRepo {

    private var curUser: User = User(
        UserInfo(
            FAKE_NAME,
            FAKE_EMAIL
        ),
        FAKE_PASSWORD,
        id = UUID.randomUUID().toString()
    )

//    private val Context.testDataStore: DataStore<Preferences> by preferencesDataStore(name = "tokens")
//    private suspend fun saveEmail(context: Context,email:String) {
//        val emailKey = stringPreferencesKey("emailKey")
//        context.testDataStore.edit { tokens->
//            tokens[emailKey] = email
//        }
//    }
//
//    init {
////        runBlockingTest {
////            saveEmail(context, FAKE_EMAIL)
////        }
//    }


    private val users = mutableListOf<User>(curUser)
    private val posts = mutableListOf<Post>()



    override suspend fun registerUser(userRegisterRequest: RegisterUserRequest): Resource<String> {
        if(users.find { it.userInfo.email == userRegisterRequest.email } != null){
            return Resource.Error("User with this email Id already exists")
        }
        val tempUser = User(
                UserInfo(userRegisterRequest.name,userRegisterRequest.email,userRegisterRequest.profilePic),
                userRegisterRequest.password,
                id = UUID.randomUUID().toString()
            )
        if(
            users.add(tempUser)
        ) {
            curUser = tempUser
            return Resource.Success("User Added")
        }
        return Resource.Error("Error Occurred!")
    }

    override suspend fun loginUser(loginRequest: LoginRequest): Resource<String> {
        val tempUser = users.find {
            it.userInfo.email == loginRequest.email && it.hashPassword == loginRequest.password
        }
        if(
            tempUser != null
        ) {
            curUser = tempUser
            return Resource.Success("User Logged In")
        }
        return Resource.Error("Error Occurred!")
    }

    override suspend fun getFeed(): Resource<List<Post>> {
        return Resource.Success(posts)
    }

    override suspend fun getUser(email: String): Resource<User> {
        val user = users.find { it.userInfo.email == email }
        return if(user != null){
            Resource.Success(user)
        } else {
            Resource.Error("User Not Found")
        }
    }

    override suspend fun updateUserInfo(userInfoRequest: UserInfoRequest): Resource<UserInfo> {
        curUser.userInfo = UserInfo(
            userInfoRequest.name,
            curUser.userInfo.email,
            userInfoRequest.profilePic,
            userInfoRequest.bio
        )
        return Resource.Success(
            curUser.userInfo
        )
    }

    override suspend fun getPosts(email: String): Resource<List<Post>> {
        return Resource.Success(posts.filter { it.createdBy.email == email })
    }

    override suspend fun getMemeTemplates(memeMakerPageNumber: Int): Resource<List<MemeTemplate>> {
        return Resource.Success(
            posts.map {
                MemeTemplate(
                    it.mediaLink,
                    it.id
                )
            }
        )
    }

    override suspend fun uploadFileOnAwsS3(
        fileName: String,
        file: File?,
        onSuccess: (String) -> Unit,
        onFail: (String) -> Unit
    ) = Unit

    override suspend fun uploadPost(postRequest: PostRequest): Resource<String> {
        if(
            posts.add(
                Post(
                    postRequest.mediaLink,
                    curUser.userInfo,
                    postRequest.postType,
                    postRequest.time,
                    mediaLink = postRequest.mediaLink
                )
            )
        ) {
            curUser.postCount += 1
            return Resource.Success("Post Uploaded")
        }
        return Resource.Error("Error")
    }

    override suspend fun likePost(postId: String): Resource<UserInfo> {
        return if(posts.find { it.id == postId }?.likedBy?.add(curUser.userInfo) == true) {
            Resource.Success(curUser.userInfo)
        } else {
            Resource.Error("Can't add user")
        }
    }

    override suspend fun dislikePost(postId: String): Resource<UserInfo> {
        return if(posts.find { it.id == postId }?.likedBy?.remove(curUser.userInfo) == true) {
            Resource.Success(curUser.userInfo)
        } else {
            Resource.Error("Can't remove user")
        }
    }

    override suspend fun findUsers(searchKeyWord: String): Resource<List<UserInfo>> {
        return Resource.Success(
            users.filter { it.userInfo.name.contains(searchKeyWord) }.map { it.userInfo }
        )
    }

    override suspend fun followUser(email: String): Resource<UserInfo> {
        val userToFollow = users.find { it.userInfo.email == email }
        return if(userToFollow?.followers?.add(curUser.userInfo) == true &&
                curUser.followings.add(userToFollow.userInfo)
        ){
            Resource.Success(userToFollow.userInfo)
        } else {
            Resource.Error("Can't follow User")
        }
    }

    override suspend fun unFollowUser(email: String): Resource<UserInfo> {
        val userToUnFollow = users.find { it.userInfo.email == email }
        return if(userToUnFollow?.followers?.remove(curUser.userInfo) == true &&
            curUser.followings.remove(userToUnFollow.userInfo)
        ){
            Resource.Success(userToUnFollow.userInfo)
        } else {
            Resource.Error("Can't follow User")
        }
    }

    override suspend fun addComment(
        postId: String,
        commentRequest: CommentRequest
    ): Resource<Comment> {
        val comment = Comment(
            userInfo = curUser.userInfo,
            text = commentRequest.text,
            time = System.currentTimeMillis(),
            id= commentRequest.id,
            likedBy = mutableListOf()
        )
        return if(
            posts.find {
                it.id == postId
            }?.comments?.add(
                comment
            ) == true
        ){
            Resource.Success(comment)
        } else {
            Resource.Error("Can't add comment")
        }
    }

    override suspend fun likeComment(postId: String, commentId: String): Resource<UserInfo> {
        return if(
            posts.find {
                it.id == postId
            }?.comments?.find { comment->
                comment.id == commentId
            }?.likedBy?.add(curUser.userInfo) == true
        ) {
            Resource.Success(curUser.userInfo)
        } else {
            Resource.Error("Can't like comment")
        }
    }

    override suspend fun dislikeComment(postId: String, commentId: String): Resource<UserInfo> {
        return if(
            posts.find {
                it.id == postId
            }?.comments?.find { comment->
                comment.id == commentId
            }?.likedBy?.remove(curUser.userInfo) == true
        ) {
            Resource.Success(curUser.userInfo)
        } else {
            Resource.Error("Can't dislike comment")
        }
    }

    override suspend fun getCurrentMonthReward(): Resource<Reward> {
        return Resource.Success(
            Reward(
                MemeBadgeType.MEMER_OF_THE_MONTH,
                System.currentTimeMillis(),
                curUser.userInfo.email,
                UUID.randomUUID().toString()
            )
        )
    }

    override suspend fun getRewards(email: String): Resource<List<Reward>> {
        val user = users.find {
            it.userInfo.email == email
        }
        return if(
            user != null
        ) {
            Resource.Success(
                user.rewards
            )
        } else {
            Resource.Error("Can't find user")
        }
    }

    override suspend fun getLastYearReward(): Resource<Reward> {
        return Resource.Success(
            Reward(
                MemeBadgeType.MEMER_OF_THE_YEAR,
                System.currentTimeMillis(),
                curUser.userInfo.email,
                UUID.randomUUID().toString()
            )
        )
    }

    override suspend fun getMemesFromGithubApi(): Resource<MemeApiGithub> {
        return Resource.Success(
            MemeApiGithub(
                count = posts.size,
                memes = posts.map {
                    Meme(
                        it.createdBy.email,
                        true,
                        it.mediaLink,
                        listOf(),
                        false,
                        "",
                        "",
                        1,
                        it.mediaLink
                    )
                }
            )
        )
    }

    override suspend fun deletePost(postId: String): Resource<String> {
        return if(posts.removeIf{
                it.id == postId
            }
        ) {
            Resource.Success("Done")
        } else {
            Resource.Error("Error")
        }
    }

    override suspend fun updateFcmToken(fcmToken: String): Resource<String> {
        return Resource.Success("Done")
    }
}