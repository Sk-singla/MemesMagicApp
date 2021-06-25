package com.samarth.memesmagic.ui.Screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.samarth.memesmagic.ui.Screens.home.feed.FeedViewModel
import com.samarth.memesmagic.ui.components.PostItem
import com.samarth.memesmagic.ui.theme.Gray500
import com.samarth.memesmagic.util.Resource

@Composable
fun FeedScreen(
    feedViewModel: FeedViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {


        when (feedViewModel.postStatus.value) {


            is Resource.Error -> {

                Text(
                    text = feedViewModel.postStatus.value.message!!,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.h5,
                    textAlign = TextAlign.Center
                )

            }

            is Resource.Loading -> {

                CircularProgressIndicator()

            }

            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {

                    itemsIndexed(feedViewModel.posts) { pos, post ->
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            PostItem(
                                post = post,
                                isLiked = feedViewModel.isPostLiked(post, context)
                            )

                            if (!feedViewModel.isItLastItem(pos)) {
                                Divider(color = Gray500)
                            }
                        }
                    }
                }
            }
        }


    }

}