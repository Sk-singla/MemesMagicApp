package com.samarth.memesmagic.ui.screens.home

import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.samarth.memesmagic.ui.screens.home.feed.FeedViewModel
import com.samarth.memesmagic.ui.components.CustomBottomNavBar
import com.samarth.memesmagic.ui.components.CustomTopBar
import com.samarth.memesmagic.R
import com.samarth.memesmagic.util.Screens

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun HomeScreen(
    navController: NavHostController,
    startActivity: (Intent) -> Unit,
    feedViewModel:FeedViewModel
) {


    val homeNavController = rememberNavController()
    val tabs = remember{ HomeSections.values() }
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {

            CustomTopBar(
                title = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colors.onBackground)) {
                        append("Meme's")
                    }
                    withStyle(style = SpanStyle(color = MaterialTheme.colors.secondary)) {
                        append(" Magic")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screens.CHAT_ROOMS_LIST_SCREEN)
                    }) {
                        Icon(painter = painterResource(id = R.drawable.ic_chat), contentDescription = "Chat")
                    }
                }
            )
        },

        bottomBar = {
            CustomBottomNavBar(navController = homeNavController,tabs = tabs,parentNavController = navController)
        }
    ) {

        HomeNavGraph(
            modifier = Modifier.fillMaxSize(),
            homeNavController,
            navController,
            scaffoldState,
            startActivity = startActivity,
            feedViewModel = feedViewModel
        )

    }
    
}