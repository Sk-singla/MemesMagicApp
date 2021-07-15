package com.samarth.memesmagic.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.samarth.memesmagic.ui.screens.another_user_profile.AnotherUserProfile
import com.samarth.memesmagic.ui.screens.comments.CommentScreen
import com.samarth.memesmagic.ui.screens.login_screen.LoginScreen
import com.samarth.memesmagic.ui.screens.register_screen.RegisterScreen
import com.samarth.memesmagic.ui.screens.home.profile.SinglePostScreen
import com.samarth.memesmagic.ui.screens.edit_profile.EditProfileScreen
import com.samarth.memesmagic.ui.screens.home.HomeScreen
import com.samarth.memesmagic.ui.screens.home.create.CreateViewModel
import com.samarth.memesmagic.ui.screens.home.create.EditingScreen
import com.samarth.memesmagic.ui.screens.home.create.NewPostDetailsScreen
import com.samarth.memesmagic.ui.screens.home.create.TemplateSelectionScreen
import com.samarth.memesmagic.ui.screens.home.feed.FeedViewModel
import com.samarth.memesmagic.ui.screens.landing_page.LandingPage
import com.samarth.memesmagic.ui.screens.landing_page.SplashScreen
import com.samarth.memesmagic.util.Screens
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
import com.samarth.memesmagic.util.Screens.SPLASH_SCREEN

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun MainNavGraph(
    startActivity:(Intent)->Unit,
    startActivityForResult:(String,(Uri?)->Unit)->Unit,
    updateOrRequestPermissions:()->Boolean,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
){
    val feedViewModel:FeedViewModel = hiltViewModel()
    val createViewModel:CreateViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = SPLASH_SCREEN,modifier =modifier){


        composable(SPLASH_SCREEN){
            SplashScreen(navController = navController)
        }

        composable(LANDING_SCREEN){
            LandingPage(navController = navController)
        }

        composable(REGISTER_SCREEN){
            RegisterScreen(navController = navController)
        }

        composable(HOME_SCREEN){
            HomeScreen(navController = navController,startActivity = startActivity,feedViewModel = feedViewModel)
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
                    createViewModel = createViewModel,
                    updateOrRequestPermissions = updateOrRequestPermissions
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
                AnotherUserProfile(userEmail = userEmail,navController = navController)
            }
        }

        composable(EDIT_PROFILE_SCREEN){
            EditProfileScreen(
                navController,
                startActivityForResult,
                updateOrRequestPermissions = updateOrRequestPermissions
            )
        }

        composable(Screens.SINGLE_POST_SCREEN){
            SinglePostScreen(
                parentNavController = navController,
                startActivity = startActivity,
                feedViewModel = feedViewModel
            )
        }




    }

}