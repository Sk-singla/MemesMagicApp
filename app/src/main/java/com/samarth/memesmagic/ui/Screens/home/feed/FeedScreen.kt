package com.samarth.memesmagic.ui.Screens.home

import android.app.Application
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
import com.samarth.memesmagic.util.Screens.LANDING_SCREEN
import com.samarth.memesmagic.util.TokenHandler
import com.samarth.memesmagic.util.TokenHandler.getEmail
import com.samarth.memesmagic.util.TokenHandler.logout
import kotlinx.coroutines.delay
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
    var showCongratsDialog by remember{
        mutableStateOf(false)
    }
    var showAdvertiseDialog by remember {
        mutableStateOf(false)
    }
    var newReward:Reward? by remember {
        mutableStateOf(null)
    }


    LaunchedEffect(key1 = Unit) {

        feedViewModel.getYearReward(context,
            newReward = { reward, isItForMe ->
            if(isItForMe){
                showCongratsDialog = true
            } else {
                showAdvertiseDialog = true
            }
            newReward = reward
        },
            onFail = {
                feedViewModel.getReward(context){ reward, isItForMe ->
                    if(isItForMe){
                        showCongratsDialog = true
                    } else {
                        showAdvertiseDialog = true
                    }
                    newReward = reward
                }
            }
        )


        feedViewModel.getFeedFromGithub()
        feedViewModel.getFeed(
            TokenHandler.getJwtToken(context)!!,
            onFail = {
                coroutineScope.launch {
                    Log.d("MyLog","Feed ------------> $it")
                    scaffoldState.snackbarHostState.showSnackbar(it)
                }
            }
        )

        feedViewModel.getUser(
            context,
            getEmail(context)!!,
            onFail = {
                Log.d("MyLog","User ------------> $it")
                 if(!it.contains("timeout")){
                     coroutineScope.launch {
                         logout(context)
                         parentNavController.popBackStack()
                         parentNavController.navigate(LANDING_SCREEN)
                     }
                 } else {
                     coroutineScope.launch {
                         scaffoldState.snackbarHostState.showSnackbar("Heroku Dyno is Down. Please Try Again. After 1-2 request it will Awake.")
                     }
                 }

            },
            onSuccess = {

            }
        )
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {


        newReward?.let {

            CongratsDialogBox(
                showCongratsDialog,
                {
                    showCongratsDialog = false
                    feedViewModel.getReward(context){ reward, isItForMe ->
                        if(isItForMe){
                            showCongratsDialog = true
                        } else {
                            showAdvertiseDialog = true
                        }
                        newReward = reward
                    }
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


            if(feedViewModel.isLoading.value){
                CircularProgressIndicator()
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {

                    itemsIndexed(feedViewModel.posts.value) { pos, post ->


                        if(pos >= feedViewModel.posts.value.size - 10){
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