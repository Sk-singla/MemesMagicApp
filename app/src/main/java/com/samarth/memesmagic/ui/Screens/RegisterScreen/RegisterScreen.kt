package com.samarth.memesmagic.ui.Screens.RegisterScreen

import android.content.res.Configuration
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.samarth.memesmagic.R
import com.samarth.memesmagic.ui.components.*

@Composable
fun RegisterScreen(
    navController: NavController,
    registerScreenViewModel: RegisterScreenViewModel = hiltViewModel()
) {

    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomTopBar(title = "Register")
        },
    ) {

        // SIGN UP WITH EMAIL
        val colScrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(colScrollState)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {



            CustomTextField(
                value = registerScreenViewModel.name.value,
                onValueChange = {
                    registerScreenViewModel.name.value = it
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .fillMaxWidth(),
                placeholder = "Name",
                leadingIconDrawable = R.drawable.ic_person
            )

            CustomTextField(
                value = registerScreenViewModel.email.value,
                onValueChange = {
                    registerScreenViewModel.email.value = it
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .fillMaxWidth(),
                placeholder = "Email",
                leadingIconDrawable = R.drawable.ic_email
            )

            CustomTextField(
                value = registerScreenViewModel.password.value,
                onValueChange = {
                    registerScreenViewModel.password.value = it
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .fillMaxWidth(),
                placeholder = "Password",
                leadingIconDrawable = R.drawable.ic_outline_lock
            )

            CustomTextField(
                value = registerScreenViewModel.confirmPassword.value,
                onValueChange = {
                    registerScreenViewModel.confirmPassword.value = it
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .fillMaxWidth(),
                placeholder = "Confirm Password",
                leadingIconDrawable = R.drawable.ic_lock
            )

            CustomButton(
                text = "Create Account",
                modifier = Modifier.padding(horizontal = 32.dp,vertical = 16.dp)
            ) {

            }


            OrText(Modifier.padding(horizontal = 16.dp, vertical = 32.dp))

            CustomButton(
                text = "Sign up with Google",
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxWidth(),
                icon = R.drawable.ic_google,
                backgroundColor = MaterialTheme.colors.secondaryVariant,
                textColor = MaterialTheme.colors.onSecondary
            ) {

            }


            PartiallyClickableText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                nonClickableText = "Already have an Account?",
                clickableText = " Login here"
            ) {
                registerScreenViewModel.clearAllTextFields()
                navController.navigate("login_screen")
            }
        }

    }

}



























