package com.samarth.memesmagic.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.samarth.memesmagic.ui.Screens.LoginScreen.LoginScreen
import com.samarth.memesmagic.ui.Screens.RegisterScreen.RegisterScreen
import com.samarth.memesmagic.ui.Screens.home.HomeScreen
import com.samarth.memesmagic.ui.Screens.home.create.CreateViewModel
import com.samarth.memesmagic.ui.Screens.home.create.EditingScreen
import com.samarth.memesmagic.ui.Screens.home.create.NewPostDetailsScreen
import com.samarth.memesmagic.ui.Screens.home.create.TemplateSelectionScreen
import com.samarth.memesmagic.ui.Screens.landing_page.LandingPage
import com.samarth.memesmagic.util.Screens.EDIT_SCREEN
import com.samarth.memesmagic.util.Screens.HOME_CREATE
import com.samarth.memesmagic.util.Screens.HOME_SCREEN
import com.samarth.memesmagic.util.Screens.LANDING_SCREEN
import com.samarth.memesmagic.util.Screens.LOGIN_SCREEN
import com.samarth.memesmagic.util.Screens.NEW_POST_DETAILS_AND_UPLOAD
import com.samarth.memesmagic.util.Screens.REGISTER_SCREEN

@ExperimentalFoundationApi
@Composable
fun MainNavGraph(){

    val navController = rememberNavController()
    val createViewModel:CreateViewModel = hiltViewModel()

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
            TemplateSelectionScreen(navController = navController,createViewModel = createViewModel)
        }

        composable(
            route = "$EDIT_SCREEN/{memeTemplateIndex}",
            arguments = listOf(
                navArgument("memeTemplateIndex"){
                    type = NavType.IntType
                }
            )
        ){
            it.arguments?.getInt("memeTemplateIndex")?.let { memeTemplateIndex ->
                EditingScreen(
                    navController = navController,
                    memeTemplateIndex = memeTemplateIndex,
                    createViewModel = createViewModel
                )
            }
        }


        composable(NEW_POST_DETAILS_AND_UPLOAD){
            NewPostDetailsScreen(
                navController = navController,
                createViewModel = createViewModel
            )
        }




    }

}