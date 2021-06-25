package com.samarth.memesmagic.ui.Screens.home.feed

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.samarth.memesmagic.data.remote.models.PostType
import com.samarth.memesmagic.data.remote.response.Post
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.TokenHandler.getEmail
import com.samarth.memesmagic.util.TokenHandler.getJwtToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    app:Application,
    val memeRepo: MemeRepo
):AndroidViewModel(app) {


    val postStatus = mutableStateOf<Resource<List<Post>>>(Resource.Empty())
    val posts = mutableListOf(
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
            Log.d("Token",token)
            getFeed(token)
        }
    }

    fun getFeed(token:String) = viewModelScope.launch{
        postStatus.value = Resource.Loading()
//        postStatus.value = memeRepo.getFeed(token)

        if(postStatus.value is Resource.Success){
            posts.addAll(postStatus.value.data!!)
        }

        delay(1500)
        postStatus.value = Resource.Empty()
    }


}