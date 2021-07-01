package com.samarth.memesmagic.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.samarth.memesmagic.data.remote.response.Comment
import com.samarth.memesmagic.ui.theme.Green500


@Composable
fun CommentItem(
    comment: Comment,
    isCommentLiked:Boolean,
    onCommentLikeUnlikePressed:(onSuccess:()->Unit)->Unit
) {

    var isLiked by remember {
        mutableStateOf(isCommentLiked)
    }

    var likes by remember {
        mutableStateOf(comment.likedBy.size)
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp),horizontalArrangement = Arrangement.SpaceBetween) {

        Row{

            Image(
                painter = rememberCoilPainter(
                    request = ImageRequest.Builder(LocalContext.current)
                        .data(comment.userInfo.profilePic ?: R.drawable.ic_person)
                        .placeholder(R.drawable.ic_person)
                        .error(R.drawable.ic_person)
                        .build(),
                    fadeIn = true,
                ),
                contentScale = ContentScale.Crop,
                contentDescription = "User Image",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .size(32.dp)
                    .clip(CircleShape),
            )

            Column {


                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        ) {
                            append(comment.userInfo.name + "  ")
                        }
                        withStyle(style = SpanStyle(fontSize = 16.sp)) {
                            append(comment.text)
                        }
                    }
                )

                Text(
                    text = "$likes likes",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(0.6f)
                )

            }

        }

        IconButton(
            onClick = {
                onCommentLikeUnlikePressed{
                    isLiked = !isLiked
                    if(isLiked){
                        likes += 1
                    } else {
                        likes -= 1
                    }
                }
            },
            modifier = Modifier
                .padding(start = 2.dp)
                .background(color = Color.Transparent, shape = CircleShape)
        ) {
            Icon(
                painter = painterResource(id = if(isLiked) R.drawable.ic_favorite else R.drawable.ic_favorite_border),
                contentDescription = "Like",
                tint = if(isLiked) Green500 else LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
                modifier = Modifier.size(16.dp)
            )
        }

    }

}