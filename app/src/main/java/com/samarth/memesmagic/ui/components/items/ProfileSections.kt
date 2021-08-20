package com.samarth.memesmagic.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import com.google.accompanist.coil.rememberCoilPainter
import com.samarth.memesmagic.R
import com.samarth.memesmagic.data.remote.response.Post
import com.samarth.memesmagic.data.remote.response.User
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.ui.theme.Green700
import com.samarth.memesmagic.util.CommentsUtil
import com.samarth.memesmagic.util.numberOfFollowersOrFollowings
import com.samarth.memesmagic.util.numberOfPosts
import kotlinx.coroutines.launch


@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun FullProfileScreen(
    modifier:Modifier = Modifier,
    user:User?= null,
    currentUser:User?= null,
    posts:List<Post>? = null,
    isLoading:Boolean = false,
    loadError:String = "",
    onRetry:()->Unit,
    isItAnotherUserProfile: Boolean,
    isFollowing: Boolean,
    onEditScreenPressed: () -> Unit,
    onFollowUnFollowBtnPressed: (onSuccess: () -> Unit) -> Unit,
    detailView : ()->Unit,
    onLogout: () -> Unit = {},
    messageUser: () -> Unit = {},
    followUser:( (
        emailOfUserToFollow:String,
        onSuccess :(UserInfo)->Unit,
        onFail:(String)->Unit
    )->Unit)? = null,
    unFollowUser:( (
        emailOfUserToUnFollow:String,
        onSuccess :()->Unit,
        onFail:(String)->Unit
    )->Unit)? = null,
    scaffoldState:ScaffoldState = rememberScaffoldState(),
    navigateToAnotherUserProfile:(String)->Unit
) {


    var isBadgesVisible by remember {
        mutableStateOf(false)
    }

    var isFollowersOrFollowingVisible by remember {
        mutableStateOf(false)
    }
    var otherUsers by remember {
        mutableStateOf(listOf<UserInfo>())
    }

    val coroutineScope = rememberCoroutineScope()


    Column(modifier = modifier) {


        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            loadError.isNotEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    RetryView(loadError = loadError, onRetry = onRetry)
                }
            }
            else -> {

                user?.let { curUser ->
                    ProfileTopSection(
                        user = curUser,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        showFollowers = {
                            otherUsers = curUser.followers
                            isFollowersOrFollowingVisible = true
                        },
                        showFollowings = {
                            otherUsers = curUser.followings
                            isFollowersOrFollowingVisible = true
                        }
                    )

                    Divider(modifier = Modifier.padding(horizontal = 12.dp))

                    ProfileScreenButtons(
                        isItAnotherUserProfile = isItAnotherUserProfile,
                        onFollowUnFollowBtnPressed = onFollowUnFollowBtnPressed,
                        isFollowing = isFollowing,
                        onEditScreenPressed = onEditScreenPressed,
                        onLogout = onLogout,
                        badgesClick = {
                              isBadgesVisible = true
                        },
                        numberOfRewards = curUser.rewards.size,
                        messageUser = messageUser
                    )

                    Divider(modifier = Modifier.padding(horizontal = 12.dp))

                    posts?.let { postsList ->
                        if (postsList.isEmpty()) {

                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No Post to Show!",
                                    style = MaterialTheme.typography.h5,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            ProfilePostsSection(
                                posts = postsList.sortedByDescending { it.time },
                                detailView = detailView
                            )
                        }

                    }












                    AnimatedVisibility(
                        isBadgesVisible,
                        enter = expandIn(
                            expandFrom = Alignment.Center,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                visibilityThreshold = IntSize.VisibilityThreshold
                            )
                        ),
                        exit = shrinkOut(
                            shrinkTowards = Alignment.Center
                        )

                    ) {
                        RewardsDialogBox(
                            rewards = curUser.rewards,
                            onDismiss = {
                                isBadgesVisible = false
                            },
                            isLoading = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.4f)
                        )
                    }





                    AnimatedVisibility(
                        isFollowersOrFollowingVisible,
                        enter = expandIn(
                            expandFrom = Alignment.Center,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                visibilityThreshold = IntSize.VisibilityThreshold
                            )
                        ),
                        exit = shrinkOut(
                            shrinkTowards = Alignment.Center
                        )

                    ) {
                        FollowersOrFollowingDialogBox(
                            users = otherUsers,
                            onDismiss = {
                                isFollowersOrFollowingVisible = false
                            },
                            isLoading = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.6f),
                            isFollowingToUser = { followerEmail ->
                                if(isItAnotherUserProfile) {
                                    currentUser?.followings?.find { it.email == followerEmail } != null
                                } else {
                                    curUser.followings.find {it.email == followerEmail } != null
                                }
                            },

                            followUser = { emailOfUserToFollow: String, onSuccess: () -> Unit ->

                                followUser?.invoke(
                                    emailOfUserToFollow,
                                    { userInfo ->
                                        onSuccess()
                                        if(!isItAnotherUserProfile && curUser.followings.find { it.email == emailOfUserToFollow } == null){
                                            curUser.followings.add(userInfo)
                                        } else if(isItAnotherUserProfile){
                                            currentUser?.followings?.add(userInfo)
                                        }
                                        coroutineScope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                "Successfully Followed to ${userInfo.name}",
                                            )
                                        }
                                    },
                                    { errorMessage ->
                                        coroutineScope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                errorMessage,
                                            )
                                        }
                                    }
                                )
                            },
                            unFollowUser = { emailOfUserToUnFollow, onSuccess ->

                                unFollowUser?.invoke(
                                    emailOfUserToUnFollow,
                                    {


                                        onSuccess()
                                        curUser.followings.find { it.email == emailOfUserToUnFollow }
                                            ?.let {
                                                if(!isItAnotherUserProfile){
                                                    otherUsers -= it
                                                    curUser.followings.remove(it)
                                                } else {
                                                    currentUser?.followings?.remove(it)
                                                }
                                            }
                                        coroutineScope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                "Successfully Unfollowed!",
                                            )
                                        }
                                    },
                                    { errorMessage ->
                                        coroutineScope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                errorMessage,
                                            )
                                        }
                                    }
                                )
                            },
                            onClick = {
                                navigateToAnotherUserProfile(it)
                            }
                        )

                    }

                }





            }
        }
    }
}



@Composable
fun ProfileTopSection(
    user: User,
    modifier: Modifier = Modifier,
    showFollowers: ()->Unit,
    showFollowings: ()->Unit
) {
    Column(modifier = modifier) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){

            ProfileImage(
                name = user.userInfo.name,
                imageUrl = user.userInfo.profilePic,
                modifier = Modifier
                    .padding(end = 8.dp, bottom = 8.dp)
                    .size(80.dp),
                fontStyle = MaterialTheme.typography.h4
            )


            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = numberOfPosts(user.postCount),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Posts",
                    style = MaterialTheme.typography.body2
                )
            }

            Column(
                modifier = Modifier
                    .background(shape = CircleShape, color = MaterialTheme.colors.surface)
                    .clickable {
                        showFollowers()
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ){
                Text(
                    text = numberOfFollowersOrFollowings(user.followers.size),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Followers",
                    style = MaterialTheme.typography.body2
                )
            }

            Column(
                modifier = Modifier.clickable {
                      showFollowings()
                },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = numberOfFollowersOrFollowings(user.followings.size),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Followings",
                    style = MaterialTheme.typography.body2
                )
            }


        }


        Text(text = user.userInfo.name,style = MaterialTheme.typography.body1,fontWeight = FontWeight.Bold)
        user.userInfo.bio?.let {  bio ->
            Text(text = bio,style = MaterialTheme.typography.body2,modifier = Modifier.fillMaxWidth())
        }


    }

}

@Composable
fun ProfileScreenButtons(
    isItAnotherUserProfile: Boolean,
    onFollowUnFollowBtnPressed: (onSuccess: () -> Unit) -> Unit,
    isFollowing: Boolean,
    onEditScreenPressed: () -> Unit,
    onLogout: () -> Unit,
    badgesClick: () -> Unit,
    numberOfRewards:Int,
    messageUser: ()->Unit,
) {

    var isFollowingToUser by remember {
        mutableStateOf(isFollowing)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ){
        OutlinedButton(
            onClick = {
                if (isItAnotherUserProfile) {
                    onFollowUnFollowBtnPressed {
                        isFollowingToUser = !isFollowingToUser
                    }
                } else {
                    onEditScreenPressed()
                }
            },
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .shadow(2.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = Green700
            )
        ) {
            Text(
                text = if (isItAnotherUserProfile && isFollowingToUser)
                    "Unfollow" else if (isItAnotherUserProfile && !isFollowingToUser)
                    " Follow  " else "Edit Profile"
            )
        }

        if(isItAnotherUserProfile){
            OutlinedButton(
                onClick = {
                      messageUser()
                },
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .shadow(2.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = MaterialTheme.colors.surface,
                    contentColor = Green700
                )
            ) {
                Text(
                    text = "Message"
                )
            }
        }


        OutlinedButton(
                onClick = {
                    if(isItAnotherUserProfile){
                        badgesClick()
                    } else {
                        onLogout()
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .shadow(2.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = Green700
                )
        ) {
            Text(
                text = if (isItAnotherUserProfile)
                    "$numberOfRewards Badge${if(numberOfRewards>1) "s" else ""}"
                else "   Logout   ",
                textAlign = TextAlign.Center
            )
        }


    }

}

@ExperimentalFoundationApi
@Composable
fun ProfilePostsSection(posts:List<Post>,detailView: () -> Unit) {

    LazyVerticalGrid(cells = GridCells.Fixed(3),contentPadding = PaddingValues(top = 8.dp,bottom = 60.dp,start = 16.dp,end = 16.dp)) {

        items(posts){ post ->

            Image(
                painter = rememberCoilPainter(
                    request = ImageRequest.Builder(LocalContext.current)
                        .data(post.mediaLink)
                        .placeholder(R.drawable.blank_image)
                        .error(R.drawable.blank_image)
                        .build(),
                    fadeIn = true,
                ),
                contentScale = ContentScale.Crop,
                contentDescription = "User Image",
                modifier = Modifier
                    .padding(1.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clickable {
                        CommentsUtil.post = post
                        detailView()
                    },
            )

        }

    }

}