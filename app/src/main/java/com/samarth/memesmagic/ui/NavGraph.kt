package com.samarth.memesmagic.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.samarth.memesmagic.ui.Screens.LoginScreen.LoginScreen
import com.samarth.memesmagic.ui.Screens.RegisterScreen.RegisterScreen
import com.samarth.memesmagic.ui.Screens.home.HomeScreen
import com.samarth.memesmagic.ui.Screens.home.create.CreateScreen
import com.samarth.memesmagic.ui.Screens.landing_page.LandingPage
import com.samarth.memesmagic.util.Screens
import com.samarth.memesmagic.util.Screens.HOME_CREATE
import com.samarth.memesmagic.util.Screens.HOME_SCREEN
import com.samarth.memesmagic.util.Screens.LANDING_SCREEN
import com.samarth.memesmagic.util.Screens.LOGIN_SCREEN
import com.samarth.memesmagic.util.Screens.REGISTER_SCREEN

@ExperimentalFoundationApi
@Composable
fun MainNavGraph(){

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = LANDING_SCREEN){


        composable(LANDING_SCREEN){
            LandingPage(navController = navController)
        }

        composable(REGISTER_SCREEN){
            RegisterScreen(navController = navController)
        }

        composable(HOME_SCREEN){
            HomeScreen(navController = navController)
        }

        composable(LOGIN_SCREEN){
            LoginScreen(navController = navController)
        }


        composable(HOME_CREATE){
            CreateScreen()
        }


    }

}