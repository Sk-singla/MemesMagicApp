package com.samarth.memesmagic.util

import androidx.navigation.NavController

object Constants {
    const val PARTIALLY_CLICKABLE_TEXT_TAG = "Click"

    const val BASE_URL = "https://memesmagic.herokuapp.com"
    private const val API_VERSION = "/v1"
    const val USERS = "$API_VERSION/user"
    const val POSTS = "$API_VERSION/posts"

    const val JWT_TOKEN_KEY = "JwtToken"
    const val DATA_PREFERENCES_NAME_FOR_TOKEN = "tokens"



    fun navigateWithPop(navController: NavController,destination:String){
        navController.popBackStack()
        navController.navigate(destination)
    }










}

object Screens{

    const val LANDING_SCREEN = "landing_page"
    const val REGISTER_SCREEN = "register_screen"
    const val LOGIN_SCREEN = "login_screen"
    const val HOME_SCREEN = "home_screen"

}