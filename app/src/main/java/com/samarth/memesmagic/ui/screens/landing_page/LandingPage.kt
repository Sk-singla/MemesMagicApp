package com.samarth.memesmagic.ui.screens.landing_page

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.samarth.memesmagic.R
import com.samarth.memesmagic.ui.components.CustomButton
import com.samarth.memesmagic.ui.components.PartiallyClickableText
import com.samarth.memesmagic.util.Screens.LOGIN_SCREEN
import com.samarth.memesmagic.util.Screens.REGISTER_SCREEN
import kotlinx.coroutines.launch

@Composable
fun LandingPage(
    navController: NavController,
    onSignUpWithGoogle: () -> Unit
) {


    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // App Name
            Column {
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

            }

            SignUp_Login_Buttons(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onSignUpWithGoogle = {
                    onSignUpWithGoogle()
                },
                onSignUpWithEmail = {
                    navController.navigate(REGISTER_SCREEN)
                }
            )



            // Login
            PartiallyClickableText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .align(Alignment.CenterHorizontally),
                nonClickableText = "Already have an Account?",
                clickableText = " Login here"
            ) {
                navController.navigate(LOGIN_SCREEN)
            }

        }


    }


}

@Composable
fun SignUp_Login_Buttons(
    modifier: Modifier = Modifier,
    onSignUpWithGoogle:()->Unit,
    onSignUpWithEmail:()->Unit
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
    }

}
