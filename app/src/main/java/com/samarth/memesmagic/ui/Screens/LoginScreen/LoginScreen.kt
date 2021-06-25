package com.samarth.memesmagic.ui.Screens.LoginScreen

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.samarth.memesmagic.R
import com.samarth.memesmagic.ui.components.*
import com.samarth.memesmagic.util.Constants.navigateWithPop
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.Screens.HOME_SCREEN
import com.samarth.memesmagic.util.Screens.REGISTER_SCREEN
import com.samarth.memesmagic.util.TokenHandler
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    navController: NavController,
    loginScreenViewModel: LoginScreenViewModel = hiltViewModel()
) {

    val scaffoldState = rememberScaffoldState()
    val configuration = LocalConfiguration.current

    var passwordVisual: VisualTransformation by remember {
        mutableStateOf(PasswordVisualTransformation())
    }
    var passwordTrailingIcon:Int by remember {
        mutableStateOf(R.drawable.ic_eye)
    }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomTopBar(title = "Login")
        },
    ) {

        val colScrollState = rememberScrollState()

        Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(colScrollState)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    )
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
                    leadingIconDrawable = R.drawable.ic_outline_lock,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = passwordVisual,
                    trailingIconDrawable = passwordTrailingIcon,
                    onTrailingIconClick = {
                        if (passwordTrailingIcon == R.drawable.ic_eye) {
                            passwordVisual = VisualTransformation.None
                            passwordTrailingIcon = R.drawable.ic_visibility_off
                        } else {
                            passwordVisual = PasswordVisualTransformation()
                            passwordTrailingIcon = R.drawable.ic_eye
                        }
                    }
                )


                if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    )
                }

                CustomButton(
                    text = "Login",
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    loginScreenViewModel.loginUser()
                }


                if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
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
                    navigateWithPop(navController,REGISTER_SCREEN)
                }
            }





            when (loginScreenViewModel.loginStatus.value) {
                is Resource.Error -> {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            loginScreenViewModel.loginStatus.value.message ?: "Error!!"
                        )
                    }
                }
                is Resource.Loading -> {
                    CircularProgressIndicator()
                }
                is Resource.Success -> {
                    scope.launch {
                        TokenHandler.saveJwtToken(
                            context,
                            loginScreenViewModel.loginStatus.value.data!!,
                            loginScreenViewModel.email.value
                        )
                        scaffoldState.snackbarHostState.showSnackbar(
                            "Login Successful!"
                        )
                    }
                    navController.popBackStack()
                    navigateWithPop(navController,HOME_SCREEN)
                }
                else -> {

                }
            }





        }

    }

}



























