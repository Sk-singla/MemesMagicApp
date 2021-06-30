package com.samarth.memesmagic.ui.Screens.home.feed

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.samarth.memesmagic.BuildConfig
import com.samarth.memesmagic.data.remote.models.PostType
import com.samarth.memesmagic.data.remote.response.Post
import com.samarth.memesmagic.data.remote.response.Reward
import com.samarth.memesmagic.data.remote.response.User
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.TokenHandler.getEmail
import com.samarth.memesmagic.util.TokenHandler.getJwtToken
import com.samarth.memesmagic.util.TokenHandler.getRewardId
import com.samarth.memesmagic.util.TokenHandler.saveRewardId
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

    val posts = mutableListOf<Post>(

        Post(
            "id1",
            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
            PostType.IMAGE,
            System.currentTimeMillis(),
            description = "This is first Image",
            mediaLink = "https://www.nasa.gov/sites/default/files/styles/full_width_feature/public/thumbnails/image/latest_1024_0211.jpg"
        ),
        Post(
            "id2",
            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
            PostType.IMAGE,
            System.currentTimeMillis(),
            description = "This is first Image",
            mediaLink = "https://miro.medium.com/max/4090/1*lRu8OA8Kjc6luPKMQajYmQ.jpeg"
        ),
        Post(
            "id2",
            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
            PostType.IMAGE,
            System.currentTimeMillis(),
            description = "This is first Image",
            mediaLink = "https://www.nasa.gov/sites/default/files/styles/full_width_feature/public/thumbnails/image/latest_1024_0211.jpg"
        ),
        Post(
            "id2",
            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
            PostType.IMAGE,
            System.currentTimeMillis(),
            description = "This is first Image",
            mediaLink = "https://miro.medium.com/max/4090/1*lRu8OA8Kjc6luPKMQajYmQ.jpeg"
        ),
        Post(
            "id2",
            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
            PostType.IMAGE,
            System.currentTimeMillis(),
            description = "This is first Image",
            likedBy = mutableListOf(UserInfo("adfs","test","")),
            mediaLink = "https://helpx.adobe.com/content/dam/help/en/photoshop/using/convert-color-image-black-white/jcr_content/main-pars/before_and_after/image-before/Landscape-Color.jpg"
        )



    )

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
            val token = getJwtToken(getApplication<Application>().applicationContext)!!
//            Log.d("Token",token)
            getFeed(token, getEmail(getApplication<Application>().applicationContext)!!)
        }
    }

    fun getReward(context: Context,newReward:(reward:Reward,isItForMe:Boolean)->Unit) = viewModelScope.launch{

        val prevReward = getRewardId(context)
        val getMemerReward = memeRepo.getCurrentMonthReward(getJwtToken(context)!!)

        if(getMemerReward is Resource.Success){
            if(prevReward == null || prevReward != getMemerReward.data!!.id){
                newReward(getMemerReward.data!!, getEmail(context)!! == getMemerReward.data.userEmail)
                getUser(context,getMemerReward.data.userEmail){
                    rewardWinner.value = it.userInfo
                }
                isFollowingToRewardy(context)
                saveRewardId(context,getMemerReward.data.id)
            }
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