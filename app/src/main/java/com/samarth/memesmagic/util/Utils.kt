package com.samarth.memesmagic.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

fun numberOfPosts(postCount:Int):String {
    return "$postCount"
}

fun numberOfFollowersOrFollowings(followers:Int):String {
    return "$followers"
}


fun navigateWithPop(navController: NavController, destination:String){
    navController.popBackStack()
    navController.navigate(destination)
}


inline fun <T> sdk29AndUp(onSdk29:()->T):T?{
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
        onSdk29()
    } else null
}

@SuppressLint("SimpleDateFormat")
fun getDate(time:Long,isMonth:Boolean = true):String{
    val date = Date(time)
    val stf = SimpleDateFormat(if(isMonth) "MMM, yyyy" else "yyyy")
    return stf.format(date)
}

fun ContentResolver.getFileName(uri: Uri):String{
    var name = ""
    val cursor = query(uri,null,null,null,null)
    cursor?.use {
        it.moveToFirst()
        name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }
    return name
}

fun isItEmail(email:String):Boolean{
    val regex =  "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"
    return Pattern.compile(regex).matcher(email).matches()
}

fun validatePassword(password:String):Boolean{
    return password.length >= 6
}




