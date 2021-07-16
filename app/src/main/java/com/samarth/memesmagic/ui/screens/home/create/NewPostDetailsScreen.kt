package com.samarth.memesmagic.ui.screens.home.create

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.samarth.memesmagic.R
import com.samarth.memesmagic.ui.components.CustomButton
import com.samarth.memesmagic.ui.components.CustomTextField
import com.samarth.memesmagic.ui.components.CustomTopBar
import com.samarth.memesmagic.util.Screens.HOME_SCREEN
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@Composable
fun NewPostDetailsScreen(
    navController: NavHostController,
    createViewModel: CreateViewModel = hiltViewModel()
) {


    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val caption by remember {
        createViewModel.caption
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {

            CustomTopBar(
                title = "New Post"
            )

        }

    ){


        Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center) {

            Column(
                modifier = Modifier.fillMaxSize(),
            ) {

                CustomTextField(
                    value = caption,
                    onValueChange = {
                        createViewModel.caption.value = it
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f),
                    placeholder = "Caption"
                )

                CustomButton(
                    text = "Post",
                    icon = R.drawable.ic_upload,
                    modifier = Modifier.padding(16.dp),
                    enabled = !createViewModel.isLoading.value
                ) {

                    keyboardController?.hide()
                    createViewModel.uploadPost(
                        context = context,
                        onFail = {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    it
                                )
                            }
                        },
                        onSuccess = {
                            coroutineScope.launch {
                                navController.popBackStack(HOME_SCREEN,
                                    inclusive = false,
                                    saveState = true
                                )
                                scaffoldState.snackbarHostState.showSnackbar(
                                    "SuccessFully Posted!"
                                )
                            }
                        }
                    )
                }

            }

            if(createViewModel.isLoading.value){
                CircularProgressIndicator()
            }


        }


    }

}