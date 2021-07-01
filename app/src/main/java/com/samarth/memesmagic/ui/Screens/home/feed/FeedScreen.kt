package com.samarth.memesmagic.ui.Screens.home

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.samarth.memesmagic.data.remote.models.MemeBadgeType
import com.samarth.memesmagic.data.remote.models.PostResource
import com.samarth.memesmagic.data.remote.response.Reward
import com.samarth.memesmagic.ui.Screens.home.feed.FeedViewModel
import com.samarth.memesmagic.ui.components.AdvertiseDialogBox
import com.samarth.memesmagic.ui.components.CongratsDialogBox
import com.samarth.memesmagic.ui.components.PostItem
import com.samarth.memesmagic.util.CommentsUtil
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.Screens
import com.samarth.memesmagic.util.Screens.ANOTHER_USER_PROFILE_SCREEN
import com.samarth.memesmagic.util.Screens.COMMENT_SCREEN
import com.samarth.memesmagic.util.Screens.EDIT_PROFILE_SCREEN
import com.samarth.memesmagic.util.Screens.HOME_PROFILE
import com.samarth.memesmagic.util.Screens.HOME_REWARDS
import kotlinx.coroutines.launch

@Composable
fun FeedScreen(
    scaffoldState: ScaffoldState,
    feedViewModel: FeedViewModel = hiltViewModel(),
    startActivity:(Intent)->Unit,
    currentNavController:NavController,
    parentNavController: NavController
) {

    val context = LocalContext.current
    val coroutineScope  = rememberCoroutineScope()
    var showCongratsDialog by remember{ mutableStateOf(false) }
    var showAdvertiseDialog by remember {
        mutableStateOf(false)
    }
    var newReward:Reward? by remember {
        mutableStateOf(null)
    }


    DisposableEffect(key1 = Unit) {


        feedViewModel.getYearReward(context){ reward, isItForMe ->
            if(isItForMe){
                showCongratsDialog = true
            } else {
                showAdvertiseDialog = true
            }
            newReward = reward
        }
        onDispose {  }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {


        newReward?.let {

            CongratsDialogBox(
                showCongratsDialog,
                {
                    showCongratsDialog = false
                },
                it,
                onClick = {
                    currentNavController.navigate(HOME_REWARDS)
                }
            )

            feedViewModel.rewardWinner.value?.let { rewardWinnerInfo ->

                AdvertiseDialogBox(
                    showDialog = showAdvertiseDialog,
                    onDismiss = {
                        showAdvertiseDialog = false
                    },
                    reward = it,
                    rewardWinnerInfo =rewardWinnerInfo,
                    onClick = {
                          parentNavController.navigate("${Screens.ANOTHER_USER_PROFILE_SCREEN}/${rewardWinnerInfo.email}")
                    },
                    isFollowingToUser = feedViewModel.isFollowingToRewardyy.value,
                    onFollowBtnPressed = {
                        feedViewModel.followUser(context)
                        showAdvertiseDialog = false
                    }
                )

            }

        }


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


                        if(pos >= feedViewModel.posts.size - 10){
                            feedViewModel.getFeedFromGithub()
                        }


                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            PostItem(
                                post = post,
                                isLiked = feedViewModel.isPostLiked(post, context),
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
                                },
                                onClick = {
                                    if(post.postResource == PostResource.GITHUB_API) {
                                        coroutineScope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar("It is a Bot!")
                                        }
                                    }
                                    else {
                                        parentNavController.navigate("$ANOTHER_USER_PROFILE_SCREEN/${post.createdBy.email}")
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