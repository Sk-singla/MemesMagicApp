package com.samarth.memesmagic.ui.Screens.RegisterScreen

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.samarth.memesmagic.R
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
        }
    ) {

        // SIGN UP WITH EMAIL
        val colScrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .scrollable(
                    colScrollState,
                    Orientation.Vertical
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CustomTextField(
                value = registerScreenViewModel.name.value,
                onValueChange = {
                    registerScreenViewModel.name.value = it
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp,vertical = 8.dp)
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
                    .padding(horizontal = 16.dp,vertical = 8.dp)
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
                    .padding(horizontal = 16.dp,vertical = 8.dp)
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
                    .padding(horizontal = 16.dp,vertical = 8.dp)
                    .fillMaxWidth(),
                placeholder = "Confirm Password",
                leadingIconDrawable = R.drawable.ic_lock
            )

        }

    }

}



























