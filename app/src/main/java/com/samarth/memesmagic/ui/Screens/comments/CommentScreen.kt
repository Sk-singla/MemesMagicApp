package com.samarth.memesmagic.ui.Screens.comments

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.samarth.memesmagic.data.remote.response.Post
import com.samarth.memesmagic.ui.components.CommentItem
import com.samarth.memesmagic.ui.components.CustomTextField
import com.samarth.memesmagic.ui.components.CustomTopBar
import com.samarth.memesmagic.ui.theme.Green700

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

    val context = LocalContext.current


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomTopBar(title = "Comments")
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {


            Row(modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.surface)
                .align(Alignment.BottomCenter),horizontalArrangement = Arrangement.SpaceBetween) {

                CustomTextField(
                    value = comment,
                    onValueChange = {
                        commentViewModel.comment.value = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    trailingIcon = {
                        Text(text = "Post",color = Green700,modifier = Modifier
                            .padding(4.dp)
                            .clickable {
                                // post comment
                                commentViewModel.addComment(context) {
                                    commentViewModel.comment.value = ""
                                    post?.comments?.add(it)
                                }
                            })
                    }
                )


            }





            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {

                item {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)) {

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

                items(commentViewModel.commentsList.value) { comment ->

                    CommentItem(comment = comment, isCommentLiked = false) { onSuccess ->
                        commentViewModel.likeUnlikeToggle(context, comment, onSuccess)
                    }

                }

            }


        }
    }


}

