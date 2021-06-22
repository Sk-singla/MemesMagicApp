package com.samarth.memesmagic.ui.Screens.RegisterScreen

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class RegisterScreenViewModel @Inject constructor(
    app:Application
):AndroidViewModel(app) {



    val name = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val confirmPassword = mutableStateOf("")

    private lateinit var mGoogleSignInClient:GoogleSignInClient


    fun clearAllTextFields(){
        name.value = ""
        email.value = ""
        password.value = ""
        confirmPassword.value = ""
    }




}