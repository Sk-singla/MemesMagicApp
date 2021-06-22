package com.samarth.memesmagic.ui.Screens.LoginScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(

):ViewModel() {

    val email = mutableStateOf("")
    val password = mutableStateOf("")

    fun clearAllTextFields(){
        email.value = ""
        password.value = ""
    }


}