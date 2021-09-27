package com.samarth.memesmagic.ui.screens.home.profile

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.doodlekong.util.DispatcherProvider
import com.samarth.memesmagic.data.local.database.MemeDatabase
import com.samarth.memesmagic.data.remote.response.Post
import com.samarth.memesmagic.data.remote.response.User
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.TokenHandler
import com.samarth.memesmagic.util.TokenHandler.getEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val memeRepo: MemeRepo,
    val memeDatabase: MemeDatabase,
    val dispatcher: DispatcherProvider
):ViewModel() {

    val user = mutableStateOf<User?>(null)
    val posts = mutableStateOf<List<Post>>(listOf())
    val isLoading = mutableStateOf(false)
    val loadError = mutableStateOf("")

    fun getUser(context: Context) = viewModelScope.launch {
        isLoading.value = true
        val result = memeRepo.getUser( getEmail(context)!!)
        if (result is Resource.Success) {
            user.value = result.data!!
            loadError.value = ""
        } else {
            loadError.value = result.message ?: "Some Problem Occurred!!"
        }
        isLoading.value = false
    }


    fun getPosts(context: Context) = viewModelScope.launch {
        isLoading.value = true
        val result = memeRepo.getPosts( getEmail(context)!!)
        if (result is Resource.Success) {
            posts.value = result.data!!
            loadError.value = ""
        } else {
            loadError.value = result.message ?: "Some Problem Occurred!!"
        }
        isLoading.value = false
    }


    fun logoutUser(context: Context) = viewModelScope.launch(dispatcher.io){
        TokenHandler.logout(context)
        memeDatabase.clearAllTables()
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


}