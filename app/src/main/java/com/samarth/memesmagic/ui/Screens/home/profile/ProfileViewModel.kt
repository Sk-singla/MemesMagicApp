package com.samarth.memesmagic.ui.Screens.home.profile

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samarth.memesmagic.data.remote.models.PostType
import com.samarth.memesmagic.data.remote.response.Post
import com.samarth.memesmagic.data.remote.response.User
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.Screens.posts
import com.samarth.memesmagic.util.TokenHandler.getEmail
import com.samarth.memesmagic.util.TokenHandler.getJwtToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    app:Application,
    val memeRepo: MemeRepo
):AndroidViewModel(app) {

    val userStatus = mutableStateOf<Resource<User>>(Resource.Empty())
    val postStatus = mutableStateOf<Resource<List<Post>>>(Resource.Empty())




    fun getUser(email:String?,context:Context) = viewModelScope.launch{
        userStatus.value = Resource.Loading()
        userStatus.value = memeRepo.getUser(getJwtToken(context)!!,email ?: getEmail(context)!!)

        /*
        userStatus.value = Resource.Success(
            User(
                UserInfo("Samarth","hihi","https://yt3.ggpht.com/yti/APfAmoFnRSyAAd-_w-q3fodH9GHLox265DUgq1UD7c56eA=s88-c-k-c0x00ffffff-no-rj-mo","Sri Muktsar Sahib\nPB30"),
                hashPassword = "asdfsdf",
                postCount = 20,
                followers = mutableListOf(
                    UserInfo("Samarth","hihi",),
                    UserInfo("Samarth","hihi",),
                    UserInfo("Samarth","hihi",)
                ),
                followings = mutableListOf(
                    UserInfo("Samarth","hihi",),
                    UserInfo("Samarth","hihi",),
                    UserInfo("Samarth","hihi",),
                    UserInfo("Samarth","hihi",)
                ),id = "asdfasdf"))

         */


    }


    fun getPosts(email: String?,context: Context) = viewModelScope.launch {
        postStatus.value = Resource.Loading()
        postStatus.value = memeRepo.getPosts(getJwtToken(context)!!,email ?: getEmail(context)!!)

        /**
        postStatus.value = Resource.Success(
            mutableListOf(
                Post(
                    "id1",
                    UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                    PostType.IMAGE,
                    System.currentTimeMillis(),
                    description = "This is first Image",
                    mediaLink = "https://memebucket90630-staging.s3.us-east-2.amazonaws.com/public/a3c5e6e2-5bc1-42fd-b47f-d774e9b20fca.jpg"
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
                ),
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
                ),
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
                ),
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
                ),
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
        )

        postStatus.value  = Resource.Success(
            posts.map {
                Post(
                    "id2",
                    UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                    PostType.IMAGE,
                    System.currentTimeMillis(),
                    description = "This is first Image",
                    likedBy = mutableListOf(UserInfo("adfs","test","")),
                    mediaLink = it

                )
            }
        )
        */


    }



}