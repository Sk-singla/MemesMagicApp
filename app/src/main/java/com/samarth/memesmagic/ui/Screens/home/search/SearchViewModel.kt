package com.samarth.memesmagic.ui.Screens.home.search

import android.app.Application
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.samarth.memesmagic.data.remote.response.User
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.TokenHandler.getEmail
import com.samarth.memesmagic.util.TokenHandler.getJwtToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    app:Application,
    val memeRepo: MemeRepo
):AndroidViewModel(app) {


    val searchKeyWord = mutableStateOf("")
    val usersList:MutableState<List<UserInfo>> = mutableStateOf(listOf<UserInfo>())

    val isLoading = mutableStateOf(false)
    val loadError = mutableStateOf("")


    var curUser:User? = null
    init {
        viewModelScope.launch {
            val resultUser  = memeRepo.getUser(getJwtToken(getApplication<Application>().applicationContext)!!,
                getEmail(getApplication<Application>().applicationContext)!!)
            if(resultUser is Resource.Success){
                curUser = resultUser.data
            }
        }
    }

    fun searchUser(context: Context) = viewModelScope.launch{
        isLoading.value = true
        val result = memeRepo.findUsers(getJwtToken(context)!!,searchKeyWord.value)
        if(result is Resource.Success){
            usersList.value = result.data ?: emptyList()
            if(usersList.value.isEmpty()){
                loadError.value = "No User Found!!"
            } else {
                loadError.value = ""
            }
        } else {
            loadError.value = result.message ?: "Some Problem Occurred!!"
        }
        isLoading.value = false
    }


    fun isFollowingToUser(userInfo:UserInfo): Boolean {
        return curUser?.followings?.contains(userInfo) ?: false
    }


    fun followUnfollowToggle(userInfo: UserInfo,context: Context,onSuccess:()->Unit) = viewModelScope.launch{
        val isFollowing = isFollowingToUser(userInfo)
        val result = if(isFollowing){
            memeRepo.unFollowUser(
                getJwtToken(context)!!,
                userInfo.email
            )
        } else {
            memeRepo.followUser(
                getJwtToken(context)!!,
                userInfo.email
            )
        }
        if(result is Resource.Success){
            onSuccess()
            if(isFollowing){
                curUser?.followings?.remove(userInfo)
            } else {
                curUser?.followings?.add(userInfo)
            }

        }
    }




























}