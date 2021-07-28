package com.samarth.memesmagic.ui.screens.another_user_profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatRoom
import com.samarth.memesmagic.ui.screens.another_user_profile.AnotherUserProfileViewModel
import com.samarth.memesmagic.ui.components.CustomTopBar
import com.samarth.memesmagic.ui.components.FullProfileScreen
import com.samarth.memesmagic.ui.components.RewardsDialogBox
import com.samarth.memesmagic.util.ChatUtils
import com.samarth.memesmagic.util.Screens
import com.samarth.memesmagic.util.Screens.CHAT_ROOM_SCREEN
import com.samarth.memesmagic.util.Screens.SINGLE_POST_SCREEN
import com.samarth.memesmagic.util.TokenHandler.getEmail
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@Composable
fun AnotherUserProfile(
    navController: NavController,
    userEmail:String,
    anotherUserProfileViewModel: AnotherUserProfileViewModel = hiltViewModel()
) {

    val user by remember {
        anotherUserProfileViewModel.user
    }
    var isBadgesVisible by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CustomTopBar(title = user?.userInfo?.name ?: "")
        }
    ) {

        LaunchedEffect(key1 = Unit) {
            anotherUserProfileViewModel.getUser(getEmail(context)!!,userEmail)
            anotherUserProfileViewModel.getPosts(userEmail)
        }


        Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center) {

            FullProfileScreen(
                modifier = Modifier.fillMaxSize(),
                user = anotherUserProfileViewModel.user.value,
                posts = anotherUserProfileViewModel.posts.value,
                isLoading = anotherUserProfileViewModel.isLoading.value,
                loadError = anotherUserProfileViewModel.loadError.value,
                onRetry = {
                      coroutineScope.launch {
                          anotherUserProfileViewModel.getUser(getEmail(context)!!, userEmail)
                          anotherUserProfileViewModel.getPosts(userEmail)
                      }
                },
                isItAnotherUserProfile = true,
                isFollowing = anotherUserProfileViewModel.isFollowing.value,
                onEditScreenPressed = {},
                onFollowUnFollowBtnPressed = { onSuccess ->
                    coroutineScope.launch {
                        anotherUserProfileViewModel.followUnfollowToggle(
                            getEmail(context)!!,
                            onSuccess
                        )
                    }
                },
                detailView = {
                    navController.navigate(SINGLE_POST_SCREEN)
                },
                badgesClick = {
                    anotherUserProfileViewModel.getRewards()
                    isBadgesVisible = true
                },
                messageUser = {
                    ChatUtils.currentChatRoom = PrivateChatRoom(
                        userEmail = anotherUserProfileViewModel.user.value?.userInfo?.email ?: "",
                        name = anotherUserProfileViewModel.user.value?.userInfo?.name ?: "",
                        profilePic = anotherUserProfileViewModel.user.value?.userInfo?.profilePic,
                    )
                    navController.navigate(CHAT_ROOM_SCREEN)
                }
            )

            if(isBadgesVisible) {
                RewardsDialogBox(
                    rewards = anotherUserProfileViewModel.rewards.value,
                    onDismiss = {
                        isBadgesVisible = false
                    },
                    isLoading = anotherUserProfileViewModel.isLoadingRewards.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f)
                )
            }

        }

    }

}