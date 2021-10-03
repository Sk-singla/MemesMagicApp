package com.samarth.memesmagic.ui.screens.home.feed

import android.content.Intent
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.google.firebase.messaging.FirebaseMessaging
import com.samarth.memesmagic.data.remote.models.PostResource
import com.samarth.memesmagic.data.remote.response.Reward
import com.samarth.memesmagic.ui.components.AdvertiseDialogBox
import com.samarth.memesmagic.ui.components.CongratsDialogBox
import com.samarth.memesmagic.ui.components.items.PostItem
import com.samarth.memesmagic.ui.exoplayer.UpdateCurrentlyPlayingItem
import com.samarth.memesmagic.util.CommentsUtil
import com.samarth.memesmagic.util.Screens.ANOTHER_USER_PROFILE_SCREEN
import com.samarth.memesmagic.util.Screens.COMMENT_SCREEN
import com.samarth.memesmagic.util.Screens.LANDING_SCREEN
import com.samarth.memesmagic.util.Screens.SINGLE_POST_SCREEN
import com.samarth.memesmagic.util.TokenHandler.getEmail
import com.samarth.memesmagic.util.TokenHandler.logout
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
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


    LaunchedEffect(key1 = Unit) {

        /**
         *  GET FIREBASE MESSAGING TOKEN AND UPDATE IT ON SERVER IF IT IS NEW.
         */
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            val token = task.result ?: ""
            feedViewModel.updateFcmToken(
                token,
                context,
                onSuccess = {
//                    coroutineScope.launch {
//                        scaffoldState.snackbarHostState.showSnackbar("Fcm Token Updated!")
//                    }
                },
                onFail = { _ ->
//                    coroutineScope.launch {
//                        scaffoldState.snackbarHostState.showSnackbar("Fcm: $errorMsg")
//                    }
                }
            )
        }



        /**
         * GET CURRENT USER AND LOGOUT ON FAIL IF ERROR CODE IS NOT TIMEOUT
         */

        feedViewModel.getUser(
            getEmail(context)!!,
            onFail = {
                Log.d("MyLog","User ------------> $it")
                if(!it.contains("timeout")){
                    coroutineScope.launch{
                        logout(context)
                        feedViewModel.clearLocalData()
                        parentNavController.popBackStack()
                        parentNavController.navigate(LANDING_SCREEN)
                    }
                } else {
                    coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("Heroku Dyno is Down. Please Try Again. After 1-2 request it will Awake.")
                    }
                }

            },
            onSuccess = {}
        )


        /**
         *  GET REWARDS AND SHOW IN DIALOG BOXES IF IT IS NEW REWARD
         */

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


        /**
         * GET NEW FEED IF FEED SCREEN IS OPENED FIRST TIME
         */
        if(feedViewModel.firstTimeOpenedFeedScreen.value) {
            feedViewModel.getFeedFromGithub()
            feedViewModel.getFeed(
                onSuccess = {
                    feedViewModel.posts.value = feedViewModel.posts.value.shuffled()
                    feedViewModel.firstTimeOpenedFeedScreen.value = false
                },
                onFail = {
                    coroutineScope.launch {
                        Log.d("MyLog", "Feed ------------> $it")
                        scaffoldState.snackbarHostState.showSnackbar(it)
                    }
                }
            )
        }




    }


    /**
     *  MAIN SCREEN STARTS
     */
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        /**
         * REWARDS DIALOG BOXES
         */
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
                    parentNavController.navigate("rewards")
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
                          parentNavController.navigate("$ANOTHER_USER_PROFILE_SCREEN/${rewardWinnerInfo.email}")
                    },
                    isFollowingToUser = feedViewModel.isFollowingToRewardyy.value,
                    onFollowBtnPressed = {
                        feedViewModel.followUser()
                        showAdvertiseDialog = false
                    }
                )

            }

        }


        /**
         * ===================== FEED =================
         */
        val listState = remember {
            feedViewModel.lazyListState
        }

        feedViewModel.determineCurrentlyPlayingItem(
            listState,
            feedViewModel.posts.value
        )
        Log.d("current video","${feedViewModel.currentlyPlayingItem.value?.mediaLink}")
        UpdateCurrentlyPlayingItem(
            player = feedViewModel.player,
            post = feedViewModel.currentlyPlayingItem.value,
            dataSourceFactory = feedViewModel.dataSourceFactory
        )


        if(feedViewModel.isLoading.value){
            CircularProgressIndicator()
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState
        ) {

            itemsIndexed(feedViewModel.posts.value) { pos, post ->


//                    if(pos >= feedViewModel.posts.value.size - 10){
//                        feedViewModel.getFeedFromGithub()
//                    }

                val isPlayerViewVisible = feedViewModel.currentlyPlayingItem.value?.id == post.id && feedViewModel.player.isPlaying

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
                                    feedViewModel.dislikePost(post, onSuccess)
                                } else {
                                    feedViewModel.likePost(post, onSuccess)
                                }
                            }
                        },
                        onCommentIconPressed = {
                            CommentsUtil.post = it
                            parentNavController.navigate(COMMENT_SCREEN)
                        },
                        onShareIconPressed = {
                            feedViewModel.shareImage(
                                context,
                                it.mediaLink,
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
                        },
                        modifier = Modifier.clickable {
                            CommentsUtil.post = post
                            parentNavController.navigate(SINGLE_POST_SCREEN)
                        },
                        exoPlayer = feedViewModel.player,
                        playerViewVisible = isPlayerViewVisible,
                        thumbnail = feedViewModel.thumbnails[post.id]
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