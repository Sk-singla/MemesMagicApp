package com.samarth.memesmagic.ui.components.items

import android.graphics.Bitmap
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.request.ImageRequest
import com.google.accompanist.coil.rememberCoilPainter
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.samarth.memesmagic.R
import com.samarth.memesmagic.data.remote.models.PostResource
import com.samarth.memesmagic.data.remote.models.PostType
import com.samarth.memesmagic.data.remote.response.Post
import com.samarth.memesmagic.ui.components.ProfileImage
import com.samarth.memesmagic.ui.theme.Green500

@Composable
fun PostItem(
    post: Post,
    modifier: Modifier = Modifier,
    isLiked: Boolean,
    onLikeIconPressed:(post:Post,isPostLiked:Boolean, onSuccess:()->Unit)->Unit,
    onCommentIconPressed: (post:Post) -> Unit,
    onShareIconPressed: (post:Post) -> Unit,
    onClick:()->Unit,
    isSinglePost:Boolean = false,
    exoPlayer: SimpleExoPlayer? = null,
    aspectRatio:Float? = null,
    playerViewVisible:Boolean = true,
    thumbnail:Bitmap? =null
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

                ProfileImage(
                    name = post.createdBy.name,
                    imageUrl = post.createdBy.profilePic,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            onClick()
                        }
                )

                Text(
                    text = post.createdBy.name,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable { onClick() }
                )

            }


            // Post Content ->
            if (post.postType == PostType.IMAGE && post.mediaLink.takeLast(3) != "mp4") {


                    Image(
                        painter = rememberCoilPainter(
                            request = ImageRequest.Builder(LocalContext.current)
                                .data(post.mediaLink)
                                .placeholder(R.drawable.blank_image)
                                .error(R.drawable.ic_error)
                                .build(),
                            fadeIn = true,
                        ),
                        contentScale = ContentScale.FillWidth,
                        contentDescription = "Post",
                        modifier = if(isSinglePost){
                            Modifier
                                .fillMaxWidth()
                        } else {
                            Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                        },
                    )

                }
                else {


                    exoPlayer?.let{
                        ExoPlayerCompose(
                            exoPlayer = it,
                            modifier =  if(isSinglePost){
                                Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(aspectRatio ?: 1f)
                            } else {
                                Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                            },
                            mediaUri = post.mediaLink,
                            playerViewVisible = playerViewVisible,
                            thumbnail = thumbnail
                        )
                    } ?: kotlin.run {
                        Text(
                            text = "Something Went Wrong!",
                            fontWeight = FontWeight.Bold,
                            modifier = if(isSinglePost){
                                Modifier
                                    .fillMaxWidth()
                            } else {
                                Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                            },
                            textAlign = TextAlign.Center
                        )
                    }
            }


            // Icons -> like, comment, share
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                IconButton(
                    onClick = {
                        onLikeIconPressed(post, isPostLiked) {
                            isPostLiked = !isPostLiked
                        }
                    },
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .background(color = Color.Transparent, shape = CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = if(isPostLiked) R.drawable.ic_favorite else R.drawable.ic_favorite_border),
                        contentDescription = "Like",
                        tint = if(isPostLiked) Green500 else LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                    )
                }

                IconButton(
                    onClick = {
                        onCommentIconPressed(post)
                    },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .background(color = Color.Transparent, shape = CircleShape),

                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_comment_24),
                        contentDescription = "Comment"
                    )
                }

                IconButton(
                    onClick = {
                        onShareIconPressed(post)
                    },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .background(color = Color.Transparent, shape = CircleShape),

                    ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_share_24),
                        contentDescription = "Share"
                    )
                }
            }

            // Caption ->


            if(post.postResource != PostResource.GITHUB_API) {

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        ) {
                            append(post.createdBy.name + ":  ")
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
}