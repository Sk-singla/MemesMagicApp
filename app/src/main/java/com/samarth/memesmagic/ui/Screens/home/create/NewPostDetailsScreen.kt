package com.samarth.memesmagic.ui.Screens.home.create

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.samarth.memesmagic.R
import com.samarth.memesmagic.ui.components.CustomButton
import com.samarth.memesmagic.ui.components.CustomTextField
import com.samarth.memesmagic.ui.components.CustomTopBar
import com.samarth.memesmagic.ui.theme.Green500
import com.samarth.memesmagic.ui.theme.Green700
import com.samarth.memesmagic.util.Screens
import com.samarth.memesmagic.util.Screens.HOME_SCREEN
import kotlinx.coroutines.launch

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

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {

            CustomTopBar(
                title = "New Post"
            )

        }

    ){


        Column(
            modifier = Modifier.fillMaxSize(),
        ) {

            CustomTextField(
                value = caption,
                onValueChange = {
                    createViewModel.caption.value = it
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp,vertical = 8.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f),
                placeholder = "Caption"
            )

            CustomButton(
                text = "Post",
                icon = R.drawable.ic_upload,
                modifier = Modifier.padding(16.dp)
            ) {

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
                            scaffoldState.snackbarHostState.showSnackbar(
                                "SuccessFully Posted!"
                            )
                            navController.popBackStack(HOME_SCREEN,false,true)
                        }
                    }
                )
            }

        }


    }

}