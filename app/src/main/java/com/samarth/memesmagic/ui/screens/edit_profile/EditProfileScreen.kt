package com.samarth.memesmagic.ui.screens.edit_profile

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.request.ImageRequest
import com.google.accompanist.coil.rememberCoilPainter
import com.samarth.memesmagic.R
import com.samarth.memesmagic.ui.components.CustomButton
import com.samarth.memesmagic.ui.components.CustomTextField
import com.samarth.memesmagic.ui.components.CustomTopBar
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(
    navController: NavController,
    startActivityForResult: (String, (Uri?)->Unit)->Unit,
    updateOrRequestPermissions:()->Boolean,
    editProfileViewModel: EditProfileViewModel = hiltViewModel()
) {


    val user by remember {
        editProfileViewModel.currentUser
    }
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomTopBar(title = "Edit Profile")
        }
    ){

        DisposableEffect(key1 = Unit) {
            editProfileViewModel.getUser(context)
            onDispose {  }
        }


        if(editProfileViewModel.isLoading.value){
            Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center){
                CircularProgressIndicator()
            }
        }



        Column(
            modifier = Modifier.fillMaxSize()
        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = rememberCoilPainter(
                        request = ImageRequest.Builder(LocalContext.current)
                            .data(editProfileViewModel.profilePic.value ?: editProfileViewModel.profilePicUrl.value)
                            .placeholder(R.drawable.ic_person)
                            .error(R.drawable.ic_person)
                            .build(),
                        fadeIn = true,
                    ),
                    contentScale = ContentScale.Crop,
                    contentDescription = "User Image",
                    modifier = Modifier
                        .padding(end = 8.dp, bottom = 8.dp)
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colors.onBackground.copy(alpha = 0.8f),
                            shape = CircleShape
                        )
                        .clickable {
                            if(updateOrRequestPermissions()) {
                                editProfileViewModel.selectProfilePic(startActivityForResult)
                            } else {
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar("Please Provide Storage Permission!")
                                }
                            }
                        }
//                    .background(color = MaterialTheme.colors.onBackground.copy(alpha = 0.85f)),
                )


                CustomTextField (
                    value = editProfileViewModel.userName.value,
                    onValueChange = {
                        editProfileViewModel.userName.value = it
                    },
                    placeholder = "Name"
                )

            }


            CustomTextField (
                value = editProfileViewModel.bio.value,
                onValueChange = {
                    editProfileViewModel.bio.value = it
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f),
                placeholder = "Bio"
            )


            CustomButton(
                text = "Done",
                modifier = Modifier.padding(16.dp)
            ) {
                editProfileViewModel.updateProfile(
                    context = context,
                    onSuccess = {
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("Successfully Updated Profile!")
                        }
                        navController.popBackStack()
                    },
                    onFail = {
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("$it . Please Retry!")
                        }

                    }
                )
            }

        }

    }


}