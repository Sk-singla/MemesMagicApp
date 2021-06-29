package com.samarth.memesmagic.ui.Screens.RegisterScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.samarth.memesmagic.R
import com.samarth.memesmagic.ui.components.*
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.Screens.HOME_SCREEN
import com.samarth.memesmagic.util.Screens.LOGIN_SCREEN
import com.samarth.memesmagic.util.TokenHandler.saveJwtToken
import com.samarth.memesmagic.util.navigateWithPop
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    navController: NavController,
    registerScreenViewModel: RegisterScreenViewModel = hiltViewModel()
) {

    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomTopBar(title = "Register")
        },
    ) {

        val colScrollState = rememberScrollState()
        val scope = rememberCoroutineScope()
        var confirmPasswordVisual:VisualTransformation by remember {
            mutableStateOf(PasswordVisualTransformation())
        }
        var confirmPasswordTrailingIcon:Int by remember {
            mutableStateOf(R.drawable.ic_eye)
        }
        var passwordVisual:VisualTransformation by remember {
            mutableStateOf(PasswordVisualTransformation())
        }
        var passwordTrailingIcon:Int by remember {
            mutableStateOf(R.drawable.ic_eye)
        }



        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {


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
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_person),
                            contentDescription = null
                        )
                    }
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
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_email),
                            contentDescription = null
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
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
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_lock),
                            contentDescription = null
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {

                        IconButton(
                            onClick = {
                                if(passwordTrailingIcon == R.drawable.ic_eye) {
                                    passwordVisual = VisualTransformation.None
                                    passwordTrailingIcon = R.drawable.ic_visibility_off
                                } else{
                                    passwordVisual = PasswordVisualTransformation()
                                    passwordTrailingIcon = R.drawable.ic_eye
                                }
                            },
                            modifier =Modifier.background(color = Color.Transparent).clip(
                                CircleShape
                            )
                        ){
                            Icon(
                                painter = painterResource(id = passwordTrailingIcon),
                                contentDescription = null
                            )
                        }
                    } ,
                    visualTransformation = passwordVisual
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
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_lock),
                            contentDescription = null
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if(confirmPasswordTrailingIcon == R.drawable.ic_eye) {
                                    confirmPasswordVisual = VisualTransformation.None
                                    confirmPasswordTrailingIcon = R.drawable.ic_visibility_off
                                } else{
                                    confirmPasswordVisual = PasswordVisualTransformation()
                                    confirmPasswordTrailingIcon = R.drawable.ic_eye
                                }
                            },
                            modifier =Modifier.background(color = Color.Transparent).clip(
                                CircleShape
                            )
                        ){
                            Icon(
                                painter = painterResource(id = confirmPasswordTrailingIcon),
                                contentDescription = null
                            )
                        }
                    } ,
                    visualTransformation = confirmPasswordVisual
                )

                CustomButton(
                    text = "Create Account",
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    registerScreenViewModel.registerUser()
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
                    navigateWithPop(navController,LOGIN_SCREEN)
                }
            }




            when (registerScreenViewModel.registerStatus.value) {
                is Resource.Error -> {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            registerScreenViewModel.registerStatus.value.message ?: "Error"
                        )
                    }
                }
                is Resource.Loading -> {
                    CircularProgressIndicator()
                }
                is Resource.Success -> {
                    scope.launch {
                        saveJwtToken(context,registerScreenViewModel.registerStatus.value.data!!,registerScreenViewModel.email.value)
                        scaffoldState.snackbarHostState.showSnackbar(
                            "Account Created!"
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



























