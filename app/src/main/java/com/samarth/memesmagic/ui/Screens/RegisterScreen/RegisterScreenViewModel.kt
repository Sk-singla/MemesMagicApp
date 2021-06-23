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

    val registerStatus = mutableStateOf<Resource<String>>(Resource.Empty())


    fun clearAllTextFields(){
        name.value = ""
        email.value = ""
        password.value = ""
        confirmPassword.value = ""
    }

    fun registerUser() = viewModelScope.launch{
        registerStatus.value = Resource.Loading()
        if(isPasswordSameAsConfirmPassword()) {
            val registerRequest = RegisterUserRequest(name.value, email.value, password.value)
            registerStatus.value = memeRepo.registerUser(registerRequest)
        } else {
            registerStatus.value = Resource.Error("Password don't match with Confirm Password")
        }
        delay(1500)
        registerStatus.value = Resource.Empty()
    }

    private fun isPasswordSameAsConfirmPassword():Boolean{
        return password.value == confirmPassword.value
    }




}