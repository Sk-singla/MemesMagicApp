package com.samarth.memesmagic.ui.Screens.home.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.request.ImageRequest
import com.google.accompanist.coil.rememberCoilPainter
import com.samarth.memesmagic.R
import com.samarth.memesmagic.data.remote.response.Post
import com.samarth.memesmagic.data.remote.response.User
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.ui.components.CustomTopBar
import com.samarth.memesmagic.ui.components.FullProfileScreen
import com.samarth.memesmagic.ui.components.ProfilePostsSection
import com.samarth.memesmagic.ui.components.ProfileTopSection
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.Screens.EDIT_PROFILE_SCREEN
import com.samarth.memesmagic.util.TokenHandler.getEmail
import com.samarth.memesmagic.util.numberOfFollowersOrFollowings
import com.samarth.memesmagic.util.numberOfPosts
import kotlinx.coroutines.launch
import java.lang.Error

@ExperimentalFoundationApi
@Composable
fun ProfileScreen(
    navController: NavController,
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
            navController.navigate(EDIT_PROFILE_SCREEN)
        },
        onFollowUnFollowBtnPressed = {

        }
    )
}










