package com.samarth.memesmagic.ui.screens.rewards

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samarth.memesmagic.data.remote.response.Reward
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.TokenHandler.getEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RewardsViewModel @Inject constructor(
    val memeRepo: MemeRepo
) : ViewModel(){

    val isLoading = mutableStateOf(false)
    val rewards = mutableStateOf(listOf<Reward>())
    val loadError = mutableStateOf("")

    fun getRewards(
        context: Context,
        userEmail:String?
    ) = viewModelScope.launch{
        isLoading.value = true
        val result = memeRepo.getRewards( userEmail ?: getEmail(context)!!)
        if(result is Resource.Success){
            rewards.value = result.data!!
            loadError.value = ""
        } else{
            loadError.value = result.message!!
        }
        isLoading.value = false
    }




}