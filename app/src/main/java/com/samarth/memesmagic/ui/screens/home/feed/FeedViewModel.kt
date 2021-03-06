package com.samarth.memesmagic.ui.screens.home.feed

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.plcoding.doodlekong.util.DispatcherProvider
import com.samarth.memesmagic.BuildConfig
import com.samarth.memesmagic.data.local.database.MemeDatabase
import com.samarth.memesmagic.data.remote.models.PostResource
import com.samarth.memesmagic.data.remote.models.PostType
import com.samarth.memesmagic.data.remote.response.Post
import com.samarth.memesmagic.data.remote.response.Reward
import com.samarth.memesmagic.data.remote.response.User
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.ui.exoplayer.ExoplayerStateProps
import com.samarth.memesmagic.ui.exoplayer.lastPlayingPost
import com.samarth.memesmagic.ui.exoplayer.postPlaybackDetails
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.TokenHandler
import com.samarth.memesmagic.util.TokenHandler.getEmail
import com.samarth.memesmagic.util.TokenHandler.getMonthRewardId
import com.samarth.memesmagic.util.TokenHandler.getYearRewardId
import com.samarth.memesmagic.util.TokenHandler.saveFcmToken
import com.samarth.memesmagic.util.TokenHandler.saveMonthRewardId
import com.samarth.memesmagic.util.TokenHandler.saveYearRewardId
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject
import kotlin.math.abs


@HiltViewModel
class FeedViewModel @Inject constructor(
    val memeRepo: MemeRepo,
    val memeDatabase: MemeDatabase,
    val dispatcher: DispatcherProvider,
    val player: SimpleExoPlayer,
    val glide:RequestManager,
    val dataSourceFactory: DefaultDataSourceFactory,
    val trackSelector: DefaultTrackSelector,
):ViewModel() {

    val isLoading = mutableStateOf(true)
    val rewardWinner = mutableStateOf<UserInfo?>(null)
    val isFollowingToRewardyy = mutableStateOf(false)
    val firstTimeOpenedFeedScreen = mutableStateOf(true)
    val posts = mutableStateOf(listOf<Post>())
    val thumbnails = mutableStateMapOf<String,Bitmap>()
    val lazyListState = LazyListState()
    var playWhenReady = false
    val currentlyPlayingItem = mutableStateOf<Post?>(null)



    fun isItLastItem(itemNumber:Int):Boolean{
        return itemNumber == posts.value.size -1
    }

    fun isPostLiked(post:Post,context:Context):Boolean {
        val isLiked = mutableStateOf(false)
        viewModelScope.launch {
            isLiked.value = post.likedBy.map { it.email }.contains(getEmail(context))
        }
        return isLiked.value
    }

    fun updateFcmToken(token: String,context: Context,onSuccess: () -> Unit,onFail: (String) -> Unit) = viewModelScope.launch {
        val oldToken = TokenHandler.getFcmToken(context)
        if(oldToken!= null && oldToken == token){
            return@launch
        }
        val result = memeRepo.updateFcmToken(token)
        if(result is Resource.Success){
            saveFcmToken(context,token)
            onSuccess()
        } else {
            onFail(result.message!!)
        }
    }


    fun getReward(context: Context,newReward:(reward:Reward,isItForMe:Boolean)->Unit) = viewModelScope.launch{

        val prevReward = getMonthRewardId(context)
        val getMemerReward = memeRepo.getCurrentMonthReward()

        if(getMemerReward is Resource.Success){
            if(prevReward == null || prevReward != getMemerReward.data!!.id){
                newReward(getMemerReward.data!!, getEmail(context)!! == getMemerReward.data.userEmail)
                getUser(getMemerReward.data.userEmail){
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
    ) = viewModelScope.launch {

        val prevReward = getYearRewardId(context)
        val getYearReward = memeRepo.getLastYearReward()

        if(getYearReward is Resource.Success){
            if(prevReward == null || prevReward != getYearReward.data!!.id){
                newReward(getYearReward.data!!, getEmail(context)!! == getYearReward.data.userEmail)
                getUser(getYearReward.data.userEmail){
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

    fun followUser() = viewModelScope.launch{
        memeRepo.followUser(
            rewardWinner.value!!.email
        )

    }

    private fun isFollowingToRewardy(context: Context) = viewModelScope.launch{
        getUser(getEmail(context)!!){
            rewardWinner.value?.email?.let { rewardyEmail ->
                isFollowingToRewardyy.value = it.followings.map{it.email}.contains(rewardyEmail)
            }
        }
    }


    fun getUser(email: String,onFail: (String) -> Unit = {},onSuccess: (user:User) -> Unit) = viewModelScope.launch{
        val result = memeRepo.getUser(email)
        if(result  is Resource.Success){
            onSuccess(result.data!!)
        } else {
            onFail(result.message ?: "Error!")
        }
    }



    fun getFeed(
        onSuccess: () -> Unit,
        onFail: (String) -> Unit
    ) = viewModelScope.launch{
        isLoading.value = true
        val result = memeRepo.getFeed()

        if(result is Resource.Success){
            posts.value += result.data!!
            posts.value.filter { it.postType == PostType.VIDEO }.forEach { curPost ->
                generateThumbnail(curPost)
            }
            onSuccess()
        } else {
            onFail(result.message ?: "Some Problem Occurred!")
        }
        isLoading.value = false
    }

    fun generateThumbnail(curPost:Post) = viewModelScope.launch(Dispatchers.IO) {
        glide.asBitmap()
            .load(curPost.mediaLink)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    thumbnails[curPost.id] = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) =Unit
            })
    }

    fun getFeedFromGithub() = viewModelScope.launch {
        isLoading.value = true
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
            posts.value += githubPosts
        }
        isLoading.value = false
    }

    fun likePost(post:Post,onSuccess:()->Unit) = viewModelScope.launch{
        val result = memeRepo.likePost(post.id)
        if(result is Resource.Success){
            post.likedBy.add(result.data!!)
            onSuccess()
        }
    }

    fun dislikePost(post:Post,onSuccess:()->Unit) = viewModelScope.launch{

        val result = memeRepo.dislikePost(post.id)
        if(result is Resource.Success){
            post.likedBy.remove(result.data!!)
            onSuccess()
        }
    }


    fun shareImage(context: Context,url:String,startActivity:(Intent)->Unit,onFail:(String)->Unit) {
        val fileName = "${System.currentTimeMillis()}.jpg"
        Picasso.get().load(url).into(
            object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    try{
                        bitmap?.let { bmp ->
                            context.openFileOutput(fileName, Context.MODE_PRIVATE).use { stream ->
                                if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                                    throw IOException("Couldn't Save Bitmap!")
                                } else{


                                    val imageUri = FileProvider.getUriForFile(
                                        context,
                                        BuildConfig.APPLICATION_ID + ".provider",
                                        File(context.filesDir,fileName)
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


    fun deletePost(post: Post,onSuccess: () -> Unit,onFail: (String) -> Unit) = viewModelScope.launch {
        isLoading.value = true
        val result = memeRepo.deletePost(post)
        if(result is Resource.Success){
            onSuccess()
        } else {
            onFail(result.message ?: "Error!")
        }
        isLoading.value = false
    }


    suspend fun clearLocalData(){
        withContext(dispatcher.io) {
            memeDatabase.clearAllTables()
        }
    }

    fun determineCurrentlyPlayingItem(
        lazyListSate: LazyListState,
        posts: List<Post>
    ) {
        val layoutInfo = lazyListSate.layoutInfo
        val visiblePosts = layoutInfo.visibleItemsInfo.map { posts[it.index] }
        val videoPosts = visiblePosts.filter { it.postType == PostType.VIDEO }


        Log.d("video size","Video size: ${videoPosts.size}")
        if(videoPosts.size == 1){
            currentlyPlayingItem.value = videoPosts.first()
        } else {
            val midPoint = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) /2
            val itemsFromCenter =
                layoutInfo.visibleItemsInfo.sortedBy {
                    abs((it.offset + it.size/2) - midPoint)
                }
            currentlyPlayingItem.value = itemsFromCenter.map { posts[it.index] }.firstOrNull{
                it.postType == PostType.VIDEO
            }

        }


    }





}