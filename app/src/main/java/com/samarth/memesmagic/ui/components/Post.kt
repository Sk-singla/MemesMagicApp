package com.samarth.memesmagic.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.request.ImageRequest
import com.google.accompanist.coil.rememberCoilPainter
import com.samarth.memesmagic.R
import com.samarth.memesmagic.data.remote.models.PostType
import com.samarth.memesmagic.data.remote.response.Post
import com.samarth.memesmagic.ui.theme.Green500

@Composable
fun PostItem(
    post: Post,
    modifier: Modifier = Modifier,
    isLiked: Boolean,
    onLikeIconPressed:(post:Post,isPostLiked:Boolean, onSuccess:()->Unit)->Unit,
    onCommentIconPressed: (post:Post) -> Unit,
    onShareIconPressed: (post:Post) -> Unit
) {

    var isPostLiked by remember {
        mutableStateOf(isLiked)
    }


        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
//            .shadow(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Top -> Image + Name
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = rememberCoilPainter(
                        request = ImageRequest.Builder(LocalContext.current)
                            .data(post.createdBy.profilePic ?: R.drawable.ic_person)
                            .placeholder(R.drawable.ic_person)
                            .error(R.drawable.ic_person)
                            .build(),
                        fadeIn = true,
                    ),
                    contentScale = ContentScale.Crop,
                    contentDescription = "User Image",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                )

                Text(
                    text = post.createdBy.name,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(start = 8.dp)
                )

            }


            // Post Content ->
            when (post.postType) {
                PostType.IMAGE -> {


                    Image(
                        painter = rememberCoilPainter(
                            request = ImageRequest.Builder(LocalContext.current)
                                .data(post.mediaLink)
                                .placeholder(R.drawable.ic_image)
                                .error(R.drawable.ic_image)
                                .build(),
                            fadeIn = true,
                        ),
                        contentScale = ContentScale.FillWidth,
                        contentDescription = "Post",
                        modifier = Modifier
                            .fillMaxWidth(),
                    )

                }
                else -> {

                }
            }


            // Icons -> like, comment, share
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(id = if(isPostLiked) R.drawable.ic_favorite else R.drawable.ic_favorite_border),
                    contentDescription = "Like",
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clickable {
                            onLikeIconPressed(post, isPostLiked) {
                                isPostLiked = !isPostLiked
                            }
                        },
                    tint = if(isPostLiked) Green500 else LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_comment_24),
                    contentDescription = "Comment",
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable {
                            onCommentIconPressed(post)
                        }
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_share_24),
                    contentDescription = "Share",
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable {
                            onShareIconPressed(post)
                        }
                )
            }

            // Description ->


            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                        append(post.createdBy.name + "   ")
                    }
                    post.description?.let { description ->
                        withStyle(style = SpanStyle(fontSize = 16.sp)) {
                            append(description)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )


        }
}