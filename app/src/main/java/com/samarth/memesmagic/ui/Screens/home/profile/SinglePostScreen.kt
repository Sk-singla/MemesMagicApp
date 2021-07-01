package com.samarth.memesmagic.ui.Screens

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.samarth.memesmagic.data.remote.models.PostResource
import com.samarth.memesmagic.data.remote.response.Post
import com.samarth.memesmagic.ui.Screens.home.feed.FeedViewModel
import com.samarth.memesmagic.ui.components.CustomTopBar
import com.samarth.memesmagic.ui.components.PostItem
import com.samarth.memesmagic.util.CommentsUtil
import com.samarth.memesmagic.util.PostsUtil
import com.samarth.memesmagic.util.Screens
import com.samarth.memesmagic.util.TokenHandler
import kotlinx.coroutines.launch

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
            CustomTopBar(title = "Posts")
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
                            Log.d("MyLog",it)
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
