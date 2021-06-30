package com.samarth.memesmagic.ui.Screens.another_user_profile

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samarth.memesmagic.data.remote.response.Post
import com.samarth.memesmagic.data.remote.response.User
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.TokenHandler
import com.samarth.memesmagic.util.TokenHandler.getEmail
import com.samarth.memesmagic.util.TokenHandler.getJwtToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnotherUserProfileViewModel @Inject constructor(
    val memeRepo: MemeRepo
):ViewModel() {


    val user = mutableStateOf<User?>(null)
    val posts = mutableStateOf<List<Post>>(listOf())
    val isLoading = mutableStateOf(false)
    val loadError = mutableStateOf("")
    val isFollowing = mutableStateOf(false)

    fun getUser(context: Context,email:String) = viewModelScope.launch {
        isLoading.value = true
        val result = memeRepo.getUser(getJwtToken(context)!!, email)
        if (result is Resource.Success) {
            user.value = result.data!!
            loadError.value = ""
            isFollowing(context)
        } else {
            loadError.value = result.message ?: "Some Problem Occurred!!"
        }
        isLoading.value = false
    }


    private fun isFollowing(context: Context) = viewModelScope.launch{
        user.value?.let { otherUser ->
            isFollowing.value = otherUser.followers.map { it.email }.contains(getEmail(context)!!)
        }
    }


    fun getPosts(context: Context,email:String) = viewModelScope.launch {
        isLoading.value = true
        val result = memeRepo.getPosts(getJwtToken(context)!!,email)
        if (result is Resource.Success) {
            posts.value = result.data!!
            loadError.value = ""
        } else {
            loadError.value = result.message ?: "Some Problem Occurred!!"
        }
        isLoading.value = false
    }




    fun followUnfollowToggle(context: Context, onSuccess:()->Unit) = viewModelScope.launch{
        val result = if(isFollowing.value){
            memeRepo.unFollowUser(
                getJwtToken(context)!!,
                user.value!!.userInfo.email
            )
        } else {
            memeRepo.followUser(
                getJwtToken(context)!!,
                user.value!!.userInfo.email
            )
        }
        if(result is Resource.Success){
            onSuccess()
            if(isFollowing.value){
                user.value?.followers?.remove(user.value?.followers?.find { it.email == getEmail(context)!! })
            } else {
                user.value?.followers?.add(UserInfo("", getEmail(context)!!))
            }
        }
    }








}