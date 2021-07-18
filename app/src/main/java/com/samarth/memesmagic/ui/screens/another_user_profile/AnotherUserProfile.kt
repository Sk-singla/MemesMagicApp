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
import com.samarth.memesmagic.ui.screens.another_user_profile.AnotherUserProfileViewModel
import com.samarth.memesmagic.ui.components.CustomTopBar
import com.samarth.memesmagic.ui.components.FullProfileScreen
import com.samarth.memesmagic.ui.components.RewardsDialogBox
import com.samarth.memesmagic.util.Screens.SINGLE_POST_SCREEN

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

    Scaffold(
        topBar = {
            CustomTopBar(title = user?.userInfo?.name ?: "")
        }
    ) {

        val context = LocalContext.current

        DisposableEffect(key1 = Unit) {
            anotherUserProfileViewModel.getUser(context,userEmail)
            anotherUserProfileViewModel.getPosts(userEmail)
            onDispose {  }
        }


        Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center) {

            FullProfileScreen(
                modifier = Modifier.fillMaxSize(),
                user = anotherUserProfileViewModel.user.value,
                posts = anotherUserProfileViewModel.posts.value,
                isLoading = anotherUserProfileViewModel.isLoading.value,
                loadError = anotherUserProfileViewModel.loadError.value,
                onRetry = {
                    anotherUserProfileViewModel.getUser(context, userEmail)
                    anotherUserProfileViewModel.getPosts(userEmail)
                },
                isItAnotherUserProfile = true,
                isFollowing = anotherUserProfileViewModel.isFollowing.value,
                onEditScreenPressed = {

                },
                onFollowUnFollowBtnPressed = { onSuccess ->
                    anotherUserProfileViewModel.followUnfollowToggle(context, onSuccess)
                },
                detailView = {
                    navController.navigate(SINGLE_POST_SCREEN)
                },
                badgesClick = {
                    anotherUserProfileViewModel.getRewards()
                    isBadgesVisible = true
                }
            )

            if(isBadgesVisible) {
                RewardsDialogBox(
                    rewards = anotherUserProfileViewModel.rewards.value,
                    onDismiss = {
                        isBadgesVisible = false
                    },
                    isLoading = anotherUserProfileViewModel.isLoadingRewards.value,
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.4f)
                )
            }

        }

    }

}