package com.samarth.memesmagic.ui.screens.register_screen


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samarth.memesmagic.data.remote.request.RegisterUserRequest
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.isItEmail
import com.samarth.memesmagic.util.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterScreenViewModel @Inject constructor(
    val memeRepo: MemeRepo
):ViewModel() {



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


        if(!isItEmail(email.value)){
            onFail("Please Enter Proper Email Id!")
            return@launch
        }
        if(!validatePassword(password.value)){
            onFail("Password Length must be greater than 6!")
            return@launch
        }
        isLoading.value = true
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