package com.samarth.memesmagic.ui.screens.login_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.doodlekong.util.DispatcherProvider
import com.samarth.memesmagic.data.remote.request.LoginRequest
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.Screens.MAX_PASSWORD_LENGTH
import com.samarth.memesmagic.util.Screens.MIN_PASSWORD_LENGTH
import com.samarth.memesmagic.util.isItEmail
import com.samarth.memesmagic.util.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    val memeRepo: MemeRepo,
    val dispatchers: DispatcherProvider
):ViewModel() {

    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val isLoading = mutableStateOf(false)


    fun loginUser(
        onSuccess:(token:String)->Unit,
        onFail:(String?)->Unit
    ) = viewModelScope.launch(dispatchers.io){

        email.value = email.value.trim()
        password.value = password.value.trim()

        if(!isItEmail(email.value)){
            onFail("Please Enter Proper Email Id!")
            return@launch
        }
        if(!validatePassword(password.value)){
            onFail("Password Length must be greater than $MIN_PASSWORD_LENGTH and less than $MAX_PASSWORD_LENGTH!")
            return@launch
        }

        isLoading.value = true

        val result = memeRepo.loginUser(LoginRequest(email.value, password.value))
        if(result is Resource.Success){
            onSuccess(result.data!!)
        } else {
            onFail(result.message)
        }

        isLoading.value = false
    }

    fun clearAllTextFields(){
        email.value = ""
        password.value = ""
    }
}