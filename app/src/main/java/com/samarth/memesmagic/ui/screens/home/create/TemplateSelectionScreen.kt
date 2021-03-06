package com.samarth.memesmagic.ui.screens.home.create

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.request.ImageRequest
import com.google.accompanist.coil.rememberCoilPainter
import com.samarth.memesmagic.R
import com.samarth.memesmagic.ui.components.CustomButton
import com.samarth.memesmagic.ui.components.CustomTopBar
import com.samarth.memesmagic.util.Screens
import com.samarth.memesmagic.util.Screens.EDIT_SCREEN
import com.samarth.memesmagic.util.Screens.NEW_POST_DETAILS_AND_UPLOAD

@ExperimentalFoundationApi
@Composable
fun TemplateSelectionScreen(
    navController:NavHostController,
    createViewModel: CreateViewModel = hiltViewModel(),
    startActivityForResult:(String,(Uri?)->Unit)->Unit,
    updateOrRequestPermissions:()->Boolean
) {


    val templateList by createViewModel.templatesList
    val isLoading by createViewModel.isLoading


    Scaffold(

        topBar = {

            CustomTopBar (
                title = "Select Template"
            )

        }

    ){


        Box(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),contentAlignment = Alignment.Center){

            LazyVerticalGrid(
                cells = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize()
            ) {

                item {

                    Box(
                        modifier = Modifier
                            .padding(1.dp)
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .shadow(1.dp)
                            .clickable {
                                if (updateOrRequestPermissions()) {
                                    createViewModel.chooseVideo(
                                        startActivityForResult = startActivityForResult,
                                        onPickingFile = {
                                            navController.navigate(NEW_POST_DETAILS_AND_UPLOAD)
                                        }
                                    )
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.pick_video),
                            style = MaterialTheme.typography.h5
                        )
                    }

                }

                items(templateList.size) {


                    if(it >= templateList.size - 10 && !createViewModel.endReached.value && !isLoading){
                        createViewModel.getMemeTemplates()
                    }

                    Image(
                        painter = rememberCoilPainter(
                            request = ImageRequest.Builder(LocalContext.current)
                                .data(templateList[it].url)
                                .placeholder(R.drawable.blank_image)
                                .error(R.drawable.ic_error)
                                .build(),
                        ),
                        contentScale = ContentScale.Crop,
                        contentDescription = "User Image",
                        modifier = Modifier
                            .padding(1.dp)
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clickable {
                                navController.navigate("$EDIT_SCREEN/$it")
                            },
                    )
                }

            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ){
                if(isLoading){
                    CircularProgressIndicator()
                }
                if(createViewModel.loadError.value.isNotEmpty()){
                    Column{
                        Text(
                            text = createViewModel.loadError.value,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            color = Color.Red
                        )

                        CustomButton(text = "Retry") {
                            createViewModel.getMemeTemplates()
                        }
                    }


                }
            }


        }

    }

}