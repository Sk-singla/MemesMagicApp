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
import coil.request.ImageRequest
import com.google.accompanist.coil.rememberCoilPainter
import com.samarth.memesmagic.R
import com.samarth.memesmagic.data.remote.response.Post
import com.samarth.memesmagic.data.remote.response.User
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.ui.components.CustomTopBar
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.TokenHandler.getEmail
import com.samarth.memesmagic.util.numberOfFollowersOrFollowings
import com.samarth.memesmagic.util.numberOfPosts
import kotlinx.coroutines.launch
import java.lang.Error

@ExperimentalFoundationApi
@Composable
fun ProfileScreen(
    email:String?=null,
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {


    val context = LocalContext.current
    profileViewModel.getUser(email,context)
    profileViewModel.getPosts(email,context)

    val scaffoldState = rememberScaffoldState()
     val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
    ){
        // todo -> complete profile -> then meme part start

        Box(modifier = Modifier.fillMaxSize()){

            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
    //                .fillMaxHeight(0.5f)
                    , contentAlignment = Alignment.Center
                ) {
                    when (profileViewModel.userStatus.value) {
                        is Resource.Success -> {
                            ProfileTopSection(
                                user = profileViewModel.userStatus.value.data!!,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }

                        is Resource.Error -> {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(profileViewModel.userStatus.value.message!!)
                            }
                        }

                        is Resource.Loading -> {
                            CircularProgressIndicator()
                        }

                        else -> {

                        }
                    }

                }

                Divider(modifier=Modifier.padding(horizontal = 12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    when (profileViewModel.postStatus.value) {
                        is Resource.Success -> {

                            profileViewModel.postStatus.value.data?.let { posts ->
                                if (posts.isEmpty()) {
                                    Text(
                                        text = "No Post to Show!",
                                        style = MaterialTheme.typography.h5,
                                        modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                                        textAlign = TextAlign.Center
                                    )
                                } else {
                                    ProfilePostsSection(
                                        posts = posts,
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )
                                }

                            }

                        }

                        is Resource.Error -> {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState
                                    .showSnackbar(
                                        profileViewModel.userStatus.value.message ?: "Error!!"
                                    )
                            }
                        }

                        is Resource.Loading -> {
                            CircularProgressIndicator()
                        }

                        else -> {

                        }
                    }


                }
            }


            SnackbarHost(
                hostState = scaffoldState.snackbarHostState,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            ){
                Snackbar(
                    snackbarData = it,
                    modifier = Modifier.padding(bottom = 32.dp),
                )

            }
        }

    }

}


@Composable
fun ProfileTopSection(
    user:User,
    modifier: Modifier = Modifier
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
                        width = 2.dp,
                        color = MaterialTheme.colors.onBackground.copy(alpha=0.8f),
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


@ExperimentalFoundationApi
@Composable
fun ProfilePostsSection(posts:List<Post>,modifier: Modifier = Modifier) {

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
                    .aspectRatio(1f),
            )

        }

    }

}












