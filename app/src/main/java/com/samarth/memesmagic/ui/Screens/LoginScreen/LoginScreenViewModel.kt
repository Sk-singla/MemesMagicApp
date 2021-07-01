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
import com.samarth.memesmagic.util.isItEmail
import com.samarth.memesmagic.util.validatePassword
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
    val isLoading = mutableStateOf(false)


    fun loginUser(
        onSuccess:(token:String)->Unit,
        onFail:(String?)->Unit
    ) = viewModelScope.launch{
        isLoading.value = true

        if(!isItEmail(email.value)){
            onFail("Please Enter Proper Email Id!")
            return@launch
        }
        if(!validatePassword(password.value)){
            onFail("Password Length must be greater than 6!")
            return@launch
        }

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