package com.samarth.memesmagic.ui.screens.home.profile

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.samarth.memesmagic.ui.components.FullProfileScreen
import com.samarth.memesmagic.util.Screens
import com.samarth.memesmagic.util.Screens.ANOTHER_USER_PROFILE_SCREEN
import com.samarth.memesmagic.util.Screens.EDIT_PROFILE_SCREEN
import com.samarth.memesmagic.util.Screens.LANDING_SCREEN

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun ProfileScreen(
    parentNavController: NavController,
    parentScaffoldState: ScaffoldState,
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    DisposableEffect(key1 = Unit) {
        profileViewModel.getUser(context)
        profileViewModel.getPosts(context)
        onDispose {  }
    }


    // todo: change badges things ( show it in profile ) and on rewards tab add notifications tab
    FullProfileScreen(
        modifier = Modifier.fillMaxSize(),
        user = profileViewModel.user.value,
        posts = profileViewModel.posts.value,
        isLoading = profileViewModel.isLoading.value,
        loadError = profileViewModel.loadError.value,
        onRetry = {
            profileViewModel.getUser(context)
            profileViewModel.getPosts(context)
        },
        isItAnotherUserProfile = false,
        isFollowing = false,
        onEditScreenPressed = {
            parentNavController.navigate(EDIT_PROFILE_SCREEN)
        },
        onFollowUnFollowBtnPressed = {},
        detailView  = {
            parentNavController.navigate("${Screens.SINGLE_POST_SCREEN}/$it")
        },
        onLogout = {
            profileViewModel.logoutUser(context)
            parentNavController.popBackStack()
            parentNavController.navigate(LANDING_SCREEN)
        },
        followUser = { email, onSuccess, onFail->
            profileViewModel.followUser(email,onSuccess,onFail)
        },
        unFollowUser = { email, onSuccess, onFail->
            profileViewModel.unFollowUser(email,onSuccess,onFail)
        },
        scaffoldState = parentScaffoldState,
        navigateToAnotherUserProfile = { email ->
            parentNavController.navigate("$ANOTHER_USER_PROFILE_SCREEN/$email")
        }
    )
}










