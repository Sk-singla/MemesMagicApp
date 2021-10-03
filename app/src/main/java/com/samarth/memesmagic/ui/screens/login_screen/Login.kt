package com.samarth.memesmagic.ui.screens.login_screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.samarth.memesmagic.R
import com.samarth.memesmagic.ui.components.*
import com.samarth.memesmagic.util.Screens
import com.samarth.memesmagic.util.TokenHandler
import com.samarth.memesmagic.util.navigateWithPop
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@Composable
fun Login(
    navController:NavController,
    loginScreenViewModel:LoginScreenViewModel = hiltViewModel(),
    onSignUpWithGoogle:()->Unit
) {



    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomTopBar(title = "Login")
        },
    ) {

        val colScrollState = rememberScrollState()
        var passwordVisual: VisualTransformation by remember {
            mutableStateOf(PasswordVisualTransformation())
        }
        var passwordTrailingIcon:Int by remember {
            mutableStateOf(R.drawable.ic_eye)
        }


        Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(colScrollState)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {

                Column{

                    CustomTextField(
                        value = loginScreenViewModel.email.value,
                        onValueChange = {
                            loginScreenViewModel.email.value = it
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .fillMaxWidth(),
                        placeholder = "Email",
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_email),
                                contentDescription = null
                            )
                        }

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
                        leadingIcon ={
                            Icon(
                                painter = painterResource(id = R.drawable.ic_outline_lock),
                                contentDescription = null
                            )
                        } ,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = passwordVisual,
                        trailingIcon ={
                            IconButton(
                                onClick = {
                                    if (passwordTrailingIcon == R.drawable.ic_eye) {
                                        passwordVisual = VisualTransformation.None
                                        passwordTrailingIcon = R.drawable.ic_visibility_off
                                    } else {
                                        passwordVisual = PasswordVisualTransformation()
                                        passwordTrailingIcon = R.drawable.ic_eye
                                    }
                                },
                                modifier = Modifier
                                    .background(color = Color.Transparent)
                                    .clip(CircleShape)
                            ){
                                Icon(
                                    painter = painterResource(id = passwordTrailingIcon),
                                    contentDescription = null
                                )
                            }
                        }
                    )


                    CustomButton(
                        text = "Login",
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                        enabled = !loginScreenViewModel.isLoading.value
                    ) {
                        keyboardController?.hide()
                        loginScreenViewModel.loginUser(
                            onSuccess = { token ->
                                coroutineScope.launch {
                                    TokenHandler.saveJwtToken(
                                        context,
                                        token,
                                        loginScreenViewModel.email.value
                                    )
                                    navController.popBackStack()
                                    navigateWithPop(navController, Screens.HOME_SCREEN)
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        "Login Successful!"
                                    )
                                }
                            },
                            onFail = { errorMsg ->
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        errorMsg ?: "Error!!"
                                    )
                                }
                            }
                        )
                    }
                }

                OrText(Modifier.padding(horizontal = 16.dp))

                Column {
                    CustomButton(
                        text = "Login with Google",
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 16.dp)
                            .fillMaxWidth(),
                        icon = R.drawable.ic_google,
                        backgroundColor = MaterialTheme.colors.secondaryVariant,
                        textColor = MaterialTheme.colors.onSecondary,
                        enabled = !loginScreenViewModel.isLoading.value
                    ) {
                        keyboardController?.hide()
                        onSignUpWithGoogle()
                    }


                    PartiallyClickableText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                            .align(Alignment.CenterHorizontally),
                        nonClickableText = "Don't have an Account?",
                        clickableText = " Sign up here"
                    ) {
                        loginScreenViewModel.clearAllTextFields()
                        navigateWithPop(navController, Screens.REGISTER_SCREEN)
                    }
                }
            }


            if(loginScreenViewModel.isLoading.value){
                CircularProgressIndicator()
            }

        }

    }
}