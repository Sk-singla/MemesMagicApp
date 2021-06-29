package com.samarth.memesmagic.ui.Screens.home

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.samarth.memesmagic.ui.components.CustomBottomNavBar
import com.samarth.memesmagic.ui.components.CustomTopBar

@ExperimentalFoundationApi
@Composable
fun HomeScreen(navController: NavHostController,homeViewModel: HomeViewModel = hiltViewModel(),startActivity:(Intent)->Unit) {


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
                }
            )
        },

        bottomBar = {

            CustomBottomNavBar(navController = homeNavController,tabs = tabs,parentNavController = navController)

        }
    ) {

        HomeNavGraph(modifier = Modifier.fillMaxSize(), homeNavController,scaffoldState,startActivity = startActivity)

    }
    
}