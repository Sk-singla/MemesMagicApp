package com.samarth.memesmagic.ui.Screens.LoginScreen

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.samarth.memesmagic.R
import com.samarth.memesmagic.ui.components.*


@Composable
fun LoginScreen(
    navController: NavController,
    loginScreenViewModel: LoginScreenViewModel = hiltViewModel()
) {

    val scaffoldState = rememberScaffoldState()
    val configuration = LocalConfiguration.current


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomTopBar(title = "Login")
        },
    ) {



        val colScrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(colScrollState)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp))
            }

            CustomTextField(
                value = loginScreenViewModel.email.value,
                onValueChange = {
                    loginScreenViewModel.email.value = it
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .fillMaxWidth(),
                placeholder = "Email",
                leadingIconDrawable = R.drawable.ic_email
            )

            CustomTextField(
                value = loginScreenViewModel.password.value,
                onValueChange = {
                    loginScreenViewModel.password.value = it
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .fillMaxWidth(),
                placeholder = "Password",
                leadingIconDrawable = R.drawable.ic_outline_lock
            )


            if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp))
            }

            CustomButton(
                text = "Login",
                modifier = Modifier.padding(horizontal = 32.dp,vertical = 16.dp)
            ) {

            }


            if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                OrText(Modifier.padding(horizontal = 16.dp, vertical = 64.dp))
            } else {
                OrText(Modifier.padding(horizontal = 16.dp, vertical = 32.dp))
            }

            CustomButton(
                text = "Login with Google",
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
                nonClickableText = "Don't have an Account?",
                clickableText = " Sign up here"
            ) {
                loginScreenViewModel.clearAllTextFields()
                navController.navigate("register_screen")
            }
        }

    }

}



























