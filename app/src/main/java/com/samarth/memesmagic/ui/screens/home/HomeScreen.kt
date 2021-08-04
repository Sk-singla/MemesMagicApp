package com.samarth.memesmagic.ui.screens.home

import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.samarth.memesmagic.ui.screens.home.feed.FeedViewModel
import com.samarth.memesmagic.ui.components.CustomBottomNavBar
import com.samarth.memesmagic.ui.components.CustomTopBar
import com.samarth.memesmagic.R
import com.samarth.memesmagic.ui.screens.chat.ChatViewModel
import com.samarth.memesmagic.util.Screens
import com.samarth.memesmagic.util.getMessageCount

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun HomeScreen(
    navController: NavHostController,
    startActivity: (Intent) -> Unit,
    feedViewModel:FeedViewModel,
    chatViewModel: ChatViewModel
) {


    val homeNavController = rememberNavController()
    val tabs = remember{ HomeSections.values() }
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true){
        chatViewModel.observeUnseenMessages(context)
    }

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
                    Box(
                        modifier = Modifier
                    ){
                        IconButton(
                            onClick = {
                                    navController.navigate(Screens.CHAT_ROOMS_LIST_SCREEN)
                            },
                            modifier = Modifier.align(Alignment.Center),
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_messenger),
                                contentDescription = "Chat",
                                modifier = Modifier
                                    .fillMaxSize(0.6f)
                                    .align(Alignment.Center)
                            )
                        }

                        if(chatViewModel.unSeenMessagesCount.value > 0) {

                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(
                                        color = MaterialTheme.colors.primary,
                                        shape = CircleShape
                                    )
                                    .align(Alignment.TopEnd),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = getMessageCount(chatViewModel.unSeenMessagesCount.value),
                                    fontSize = 8.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                        }
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