package com.samarth.memesmagic.ui.Screens.home

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.samarth.memesmagic.ui.Screens.home.feed.FeedViewModel
import com.samarth.memesmagic.ui.components.PostItem
import com.samarth.memesmagic.util.CommentsUtil
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.Screens.COMMENT_SCREEN
import kotlinx.coroutines.launch

@Composable
fun FeedScreen(
    scaffoldState: ScaffoldState,
    feedViewModel: FeedViewModel = hiltViewModel(),
    startActivity:(Intent)->Unit,
    parentNavController: NavController
) {

    val context = LocalContext.current
    val coroutineScope  = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {


        when (feedViewModel.postStatus.value) {

            is Resource.Error -> {

                Text(
                    text = feedViewModel.postStatus.value.message!!,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.h5,
                    textAlign = TextAlign.Center
                )

            }

            is Resource.Loading -> {
                CircularProgressIndicator()
            }

            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {

                    itemsIndexed(feedViewModel.posts) { pos, post ->
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            PostItem(
                                post = post,
                                isLiked = feedViewModel.isPostLiked(post, context),
                                onLikeIconPressed = { post,isPostLiked, onSuccess  ->
                                    if(isPostLiked) {
                                        feedViewModel.dislikePost(post,context,onSuccess)
                                    } else {
                                        feedViewModel.likePost(post, context, onSuccess)
                                    }
                                },
                                onCommentIconPressed = {
                                    CommentsUtil.post = it
                                    parentNavController.navigate(COMMENT_SCREEN)
                                },
                                onShareIconPressed = {
                                    feedViewModel.shareImage(
                                        it.mediaLink,
                                        context,
                                        startActivity
                                    ){
                                        coroutineScope.launch {
                                            Log.d("MyLog",it)
                                            scaffoldState.snackbarHostState.showSnackbar(it)
                                        }
                                    }
                                }
                            )

                            if (!feedViewModel.isItLastItem(pos)) {
                                Divider()
                            } else {
                                Spacer(modifier = Modifier.padding(24.dp))
                            }
                        }
                    }
                }
            }
        }


    }

}