package com.samarth.memesmagic.ui.screens.another_user_profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.doodlekong.util.DispatcherProvider
import com.samarth.memesmagic.data.remote.response.Post
import com.samarth.memesmagic.data.remote.response.Reward
import com.samarth.memesmagic.data.remote.response.User
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnotherUserProfileViewModel @Inject constructor(
    val memeRepo: MemeRepo,
    val dispatcher: DispatcherProvider
):ViewModel() {


    val user = mutableStateOf<User?>(null)
    val curUser = mutableStateOf<User?>(null)
    val posts = mutableStateOf<List<Post>>(listOf())
    val isLoading = mutableStateOf(false)
    val loadError = mutableStateOf("")
    val isFollowing = mutableStateOf(false)
    val rewards = mutableStateOf(listOf<Reward>())
    val isLoadingRewards = mutableStateOf(false)

    fun getUser(
        email:String,
        onSuccess: (user:User) -> Unit
    ) = viewModelScope.launch(dispatcher.io){
        isLoading.value = true
        val result = memeRepo.getUser(email)
        if (result is Resource.Success) {
            loadError.value = ""
            onSuccess(result.data!!)
        } else {
            loadError.value = result.message ?: "Some Problem Occurred!!"
        }
        isLoading.value = false
    }


    fun isFollowing(curUserEmail:String) = viewModelScope.launch(dispatcher.io){
        user.value?.let { otherUser ->
            isFollowing.value = otherUser.followers.map { it.email }.contains(curUserEmail)
        }
    }


    fun getPosts(email:String) = viewModelScope.launch(dispatcher.io) {
        isLoading.value = true
        val result = memeRepo.getPosts(email)
        if (result is Resource.Success) {
            posts.value = result.data!!
            loadError.value = ""
        } else {
            loadError.value = result.message ?: "Some Problem Occurred!!"
        }
        isLoading.value = false
    }


    fun followUser(
        emailOfUserToFollow:String,
        onSuccess: (UserInfo) -> Unit,
        onFail: (String) -> Unit
    ) = viewModelScope.launch {
        val result = memeRepo.followUser(emailOfUserToFollow)
        if(result is Resource.Success){
            onSuccess(result.data!!)
        } else {
            onFail(result.message ?: "Error!!")
        }
    }

    fun unFollowUser(
        emailOfUserToUnFollow:String,
        onSuccess: () -> Unit,
        onFail: (String) -> Unit
    ) = viewModelScope.launch {
        val result = memeRepo.unFollowUser(emailOfUserToUnFollow)
        if (result is Resource.Success) {
            onSuccess()
        } else {
            onFail(result.message ?: "Error!!")
        }
    }

    fun followUnfollowToggle(curUserEmail: String, onSuccess:()->Unit) = viewModelScope.launch(dispatcher.io){
        val result = if(isFollowing.value){
            memeRepo.unFollowUser(
                user.value!!.userInfo.email
            )
        } else {
            memeRepo.followUser(
                user.value!!.userInfo.email
            )
        }
        if(result is Resource.Success){
            if(isFollowing.value){
                user.value?.followers?.remove(user.value?.followers?.find { it.email == curUserEmail })
            } else {
                user.value?.followers?.add(UserInfo("", curUserEmail))
                isFollowing.value = true
            }
            onSuccess()
        }
    }


    fun getRewards() = viewModelScope.launch(dispatcher.io){
        isLoadingRewards.value = true
        val result = memeRepo.getRewards(user.value!!.userInfo.email)
        if(result is Resource.Success){
            rewards.value = result.data!!
            loadError.value = ""
        } else {
            loadError.value = result.message!!
        }
        isLoadingRewards.value = false
    }







}