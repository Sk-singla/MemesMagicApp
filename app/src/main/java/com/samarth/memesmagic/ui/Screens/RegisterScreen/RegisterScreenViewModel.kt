package com.samarth.memesmagic.ui.Screens.RegisterScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class RegisterScreenViewModel @Inject constructor(

):ViewModel() {

    val name = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val confirmPassword = mutableStateOf("")


    fun clearAllTextFields(){
        name.value = ""
        email.value = ""
        password.value = ""
        confirmPassword.value = ""
    }

}