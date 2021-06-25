package com.samarth.memesmagic.ui.Screens.home

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
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
class HomeViewModel @Inject constructor(
    app:Application,
    val memeRepo: MemeRepo
):AndroidViewModel(app) {



}