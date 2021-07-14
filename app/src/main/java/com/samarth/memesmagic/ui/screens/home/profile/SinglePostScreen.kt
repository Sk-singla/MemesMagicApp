package com.samarth.memesmagic.ui.screens

import android.content.Intent
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.samarth.memesmagic.data.remote.models.PostResource
import com.samarth.memesmagic.ui.screens.home.feed.FeedViewModel
import com.samarth.memesmagic.ui.components.CustomTopBar
import com.samarth.memesmagic.ui.components.PostItem
import com.samarth.memesmagic.util.CommentsUtil
import com.samarth.memesmagic.util.Screens
import kotlinx.coroutines.launch
import com.samarth.memesmagic.R

@Composable
fun SinglePostScreen(
    parentNavController:NavController,
    startActivity:(Intent)->Unit,
    feedViewModel:FeedViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomTopBar(title = "Post",actions = {
                IconButton(onClick = {
                    feedViewModel.deletePost(
                        post = CommentsUtil.post!!,
                        context,
                        onSuccess = {
                            parentNavController.popBackStack()
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("Post Deleted Successfully!")
                            }
                        },
                        onFail = {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(it)
                            }
                        }
                    )
                }) {
                    Icon(painter = painterResource(id = R.drawable.ic_baseline_delete_24), contentDescription = "Delete Post")
                }
            })
        }
    ) {



        if(CommentsUtil.post != null) {

            PostItem(
                post = CommentsUtil.post!!,
                isLiked = feedViewModel.isPostLiked(CommentsUtil.post!!,context),
                onLikeIconPressed = { post,isPostLiked, onSuccess  ->

                    if(post.postResource == PostResource.GITHUB_API){
                        onSuccess()
                    } else {
                        if (isPostLiked) {
                            feedViewModel.dislikePost(post, context, onSuccess)
                        } else {
                            feedViewModel.likePost(post, context, onSuccess)
                        }
                    }
                },
                onCommentIconPressed =  {
                    CommentsUtil.post = it
                    parentNavController.navigate(Screens.COMMENT_SCREEN)
                },
                onShareIconPressed = {
                    feedViewModel.shareImage(
                        it.mediaLink,
                        context,
                        startActivity
                    ){
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(it)
                        }
                    }
                },
                onClick =  {

                }
            )

        }

    }

}
