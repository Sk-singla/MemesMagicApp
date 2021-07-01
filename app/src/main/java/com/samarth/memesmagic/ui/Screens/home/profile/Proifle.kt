package com.samarth.memesmagic.ui.Screens.home.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.samarth.memesmagic.ui.components.FullProfileScreen
import com.samarth.memesmagic.util.Screens
import com.samarth.memesmagic.util.Screens.EDIT_PROFILE_SCREEN
import com.samarth.memesmagic.util.Screens.LANDING_SCREEN

@ExperimentalFoundationApi
@Composable
fun ProfileScreen(
    parentNavController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    DisposableEffect(key1 = Unit) {
        profileViewModel.getUser(context)
        profileViewModel.getPosts(context)
        onDispose {  }
    }


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
        onFollowUnFollowBtnPressed = {

        },
        detailView  = {
            parentNavController.navigate(Screens.SINGLE_POST_SCREEN)
        },
        onLogout = {
            profileViewModel.logoutUser(context)
            parentNavController.popBackStack()
            parentNavController.navigate(LANDING_SCREEN)
        }

    )
}










