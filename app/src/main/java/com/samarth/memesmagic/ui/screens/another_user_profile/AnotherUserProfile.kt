package com.samarth.memesmagic.ui.screens.another_user_profile

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatRoom
import com.samarth.memesmagic.ui.components.CustomTopBar
import com.samarth.memesmagic.ui.components.FullProfileScreen
import com.samarth.memesmagic.util.ChatUtils
import com.samarth.memesmagic.util.Screens
import com.samarth.memesmagic.util.Screens.ANOTHER_USER_PROFILE_SCREEN
import com.samarth.memesmagic.util.Screens.CHAT_ROOM_SCREEN
import com.samarth.memesmagic.util.Screens.SINGLE_POST_SCREEN
import com.samarth.memesmagic.util.TokenHandler.getEmail
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
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
    val curUser by remember {
        anotherUserProfileViewModel.curUser
    }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomTopBar(title = user?.userInfo?.name ?: "")
        }
    ) {

        LaunchedEffect(key1 = Unit) {
            startUp(
                otherUserEmail = userEmail,
                anotherUserProfileViewModel = anotherUserProfileViewModel,
                context = context
            )
        }


        Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center) {

            FullProfileScreen(
                modifier = Modifier.fillMaxSize(),
                user = anotherUserProfileViewModel.user.value,
                currentLoggedInUser = curUser,
                posts = anotherUserProfileViewModel.posts.value,
                isLoading = anotherUserProfileViewModel.isLoading.value,
                loadError = anotherUserProfileViewModel.loadError.value,
                onRetry = {
                      coroutineScope.launch {
                          startUp(
                              otherUserEmail = userEmail,
                              anotherUserProfileViewModel = anotherUserProfileViewModel,
                              context = context
                          )
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
                messageUser = {
                    ChatUtils.currentChatRoom = PrivateChatRoom(
                        userEmail = anotherUserProfileViewModel.user.value?.userInfo?.email ?: "",
                        name = anotherUserProfileViewModel.user.value?.userInfo?.name ?: "",
                        profilePic = anotherUserProfileViewModel.user.value?.userInfo?.profilePic,
                    )
                    navController.navigate(CHAT_ROOM_SCREEN)
                },
                followUser = { email, onSuccess, onFail->
                    anotherUserProfileViewModel.followUser(email,onSuccess,onFail)
                },
                unFollowUser = { email, onSuccess, onFail->
                    anotherUserProfileViewModel.unFollowUser(email,onSuccess,onFail)
                },
                scaffoldState = scaffoldState,
                navigateToAnotherUserProfile = { email ->
                    navController.navigate("$ANOTHER_USER_PROFILE_SCREEN/$email")
                },
                navController = navController
            )

        }

    }

}

private suspend fun startUp(
    otherUserEmail:String,
    anotherUserProfileViewModel: AnotherUserProfileViewModel,
    context: Context
){
    val curUserEmail = getEmail(context)!!
    anotherUserProfileViewModel.getUser(otherUserEmail){
        anotherUserProfileViewModel.user.value = it
        anotherUserProfileViewModel.isFollowing(curUserEmail)
    }
    anotherUserProfileViewModel.getPosts(otherUserEmail)
    anotherUserProfileViewModel.getUser(curUserEmail){
        anotherUserProfileViewModel.curUser.value = it
    }
}