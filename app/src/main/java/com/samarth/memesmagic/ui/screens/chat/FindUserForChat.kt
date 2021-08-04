package com.samarth.memesmagic.ui.screens.chat

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import com.samarth.memesmagic.ui.screens.home.search.SearchScreen

@ExperimentalComposeUiApi
@Composable
fun FindUserForChat(
    navController: NavHostController
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState
    ) {
        SearchScreen(
            scaffoldState = scaffoldState,
            parentNavController = navController
        )
    }

}