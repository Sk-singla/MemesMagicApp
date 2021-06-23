package com.samarth.memesmagic.ui.Screens.LoginScreen

import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samarth.data.models.request.LoginRequest
import com.samarth.data.models.request.RegisterUserRequest
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    val memeRepo: MemeRepo
):ViewModel() {

    val email = mutableStateOf("")
    val password = mutableStateOf("")

    val loginStatus = mutableStateOf<Resource<String>>(Resource.Empty<String>())

    fun loginUser() = viewModelScope.launch{
        loginStatus.value = Resource.Loading()
        loginStatus.value = memeRepo.loginUser(LoginRequest(email.value, password.value))

        delay(2000)
        loginStatus.value = Resource.Empty()
    }














    fun clearAllTextFields(){
        email.value = ""
        password.value = ""
    }
}