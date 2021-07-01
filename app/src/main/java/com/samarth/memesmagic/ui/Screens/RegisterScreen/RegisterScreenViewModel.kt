package com.samarth.memesmagic.ui.Screens.RegisterScreen

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.samarth.data.models.request.RegisterUserRequest
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.isItEmail
import com.samarth.memesmagic.util.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import javax.inject.Inject


@HiltViewModel
class RegisterScreenViewModel @Inject constructor(
    app:Application,
    val memeRepo: MemeRepo
):AndroidViewModel(app) {



    val name = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val confirmPassword = mutableStateOf("")


    val isLoading = mutableStateOf(false)

    fun clearAllTextFields(){
        name.value = ""
        email.value = ""
        password.value = ""
        confirmPassword.value = ""
    }

    fun registerUser(onSuccess:(token:String)->Unit,onFail:(String?)->Unit) = viewModelScope.launch{
        isLoading.value = true

        if(!isItEmail(email.value)){
            onFail("Please Enter Proper Email Id!")
            return@launch
        }
        if(!validatePassword(password.value)){
            onFail("Password Length must be greater than 6!")
            return@launch
        }

        if(isPasswordSameAsConfirmPassword()) {
            val registerRequest = RegisterUserRequest(name.value, email.value, password.value)
            val result = memeRepo.registerUser(registerRequest)

            if(result is Resource.Success){
                onSuccess(result.data!!)
            } else {
                onFail(result.message)
            }

        } else {
           onFail("Password don't match with Confirm Password")
        }
        isLoading.value = false
    }

    private fun isPasswordSameAsConfirmPassword():Boolean{
        return password.value == confirmPassword.value
    }




}