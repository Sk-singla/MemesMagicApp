package com.samarth.memesmagic.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.samarth.memesmagic.ui.Screens.RegisterScreen.RegisterScreen
import com.samarth.memesmagic.ui.Screens.landing_page.LandingPage

@Composable
fun MainNavGraph(){

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "landing_page"){


        composable("landing_page"){
            LandingPage(navController = navController)
        }

        composable("register_screen"){
            RegisterScreen(navController = navController)
        }

    }

}