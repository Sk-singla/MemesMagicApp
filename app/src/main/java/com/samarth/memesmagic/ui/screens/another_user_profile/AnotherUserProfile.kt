package com.samarth.memesmagic.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.samarth.memesmagic.ui.screens.another_user_profile.AnotherUserProfileViewModel
import com.samarth.memesmagic.ui.components.CustomTopBar
import com.samarth.memesmagic.ui.components.FullProfileScreen

@ExperimentalFoundationApi
@Composable
fun AnotherUserProfile(
    userEmail:String,
    anotherUserProfileViewModel: AnotherUserProfileViewModel = hiltViewModel()
) {

    val user by remember {
        anotherUserProfileViewModel.user
    }

    Scaffold(
        topBar = {
            CustomTopBar(title = user?.userInfo?.name ?: "")
        }
    ) {

        val context = LocalContext.current

        DisposableEffect(key1 = Unit) {
            anotherUserProfileViewModel.getUser(context,userEmail)
            anotherUserProfileViewModel.getPosts(context,userEmail)
            onDispose {  }
        }


        FullProfileScreen(
            modifier = Modifier.fillMaxSize(),
            user = anotherUserProfileViewModel.user.value,
            posts = anotherUserProfileViewModel.posts.value,
            isLoading = anotherUserProfileViewModel.isLoading.value,
            loadError = anotherUserProfileViewModel.loadError.value,
            onRetry = {
                anotherUserProfileViewModel.getUser(context,userEmail)
                anotherUserProfileViewModel.getPosts(context,userEmail)
            },
            isItAnotherUserProfile = true,
            isFollowing = anotherUserProfileViewModel.isFollowing.value,
            onEditScreenPressed = {

            },
            onFollowUnFollowBtnPressed = { onSuccess ->
                anotherUserProfileViewModel.followUnfollowToggle(context,onSuccess)
            },
            detailView = {

            }
        )

    }

}