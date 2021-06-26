package com.samarth.memesmagic.util

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