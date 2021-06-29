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
import com.samarth.memesmagic.ui.Screens.comments.CommentScreen
import com.samarth.memesmagic.ui.Screens.LoginScreen.LoginScreen
import com.samarth.memesmagic.ui.Screens.RegisterScreen.RegisterScreen
import com.samarth.memesmagic.ui.Screens.home.HomeScreen
import com.samarth.memesmagic.ui.Screens.home.create.CreateViewModel
import com.samarth.memesmagic.ui.Screens.home.create.EditingScreen
import com.samarth.memesmagic.ui.Screens.home.create.NewPostDetailsScreen
import com.samarth.memesmagic.ui.Screens.home.create.TemplateSelectionScreen
import com.samarth.memesmagic.ui.Screens.landing_page.LandingPage
import com.samarth.memesmagic.util.Screens.COMMENT_SCREEN
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

            /*
            CommentScreen(
                navController = navController,
                post =  Post(
                    "id1",
                    UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                    PostType.IMAGE,
                    System.currentTimeMillis(),
                    description = "I have a vertical sliding drawer at the bottom of my app. When the soft keyboard opens, it pushes the tab for the drawer up, so it sits atop the keyboard. I actually want it to remain at the bottom of the screen, becoming hidden when the keyboard is shown.\n" +
                            "\n" +
                            "Anyone else run into this issue? Know how to fix it? 91\n" +
                            "\n" +
                            "In my case, the reason the buttons got pushed up was because the view above them was a ScrollView, and it got collapsed with the buttons pushed up above the keyboard no matter what value of android:windowSoftInputMode I was setting.\n" +
                            "\n" +
                            "I was able to avoid my bottom row of buttons getting pushed up by the soft keyboard by setting android:isScrollContainer=\"false\" on the ScrollView that sits above the buttons." +
                            "" +
                            "Alright, I've been dealing with this for hours now and I just can't find a solution.\n" +
                            "\n" +
                            "I have an app with login screen. I want the login button and EditTexts to be pushed up when keyboard is shown, but none of thoe normal methods work.\n" +
                            "\n" +
                            "I've tried both \"adjustPan\" and \"adjustResize\" in Manifest and also programatically. Nothing works. When keyboard is shown, the layout stays the same.\n" +
                            "\n" +
                            "Any idea why? This feature always used to work great for me. Also, maybe this is related. I can't recall ever disabling the keyboard and it never shows automatically, I always need to click an EditText. Didn't keyboard usually always show automatically?",
                    mediaLink = "https://www.nasa.gov/sites/default/files/styles/full_width_feature/public/thumbnails/image/latest_1024_0211.jpg",
                    comments = mutableListOf(
                        Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),
                        Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),
                        Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),
                        Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),
                        Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),
                        Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),
                        Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),
                        Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),
                        Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),
                        Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),
                        Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),
                        Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),
                        Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),
                        Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),
                        Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),
                        Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),
                        Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),
                        Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),
                        Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),
                        Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),
                        Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),
                        Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),
                        Comment(
                            UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                            "Nice One",
                            1234234,
                            likedBy = emptyList()
                        ),



                    ),
                    likedBy = mutableListOf(
                        UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                        UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                        UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                        UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                        UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                        UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),
                        UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),UserInfo("Samarth","asasdf@gmail.com","https://cdn.pixabay.com/photo/2018/08/28/12/41/avatar-3637425_960_720.png"),


                        )
                )
            )

             */
        }




    }

}