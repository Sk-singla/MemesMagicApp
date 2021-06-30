package com.samarth.memesmagic.ui

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.samarth.memesmagic.data.remote.models.PostType
import com.samarth.memesmagic.data.remote.response.Comment
import com.samarth.memesmagic.data.remote.response.Post
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.ui.Screens.AnotherUserProfile
import com.samarth.memesmagic.ui.Screens.comments.CommentScreen
import com.samarth.memesmagic.ui.Screens.LoginScreen.LoginScreen
import com.samarth.memesmagic.ui.Screens.RegisterScreen.RegisterScreen
import com.samarth.memesmagic.ui.Screens.edit_profile.EditProfileScreen
import com.samarth.memesmagic.ui.Screens.home.HomeScreen
import com.samarth.memesmagic.ui.Screens.home.create.CreateViewModel
import com.samarth.memesmagic.ui.Screens.home.create.EditingScreen
import com.samarth.memesmagic.ui.Screens.home.create.NewPostDetailsScreen
import com.samarth.memesmagic.ui.Screens.home.create.TemplateSelectionScreen
import com.samarth.memesmagic.ui.Screens.landing_page.LandingPage
import com.samarth.memesmagic.util.Screens.ANOTHER_USER_PROFILE_SCREEN
import com.samarth.memesmagic.util.Screens.COMMENT_SCREEN
import com.samarth.memesmagic.util.Screens.EDIT_PROFILE_SCREEN
import com.samarth.memesmagic.util.Screens.EDIT_SCREEN
import com.samarth.memesmagic.util.Screens.HOME_CREATE
import com.samarth.memesmagic.util.Screens.HOME_SCREEN
import com.samarth.memesmagic.util.Screens.LANDING_SCREEN
import com.samarth.memesmagic.util.Screens.LOGIN_SCREEN
import com.samarth.memesmagic.util.Screens.NEW_POST_DETAILS_AND_UPLOAD
import com.samarth.memesmagic.util.Screens.REGISTER_SCREEN

@ExperimentalFoundationApi
@Composable
fun MainNavGraph(
    startActivity:(Intent)->Unit
){

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
            HomeScreen(navController = navController,startActivity = startActivity)
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


        composable(COMMENT_SCREEN){
            CommentScreen(navController = navController)
        }

        composable(
            "$ANOTHER_USER_PROFILE_SCREEN/{email}",
            arguments = listOf(
                navArgument("email"){
                    type = NavType.StringType
                }
            )
        ){
            it.arguments?.getString("email")?.let { userEmail ->
                AnotherUserProfile(userEmail = userEmail)
            }
        }

        composable(EDIT_PROFILE_SCREEN){
            EditProfileScreen()
        }




    }

}