package com.samarth.memesmagic.ui.Screens.home.feed

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.samarth.memesmagic.BuildConfig
import com.samarth.memesmagic.data.remote.models.PostResource
import com.samarth.memesmagic.data.remote.models.PostType
import com.samarth.memesmagic.data.remote.response.Post
import com.samarth.memesmagic.data.remote.response.Reward
import com.samarth.memesmagic.data.remote.response.User
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.TokenHandler.getEmail
import com.samarth.memesmagic.util.TokenHandler.getJwtToken
import com.samarth.memesmagic.util.TokenHandler.getMonthRewardId
import com.samarth.memesmagic.util.TokenHandler.getYearRewardId
import com.samarth.memesmagic.util.TokenHandler.saveMonthRewardId
import com.samarth.memesmagic.util.TokenHandler.saveYearRewardId
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class FeedViewModel @Inject constructor(
    app:Application,
    val memeRepo: MemeRepo
):AndroidViewModel(app) {


    val postStatus = mutableStateOf<Resource<List<Post>>>(Resource.Empty())
    val rewardWinner = mutableStateOf<UserInfo?>(null)
    val isFollowingToRewardyy = mutableStateOf(false)

    val posts = mutableListOf<Post>()

    fun isItLastItem(itemNumber:Int):Boolean{
        return itemNumber == posts.size -1
    }

    fun isPostLiked(post:Post,context:Context):Boolean {
        val isLiked = mutableStateOf(false)
        viewModelScope.launch {
            isLiked.value = post.likedBy.map { it.email }.contains(getEmail(context))
        }
        return isLiked.value
    }

    init {
        viewModelScope.launch{
            val token = getJwtToken(getApplication<Application>().applicationContext)
            if(token != null) {
                getFeed(token, getEmail(getApplication<Application>().applicationContext)!!)
                getFeedFromGithub()
            }
        }
    }

    fun getReward(context: Context,newReward:(reward:Reward,isItForMe:Boolean)->Unit) = viewModelScope.launch{

        val prevReward = getMonthRewardId(context)
        val getMemerReward = memeRepo.getCurrentMonthReward(getJwtToken(context)!!)

        if(getMemerReward is Resource.Success){
            if(prevReward == null || prevReward != getMemerReward.data!!.id){
                newReward(getMemerReward.data!!, getEmail(context)!! == getMemerReward.data.userEmail)
                getUser(context,getMemerReward.data.userEmail){
                    rewardWinner.value = it.userInfo
                }
                isFollowingToRewardy(context)
                saveMonthRewardId(context,getMemerReward.data.id)
            }
        }
    }

    fun getYearReward(
        context: Context,
        newReward: (reward: Reward, isItForMe: Boolean) -> Unit,
        onFail: () -> Unit
    ) = viewModelScope.launch{

        val prevReward = getYearRewardId(context)
        val getYearReward = memeRepo.getLastYearReward(getJwtToken(context)!!)

        if(getYearReward is Resource.Success){
            if(prevReward == null || prevReward != getYearReward.data!!.id){
                newReward(getYearReward.data!!, getEmail(context)!! == getYearReward.data.userEmail)
                getUser(context,getYearReward.data.userEmail){
                    rewardWinner.value = it.userInfo
                }
                isFollowingToRewardy(context)
                saveYearRewardId(context,getYearReward.data.id)
            } else {
                onFail()
            }
        } else {
            onFail()
        }
    }

    fun followUser(context: Context) = viewModelScope.launch{
        memeRepo.followUser(
            getJwtToken(context)!!,
            rewardWinner.value!!.email
        )

    }

    private fun isFollowingToRewardy(context: Context) = viewModelScope.launch{
        getUser(context,getEmail(context)!!){
            rewardWinner.value?.email?.let { rewardyEmail ->
                isFollowingToRewardyy.value = it.followings.map{it.email}.contains(rewardyEmail)
            }
        }
    }


    private fun getUser(context: Context,email: String,onSuccess: (user:User) -> Unit) = viewModelScope.launch{
        val result = memeRepo.getUser(getJwtToken(context)!!,email)
        if(result  is Resource.Success){
            onSuccess(result.data!!)
        }
    }

    fun getFeed(token:String,email:String) = viewModelScope.launch{
        postStatus.value = Resource.Loading()
//        postStatus.value = memeRepo.getFeed(token)
        postStatus.value = memeRepo.getPosts(token,email)

        if(postStatus.value is Resource.Success){
            posts.addAll(postStatus.value.data!!)
        }

        delay(1500)
        postStatus.value = Resource.Empty()
    }

    fun getFeedFromGithub() = viewModelScope.launch {
        val result = memeRepo.getMemesFromGithubApi()
        result.data?.memes?.map {
            Post(
                id = it.url,
                createdBy = UserInfo("Meme's Magic Bot","admin@email.com"),
                postType = PostType.IMAGE,
                time = System.currentTimeMillis(),
                mediaLink = it.url,
                postResource = PostResource.GITHUB_API
            )
        }?.let { githubPosts ->
            posts.addAll(githubPosts)
            posts.shuffle()
        }
    }

    fun likePost(post:Post,context: Context,onSuccess:()->Unit) = viewModelScope.launch{
        val result = memeRepo.likePost(getJwtToken(context)!!,post.id)
        if(result is Resource.Success){
            post.likedBy.add(result.data!!)
            onSuccess()
        }
    }

    fun dislikePost(post:Post,context: Context,onSuccess:()->Unit) = viewModelScope.launch{

        val result = memeRepo.dislikePost(getJwtToken(context)!!,post.id)
        if(result is Resource.Success){
            post.likedBy.add(result.data!!)
            onSuccess()
        }
    }


    fun shareImage(url:String,context: Context,startActivity:(Intent)->Unit,onFail:(String)->Unit) {
        val fileName = "${System.currentTimeMillis()}.jpg"
        Picasso.get().load(url).into(
            object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    try{
                        bitmap?.let { bmp ->
                            getApplication<Application>().applicationContext.openFileOutput(fileName, Context.MODE_PRIVATE).use { stream ->
                                if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                                    throw IOException("Couldn't Save Bitmap!")
                                } else{


                                    val imageUri = FileProvider.getUriForFile(
                                        getApplication<Application>().applicationContext,
                                        BuildConfig.APPLICATION_ID + ".provider",
                                        File(getApplication<Application>().applicationContext.filesDir,fileName)
                                    )
                                    val intent = Intent(Intent.ACTION_SEND)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    intent.type = "image/*"
                                    intent.putExtra(Intent.EXTRA_STREAM,imageUri)
                                    startActivity(Intent.createChooser(intent,"Share Image"))
                                }
                            }
                        }
                    }catch (e:Exception){
                        onFail(e.message ?: "Some Problem Occurred!!")
                    }
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    onFail(e?.message ?: "Bitmap creation Failed!!")
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                }
            }
        )
    }





}