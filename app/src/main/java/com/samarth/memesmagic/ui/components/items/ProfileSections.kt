package com.samarth.memesmagic.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.request.ImageRequest
import coil.request.videoFrameMillis
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.accompanist.coil.rememberCoilPainter
import com.samarth.memesmagic.R
import com.samarth.memesmagic.data.remote.models.PostType
import com.samarth.memesmagic.data.remote.response.Post
import com.samarth.memesmagic.data.remote.response.User
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.ui.theme.Green700
import com.samarth.memesmagic.util.CommentsUtil
import com.samarth.memesmagic.util.numberOfFollowersOrFollowings
import com.samarth.memesmagic.util.numberOfPosts
import kotlinx.coroutines.launch
import java.lang.Exception


@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun FullProfileScreen(
    modifier:Modifier = Modifier,
    user:User?= null,
    currentLoggedInUser:User?= null,
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
    navigateToAnotherUserProfile:(String)->Unit,
    navController:NavController
) {


    var isBadgesVisible by remember {
        mutableStateOf(false)
    }

    var isFollowersVisible by remember {
        mutableStateOf(false)
    }

    var isFollowingsVisible by remember {
        mutableStateOf(false)
    }
    var otherUsers by remember {
        mutableStateOf(listOf<UserInfo>())
    }
    val thumbnails = remember {
        mutableStateMapOf<String,Bitmap>()
    }
    val context = LocalContext.current
    val glide = remember {
        Glide.with(context)
    }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(thumbnails,posts){
        posts?.filter {
            it.postType == PostType.VIDEO
        }?.forEach { curPost ->
            glide.asBitmap()
                .load(curPost.mediaLink)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        thumbnails[curPost.id] = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) =Unit
                })

        }
    }


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
                            isFollowersVisible = true
                        },
                        showFollowings = {
                            otherUsers = curUser.followings
                            isFollowingsVisible = true
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
                              navController.navigate("rewards?userEmail=${user.userInfo.email}")
//                              isBadgesVisible = true
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
                                detailView = detailView,
                                thumbnails = thumbnails
                            )
                        }

                    }












//                    AnimatedVisibility(
//                        isBadgesVisible,
//                        enter = expandIn(
//                            expandFrom = Alignment.Center,
//                            animationSpec = spring(
//                                dampingRatio = Spring.DampingRatioMediumBouncy,
//                                visibilityThreshold = IntSize.VisibilityThreshold
//                            )
//                        ),
//                        exit = shrinkOut(
//                            shrinkTowards = Alignment.Center
//                        )
//
//                    ) {
//                        RewardsDialogBox(
//                            rewards = curUser.rewards,
//                            onDismiss = {
//                                isBadgesVisible = false
//                            },
//                            isLoading = false,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .fillMaxHeight(0.4f)
//                        )
//                    }





                    AnimatedVisibility (
                        isFollowersVisible || isFollowingsVisible,
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
                        FollowersOrFollowingDialogBox (
                            users = otherUsers,
                            curLoggedInUserEmail = currentLoggedInUser?.userInfo?.email ?: "",
                            onDismiss = {
                                isFollowersVisible = false
                                isFollowingsVisible = false
                            },
                            isLoading = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.6f),
                            isFollowingToUser = { followerEmail ->
                                if(isItAnotherUserProfile) {
                                    currentLoggedInUser?.followings?.find { it.email == followerEmail } != null
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
                                            currentLoggedInUser?.followings?.add(userInfo)
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
                                                    if(isFollowingsVisible)  otherUsers -= it
                                                    curUser.followings.remove(it)
                                                } else {
                                                    currentLoggedInUser?.followings?.remove(it)
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

@ExperimentalAnimationApi
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
    var isAllButtonsVisible by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
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
                    .weight(5f)
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

            OutlinedButton(
                onClick = {
                    isAllButtonsVisible = ! isAllButtonsVisible
                },
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .weight(1f)
                    .shadow(2.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = MaterialTheme.colors.surface,
                    contentColor = Green700
                )
            ) {
                Icon(
                    imageVector = if(isAllButtonsVisible){
                        Icons.Default.ArrowUpward
                    } else {
                        Icons.Default.ArrowDownward
                    },
                    contentDescription = "Show/Hide All buttons",
                    modifier = Modifier.size(20.dp)
                )
            }
        }


        AnimatedVisibility(
            isAllButtonsVisible,
            enter = fadeIn() + slideInVertically(),
            exit = slideOutVertically() + fadeOut()
        ){

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(isItAnotherUserProfile){
                    OutlinedButton(
                        onClick = {
                            messageUser()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, start = 4.dp, end = 4.dp)
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
                        badgesClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 4.dp, end = 4.dp)
                        .shadow(2.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = MaterialTheme.colors.surface,
                        contentColor = Green700
                    )
                ) {
                    Text(
                        text = "$numberOfRewards Badge${if(numberOfRewards>1) "s" else ""}",
                        textAlign = TextAlign.Center
                    )
                }

                if(!isItAnotherUserProfile){
                    OutlinedButton(
                        onClick = {
                            onLogout()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, start = 4.dp, end = 4.dp)
                            .shadow(2.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = MaterialTheme.colors.surface,
                            contentColor = Green700
                        )
                    ) {
                        Text(
                            text = "   Logout   ",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

        }







    }

}

@ExperimentalFoundationApi
@Composable
fun ProfilePostsSection(
    posts:List<Post>,
    thumbnails:Map<String,Bitmap>,
    detailView: () -> Unit
) {

    val context = LocalContext.current
    LazyVerticalGrid(cells = GridCells.Fixed(3),contentPadding = PaddingValues(top = 8.dp,bottom = 60.dp,start = 16.dp,end = 16.dp)) {

        items(posts){ post ->
            if(post.postType == PostType.IMAGE){
                Image(
                    painter = rememberCoilPainter(
                        request = ImageRequest.Builder(context)
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
            } else {

                Box(
                    modifier =  Modifier
                        .padding(1.dp)
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(color= Color.LightGray)
                        .clickable {
                            CommentsUtil.post = post
                            detailView()
                        },
                    contentAlignment = Alignment.Center
                ) {



                    if(thumbnails[post.id] != null){
                        Image(
                            bitmap = thumbnails[post.id]!!.asImageBitmap(),
                            contentScale = ContentScale.Crop,
                            contentDescription = "User Image",
                            modifier = Modifier
                                .fillMaxSize(),
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.ic_image),
                            contentScale = ContentScale.Crop,
                            contentDescription = "User Image",
                            modifier = Modifier
                                .fillMaxSize(),
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Video Icon"
                    )
                }
            }

        }

    }

}
