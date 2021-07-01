package com.samarth.memesmagic.ui.Screens.landing_page

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.samarth.memesmagic.R
import com.samarth.memesmagic.ui.components.CustomButton
import com.samarth.memesmagic.ui.components.PartiallyClickableText
import com.samarth.memesmagic.util.Constants
import com.samarth.memesmagic.util.Screens.HOME_SCREEN
import com.samarth.memesmagic.util.Screens.LOGIN_SCREEN
import com.samarth.memesmagic.util.Screens.REGISTER_SCREEN
import com.samarth.memesmagic.util.TokenHandler.getJwtToken
import com.samarth.memesmagic.util.navigateWithPop
import kotlinx.coroutines.launch

@Composable
fun LandingPage(navController: NavController) {


    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // App Name
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colors.onBackground)) {
                        append("Meme's")
                    }
                    withStyle(style = SpanStyle(color = MaterialTheme.colors.secondary)) {
                        append(" Magic")
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(24.dp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 34.sp
            )

            // Rest Illustration
            Image(
                painter = painterResource(id = R.drawable.rest_illustration),
                contentDescription = "Illustration",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )




            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            )





            SignUp_Login_Buttons(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onSignUpWithGoogle = {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("Feature is under Development!")
                    }
                },
                onSignUpWithEmail = {
                    navController.navigate(REGISTER_SCREEN)
                },
                onLogin = {
                    navController.navigate(LOGIN_SCREEN)
                }
            )

        }


    }


}

@Composable
fun SignUp_Login_Buttons(
    modifier: Modifier = Modifier,
    onSignUpWithGoogle:()->Unit,
    onSignUpWithEmail:()->Unit,
    onLogin:()->Unit
) {

    Column(
        modifier = modifier
    ) {

        // Sign up with Google
        CustomButton(
            text = "Sign up with Google",
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            icon = R.drawable.ic_google,
            backgroundColor = MaterialTheme.colors.secondaryVariant,
            textColor = MaterialTheme.colors.onSecondary
        ) {
            onSignUpWithGoogle()
        }

        // Sign up with Email
        CustomButton(
            text = "Sign up with Email",
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            onSignUpWithEmail()
        }

        // Login
        PartiallyClickableText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            nonClickableText = "Already have an Account?",
            clickableText = " Login here"
        ) {
            onLogin()
        }
    }

}
