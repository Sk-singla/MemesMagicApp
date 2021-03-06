package com.samarth.memesmagic.ui.screens.home.profile

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.samarth.memesmagic.data.remote.models.PostResource
import com.samarth.memesmagic.ui.screens.home.feed.FeedViewModel
import com.samarth.memesmagic.ui.components.CustomTopBar
import com.samarth.memesmagic.ui.components.items.PostItem
import com.samarth.memesmagic.util.CommentsUtil
import com.samarth.memesmagic.util.Screens
import kotlinx.coroutines.launch
import com.samarth.memesmagic.R
import com.samarth.memesmagic.data.remote.models.PostType
import com.samarth.memesmagic.ui.exoplayer.UpdateCurrentlyPlayingItem
import com.samarth.memesmagic.ui.exoplayer.provideTrackSelectorDialog
import com.samarth.memesmagic.util.TokenHandler.getEmail

@ExperimentalAnimationApi
@Composable
fun SinglePostScreen(
    parentNavController:NavController,
    startActivity:(Intent)->Unit,
    feedViewModel:FeedViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    var email by remember {
        mutableStateOf("")
    }
    var deletePostDialogBoxVisible by remember {
        mutableStateOf(false)
    }

    var isExoplayerViewVisible by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(key1 = Unit) {
        email = getEmail(context) ?: ""
        if(feedViewModel.thumbnails[CommentsUtil.post!!.id] == null){
            feedViewModel.generateThumbnail(CommentsUtil.post!!)
        }
    }
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner) {
        val lifeCycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            when(event) {
                Lifecycle.Event.ON_PAUSE -> {
                    feedViewModel.player.playWhenReady = false
                    feedViewModel.playWhenReady = false
                }
                Lifecycle.Event.ON_RESUME -> {
                    feedViewModel.player.playWhenReady = feedViewModel.playWhenReady
                }
            }

        }
        lifeCycle.addObserver(observer)
        onDispose {
            lifeCycle.removeObserver(observer)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomTopBar(title = "Post",actions = {
                if(CommentsUtil.post?.createdBy?.email == email) {
                    IconButton(
                        onClick = {
                            feedViewModel.deletePost(
                                post = CommentsUtil.post!!,
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
                        },
                        enabled = !feedViewModel.isLoading.value
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                            contentDescription = "Delete Post"
                        )
                    }
                }
            })
        }
    ) {




        if(CommentsUtil.post!!.postType == PostType.VIDEO){
            UpdateCurrentlyPlayingItem(
                post = CommentsUtil.post!!,
                player = feedViewModel.player,
                dataSourceFactory = feedViewModel.dataSourceFactory,
            )
        }



        if(CommentsUtil.post != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                PostItem(
                    post = CommentsUtil.post!!,
                    isLiked = feedViewModel.isPostLiked(CommentsUtil.post!!, context),
                    onLikeIconPressed = { post, isPostLiked, onSuccess ->

                        if (post.postResource == PostResource.GITHUB_API) {
                            onSuccess()
                        } else {
                            if (isPostLiked) {
                                feedViewModel.dislikePost(post, onSuccess)
                            } else {
                                feedViewModel.likePost(post, onSuccess)
                            }
                        }
                    },
                    onCommentIconPressed = {
                        CommentsUtil.post = it
                        parentNavController.navigate(Screens.COMMENT_SCREEN)
                    },
                    onShareIconPressed = {
                        feedViewModel.shareImage(
                            context,
                            it.mediaLink,
                            startActivity
                        ) {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(it)
                            }
                        }
                    },
                    onClick = {
                        parentNavController.navigate("${Screens.ANOTHER_USER_PROFILE_SCREEN}/${CommentsUtil.post!!.createdBy.email}")
                    },
                    isSinglePost = true,
                    exoPlayer = feedViewModel.player,
                    playerViewVisible = isExoplayerViewVisible,
                    thumbnail = feedViewModel.thumbnails[CommentsUtil.post!!.id],
                    changeVideoQuality = {
                        Log.d("video quality","${feedViewModel.player.videoSize.width}/${feedViewModel.trackSelector.parameters.maxVideoHeight}, ${feedViewModel.player.videoSize.height}")
                        provideTrackSelectorDialog(
                            context = context,
                            trackSelector = feedViewModel.trackSelector,
                            exoplayer = feedViewModel.player
                        ).show()
                    }
                )

                if (feedViewModel.isLoading.value) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

            }
        }

    }

}
