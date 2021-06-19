package com.samarth.memesmagic.ui.Screens.RegisterScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.samarth.memesmagic.R
import com.samarth.memesmagic.ui.components.CustomButton
import com.samarth.memesmagic.ui.components.CustomTextField
import com.samarth.memesmagic.ui.components.CustomTopBar

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


            Text(
                text = "————————— OR —————————",
                style = MaterialTheme.typography.body2,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp,vertical = 32.dp),
                textAlign = TextAlign.Center,
                maxLines = 1,
            )

            CustomButton(
                text = "Sign up with Google",
                modifier = Modifier
                    .padding(horizontal = 24.dp,vertical = 16.dp)
                    .fillMaxWidth(),
                icon = R.drawable.ic_google,
                backgroundColor = MaterialTheme.colors.secondaryVariant,
                textColor = MaterialTheme.colors.onSecondary
            ) {

            }


            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colors.secondaryVariant)) {
                        append("Already have a Account?")
                    }
                    withStyle(style = SpanStyle(color = MaterialTheme.colors.secondaryVariant,fontWeight = FontWeight.Bold)) {
                        append(" Login here")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable(onClick = {
                        // GO TO LOGIN SECTION

                    }),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1
            )

        }

    }

}



























