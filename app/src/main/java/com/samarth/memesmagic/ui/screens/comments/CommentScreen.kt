package com.samarth.memesmagic.ui.screens.comments

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.request.ImageRequest
import com.google.accompanist.coil.rememberCoilPainter
import com.samarth.memesmagic.R
import com.samarth.memesmagic.ui.components.CommentItem
import com.samarth.memesmagic.ui.components.CustomTextField
import com.samarth.memesmagic.ui.components.CustomTopBar
import com.samarth.memesmagic.util.Screens

@ExperimentalComposeUiApi
@Composable
fun CommentScreen(
    navController: NavController,
    commentViewModel: CommentViewModel = hiltViewModel()
) {

    val scaffoldState = rememberScaffoldState()
    var comment by remember{
        commentViewModel.comment
    }

    val post by remember {
        commentViewModel.post
    }

    val commentsList by remember {
        commentViewModel.commentsList
    }

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomTopBar(title = "Comments")
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {


            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {

                item {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                    ) {

                        Image(
                            painter = rememberCoilPainter(
                                request = ImageRequest.Builder(LocalContext.current)
                                    .data(post?.createdBy?.profilePic ?: R.drawable.ic_person)
                                    .placeholder(R.drawable.ic_person)
                                    .error(R.drawable.ic_person)
                                    .build(),
                                fadeIn = true,
                            ),
                            contentScale = ContentScale.Crop,
                            contentDescription = "User Image",
                            modifier = Modifier
                                .padding(start = 8.dp, end = 16.dp, top = 8.dp)
                                .size(32.dp)
                                .clip(CircleShape),
                        )

                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                ) {
                                    append(post?.createdBy?.name + "   ")
                                }
                                post?.description?.let { description ->
                                    withStyle(style = SpanStyle(fontSize = 16.sp)) {
                                        append(description)
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                }

                item{
                    Divider()
                }

                items(commentsList) { comment ->
                    CommentItem(
                        comment = comment,
                        isCommentLiked = false,
                        onCommentLikeUnlikePressed = { onSuccess ->
                            commentViewModel.likeUnlikeToggle(comment, onSuccess)
                        },
                        onProfileClick = {
                            navController.navigate("${Screens.ANOTHER_USER_PROFILE_SCREEN}/${comment.userInfo.email}")
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.padding(64.dp))
                }

            }




            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .align(Alignment.BottomCenter)
                    .background(color = MaterialTheme.colors.background),
                contentAlignment = Alignment.Center
            ) {


                CustomTextField(
                    value = comment,
                    onValueChange = {
                        commentViewModel.comment.value = it
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
//                        .align(Alignment.BottomCenter),
                    trailingIcon = {
                        IconButton(onClick = {
                            keyboardController?.hide()
                            commentViewModel.addComment {
                                commentViewModel.comment.value = ""
                                if (post?.comments != null) {
                                    post?.comments?.add(it)
                                } else {
                                    post?.comments = mutableListOf(it)
                                }
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_send_24),
                                contentDescription = "Send Comment"
                            )
                        }
                    }
                )

            }


        }
    }


}

