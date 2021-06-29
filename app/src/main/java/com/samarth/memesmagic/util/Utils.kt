package com.samarth.memesmagic.util

import android.os.Build
import androidx.navigation.NavController

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