package com.samarth.memesmagic.ui.screens.landing_page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavController
import com.samarth.memesmagic.R
import com.samarth.memesmagic.util.Screens
import com.samarth.memesmagic.util.Screens.LANDING_SCREEN
import com.samarth.memesmagic.util.TokenHandler
import com.samarth.memesmagic.util.navigateWithPop

@Composable
fun SplashScreen(navController: NavController,testing:Boolean = false) {

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit,) {
        if(testing) {
            return@LaunchedEffect
        }
        if (TokenHandler.getJwtToken(context) != null) {

            navigateWithPop(navController, Screens.HOME_SCREEN)
        } else {
            navigateWithPop(navController,LANDING_SCREEN)
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Image(
            painter = painterResource(id = R.drawable.memes_magic_logo),
            contentDescription = "Logo"
        )
    }

}