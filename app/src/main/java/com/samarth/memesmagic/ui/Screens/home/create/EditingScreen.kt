package com.samarth.memesmagic.ui.Screens.home.create

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.ArrowRightAlt
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.samarth.memesmagic.R
import com.samarth.memesmagic.ui.components.CustomTopBar
import com.samarth.memesmagic.ui.theme.Green500
import com.samarth.memesmagic.ui.theme.Green700
import com.samarth.memesmagic.util.Screens.NEW_POST_DETAILS_AND_UPLOAD
import ja.burhanrashid52.photoeditor.PhotoEditorView
import kotlinx.coroutines.launch

@Composable
fun EditingScreen(
    navController: NavHostController,
    memeTemplateIndex:Int,
    createViewModel: CreateViewModel = hiltViewModel()
) {


    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()



    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {

            CustomTopBar(
                title = "Edit",
                actions = {
                    Icon(
                        imageVector = if(createViewModel.textMode.value) Icons.Default.Done else Icons.Default.ArrowForward,
                        contentDescription = "Done",
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable {

                                if(createViewModel.textMode.value){
                                    if(createViewModel.selectedTextView.value == null){
                                        createViewModel.addText()
                                    } else {
                                        createViewModel.editText()
                                    }
                                } else {
                                    navController.navigate(NEW_POST_DETAILS_AND_UPLOAD)
                                }
                            },
                        tint = if(createViewModel.notToolMode.value) Green500 else Green700
                    )
                }
            )

        }

    ){


        Box(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
            contentAlignment = Alignment.Center
        ){

            Column(
                modifier = Modifier.fillMaxSize()
            ){


                // PHOTO EDITOR VIEW ->
                AndroidView(
                    factory = {
                        PhotoEditorView(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f)
                ) {
                    coroutineScope.launch{
                        createViewModel.initPhotoEditor(
                            context,
                            it,
                            createViewModel.templatesList.value[memeTemplateIndex].url
                        )
                    }
                }
            }



            // TOOLS ->
            if(createViewModel.notToolMode.value) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row{

                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_undo_24),
                                contentDescription = "Undo"
                            )
                            Spacer(modifier = Modifier.padding(16.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_redo_24),
                                contentDescription = "Redo"
                            )

                        }

                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_download_24),
                            contentDescription = "Download",
                            modifier = Modifier.clickable {
                                createViewModel.saveAsBitmapInInternalStorage(
                                    context = context,
                                    onSuccess = {
                                        coroutineScope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                "SuccessFully Downloaded!"
                                            )
                                        }
                                    },
                                    onFail = {
                                        coroutineScope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                it
                                            )
                                        }
                                    }
                                )
                            }
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp, top = 16.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        createViewModel.toolsList.forEach { editTool ->
                            EditToolItem(editTool = editTool)
                        }

                    }
                }
            }



            // BRUSH COLOR, SIZE, OPACITY SELECTION LAYOUT
            if(createViewModel.brushMode.value) {


                BackHandler {
                    createViewModel.endBrushMode()
                }

                var brushSize by remember {
                    createViewModel.brushSize
                }
                var opacity by remember {
                    createViewModel.opacity
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.3f)
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState())
                        .align(Alignment.BottomCenter)
                ) {

                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)){
                        Text(text = "Brush Size", style = MaterialTheme.typography.body1)
                        Slider(
                            value = brushSize,
                            onValueChange = {
                                createViewModel.setBrushSize(it)
                            },
                            valueRange = 0f..100f,
                            modifier = Modifier.padding(0.dp),
                            enabled = true,
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colors.secondary,
                                activeTrackColor = MaterialTheme.colors.secondaryVariant
                            )
                        )
                    }

                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)){
                        Text(text = "Opacity", style = MaterialTheme.typography.body1)
                        Slider(
                            value = opacity.toFloat(),
                            onValueChange = {
                                createViewModel.setOpacity(it.toInt())
                            },
                            valueRange = 0f..100f,
                            modifier = Modifier.padding(0.dp),
                            enabled = true,
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colors.secondary,
                                activeTrackColor = MaterialTheme.colors.secondaryVariant
                            )
                        )
                    }

                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                    ) {
                        Text(text = "Brush Colour", style = MaterialTheme.typography.body1)
                        LazyRow() {

                            items(createViewModel.coloursList){ color ->

                                Surface(
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            createViewModel.setBrushColor(color)
                                        },
                                    color = Color(color)
                                ) {
                                    if(color == createViewModel.curBrushColor.value){
                                        Icon(
                                            Icons.Default.Done, contentDescription = "Colour Selected",
                                            tint = if(color == android.graphics.Color.WHITE) Color.Black else Color.White
                                        )
                                    }
                                }

                            }

                        }
                    }


                }
            }


            // ERASER MODE ->

            if(createViewModel.eraserMode.value){

                BackHandler {
                    createViewModel.endEraserMode()
                }

                val eraserSize by remember {
                    createViewModel.curEraserSize
                }
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
                    .padding(8.dp)
                    .align(Alignment.BottomCenter),
                    verticalArrangement = Arrangement.Center
                ){
                    Text(text = "Eraser Size", style = MaterialTheme.typography.body1)
                    Slider(
                        value = eraserSize,
                        onValueChange = {
                            createViewModel.setEraserSize(it)
                        },
                        valueRange = 0f..100f,
                        modifier = Modifier.padding(0.dp),
                        enabled = true,
                    )
                }

            }


            // TEXT MODE ->


            if(createViewModel.textMode.value){

                BackHandler {
                    createViewModel.endTextMode()
                }

                val curText by remember {
                    createViewModel.curText
                }

                val focusRequester = remember {
                    FocusRequester()
                }

                DisposableEffect(key1 = Unit) {
                    focusRequester.requestFocus()
                    onDispose {  }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha=0.5f)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){


                    LazyRow {

                        items(createViewModel.coloursList){ color ->

                            Surface(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        createViewModel.setTextColor(color)
                                    },
                                color = Color(color)
                            ) {
                                if(color == createViewModel.curTextColor.value){
                                    Icon(
                                        Icons.Default.Done, contentDescription = "Colour Selected",
                                        tint = if(color == android.graphics.Color.WHITE) Color.Black else Color.White
                                    )
                                }
                            }

                        }

                    }


                    TextField(
                        value  = curText,
                        onValueChange = {
                            createViewModel.curText.value = it
                        },
                        modifier= Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp, start = 4.dp, end = 4.dp)
                            .focusRequester(focusRequester),
                        textStyle = TextStyle(textAlign = TextAlign.Center),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = MaterialTheme.colors.secondary,
                            textColor = Color(createViewModel.curTextColor.value)
                        ),
                    )

                }

            }



        }

    }



}




data class EditTool(
    val name:String,
    @DrawableRes val icon:Int,
    val onClick: ()->Unit
)

@Composable
fun EditToolItem(editTool: EditTool) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {
            editTool.onClick()
        }
    ) {
        Icon(painter = painterResource(id = editTool.icon), contentDescription = editTool.name)
        Text(text = editTool.name, style = MaterialTheme.typography.body2)
    }

}