package com.samarth.memesmagic.ui.Screens.edit_profile

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.FileUtils
import android.provider.OpenableColumns
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samarth.memesmagic.data.remote.request.UserInfoRequest
import com.samarth.memesmagic.data.remote.response.User
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.util.Constants.BUCKET_OBJECT_URL_PREFIX
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.TokenHandler.getEmail
import com.samarth.memesmagic.util.TokenHandler.getJwtToken
import com.samarth.memesmagic.util.getFileName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Exception
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    val memeRepo: MemeRepo
) : ViewModel(){

    val currentUser = mutableStateOf<User?>(null)

    val userName = mutableStateOf("")
    val bio = mutableStateOf( "")
    val profilePic = mutableStateOf<Uri?>(null)
    val profilePicUrl = mutableStateOf("")

    val isLoading = mutableStateOf(false)

    fun getUser(context: Context) = viewModelScope.launch{

        val email  = getEmail(context)!!
        val result = memeRepo.getUser(getJwtToken(context)!!,email)
        if(result is Resource.Success){
            currentUser.value = result.data!!
            userName.value = result.data.userInfo.name
            bio.value = result.data.userInfo.bio ?: ""
            profilePicUrl.value = result.data.userInfo.profilePic ?: ""
        }
    }

    fun selectProfilePic(startActivityForImage:(String,(Uri?)->Unit) -> Unit){
        startActivityForImage("image/*"){
            Log.d("MyLog","select -> ${it}")
            profilePic.value = it
        }
    }

    fun updateProfile(context: Context,onSuccess: () -> Unit,onFail:(String)->Unit){
        isLoading.value = true
        if(profilePic.value == null){
            updateUserInfo(context,"",onSuccess,onFail)
        } else {


            try {

                val fd = context.contentResolver.openFileDescriptor(profilePic.value!!,"r",null)!!
                val inputStream = FileInputStream(fd.fileDescriptor)
                val file = File(
                    context.cacheDir,
                    context.contentResolver.getFileName(profilePic.value!!)
                )
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)
                uploadImageToAwsAndUpdaterUserInfo(context,file,onSuccess,onFail)

            }catch (e:Exception){
                onFail(e.message ?: "IO exception")
            }
        }
        isLoading.value = false
    }



    private fun uploadImageToAwsAndUpdaterUserInfo(context: Context, imageFile:File, onSuccess: () -> Unit, onFail:(String)->Unit) = viewModelScope.launch {
        isLoading.value = true
        memeRepo.uploadFileOnAwsS3(
            context,
            fileName = imageFile.name,
            file = imageFile,
            onSuccess = {
                updateUserInfo(context,"$BUCKET_OBJECT_URL_PREFIX$it",onSuccess,onFail)
            },
            onFail = {
                onFail(it)
            }
        )
        isLoading.value = false
    }


    private fun updateUserInfo(context: Context, profilePicUrl:String,onSuccess:()->Unit, onFail: (String) -> Unit) = viewModelScope.launch{
        isLoading.value = true
        val userInfoRequest = UserInfoRequest(
            userName.value,
            profilePicUrl,
            bio.value
        )
        val result = memeRepo.updateUserInfo(getJwtToken(context)!!,userInfoRequest)
        if(result is Resource.Success){
            onSuccess()
        } else {
            onFail(result.message ?: "Some Problem Occurred!!")
        }
        isLoading.value = false
    }




}