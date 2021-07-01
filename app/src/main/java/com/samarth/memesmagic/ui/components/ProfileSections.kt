package com.samarth.memesmagic.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import com.google.accompanist.coil.rememberCoilPainter
import com.samarth.memesmagic.R
import com.samarth.memesmagic.data.remote.response.Post
import com.samarth.memesmagic.data.remote.response.User
import com.samarth.memesmagic.ui.theme.Green700
import com.samarth.memesmagic.util.CommentsUtil
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.numberOfFollowersOrFollowings
import com.samarth.memesmagic.util.numberOfPosts
import kotlinx.coroutines.launch


@ExperimentalFoundationApi
@Composable
fun FullProfileScreen(
    modifier:Modifier = Modifier,
    user:User?= null,
    posts:List<Post>? = null,
    isLoading:Boolean = false,
    loadError:String = "",
    onRetry:()->Unit,
    isItAnotherUserProfile: Boolean,
    isFollowing: Boolean,
    onEditScreenPressed: () -> Unit,
    onFollowUnFollowBtnPressed: (onSuccess: () -> Unit) -> Unit,
    detailView : ()->Unit,
    onLogout: () -> Unit = {}
) {


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

                user?.let {
                    ProfileTopSection(
                        user = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    )

                    Divider(modifier = Modifier.padding(horizontal = 12.dp))

                    ProfileScreenButtons(
                        isItAnotherUserProfile = isItAnotherUserProfile,
                        onFollowUnFollowBtnPressed = onFollowUnFollowBtnPressed,
                        isFollowing = isFollowing,
                        onEditScreenPressed = onEditScreenPressed,
                        onLogout = onLogout,
                        numberOfRewards = it.rewards.size
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
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            ProfilePostsSection(
                                posts = postsList,
                                modifier = Modifier.padding(horizontal = 4.dp),
                                detailView = detailView
                            )
                        }

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
) {
    Column(modifier = modifier) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){

            Image(
                painter = rememberCoilPainter(
                    request = ImageRequest.Builder(LocalContext.current)
                        .data(user.userInfo.profilePic ?: R.drawable.ic_person)
                        .placeholder(R.drawable.ic_person)
                        .error(R.drawable.ic_person)
                        .build(),
                    fadeIn = true,
                ),
                contentScale = ContentScale.Crop,
                contentDescription = "User Image",
                modifier = Modifier
                    .padding(end = 8.dp, bottom = 8.dp)
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.8f),
                        shape = CircleShape
                    )
//                    .background(color = MaterialTheme.colors.onBackground.copy(alpha = 0.85f)),
            )


            listOf(
                Pair(numberOfPosts(user.postCount),"Posts"),
                Pair(numberOfFollowersOrFollowings(user.followers.size),"Followers"),
                Pair(numberOfFollowersOrFollowings(user.followings.size),"Followings")
            ).forEach{ item ->

                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Text(
                        text = item.first,
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.W500
                    )
                    Text(
                        text = item.second,
                        style = MaterialTheme.typography.body2
                    )
                }

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
    numberOfRewards:Int
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
                .padding(horizontal = 8.dp)
                .shadow(2.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = Green700
            )
        ) {
            Text(
                text = if (isItAnotherUserProfile && isFollowingToUser)
                    "Unfollow" else if (isItAnotherUserProfile && !isFollowingToUser)
                    "Follow" else "Edit Profile"
            )
        }

        OutlinedButton(
                onClick = {
                    if(!isItAnotherUserProfile){
                        onLogout()
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .shadow(2.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = Green700
                ),
            enabled = !isItAnotherUserProfile
        ) {
            Text(
                text = if (isItAnotherUserProfile)
                    "$numberOfRewards Badges"
                else "Logout"
            )
        }


    }

}


@ExperimentalFoundationApi
@Composable
fun ProfilePostsSection(posts:List<Post>, modifier: Modifier = Modifier,detailView: () -> Unit) {

    LazyVerticalGrid(cells = GridCells.Fixed(3),modifier = modifier,contentPadding = PaddingValues(top = 8.dp,bottom = 60.dp)) {

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


