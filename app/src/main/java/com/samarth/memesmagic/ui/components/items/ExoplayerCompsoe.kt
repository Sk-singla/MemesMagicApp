package com.samarth.memesmagic.ui.components.items

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toBitmap
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.google.accompanist.coil.rememberCoilPainter
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.samarth.memesmagic.R
import com.samarth.memesmagic.data.remote.response.Post
import com.samarth.memesmagic.ui.exoplayer.VideoQuality
import com.samarth.memesmagic.util.CommentsUtil
import com.samarth.memesmagic.util.videoTime
import kotlinx.coroutines.delay

@ExperimentalAnimationApi
@Composable
fun ExoPlayerCompose(
    exoPlayer:SimpleExoPlayer,
    modifier: Modifier = Modifier,
    playerViewVisible:Boolean,
    thumbnail:Bitmap?,
    isFeedScreen:Boolean,
    changeVideoQuality: ()->Unit
) {

    var durationVisible by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(exoPlayer.isLoading){
        while(exoPlayer.isLoading){
            durationVisible = !durationVisible
            delay(1000L)
        }
        if(!exoPlayer.isLoading){
            durationVisible = true
        }
    }

    Log.d("video exo view","visibilty-> $playerViewVisible && playing-> ${exoPlayer.isPlaying}")

    Box(modifier = modifier){
        if(playerViewVisible){
            AndroidView(
                factory = {
                    PlayerView(it)
                },
                modifier = Modifier.matchParentSize()
            ) {
                it.setShutterBackgroundColor(Color.TRANSPARENT)
                it.resizeMode = if(isFeedScreen) AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
                else  AspectRatioFrameLayout.RESIZE_MODE_FIT

                it.player = exoPlayer
                it.hideController()
            }

            if(isFeedScreen){
                if (exoPlayer.isPlaying) {
                    IconButton(
                        onClick = {
                            exoPlayer.pause()
                        },
                        modifier = Modifier.align(Alignment.BottomEnd)
                    ) {
                        Icon(imageVector = Icons.Default.Pause, contentDescription = "Pause")
                    }
                } else {
                    IconButton(
                        onClick = {
                            exoPlayer.playWhenReady = true
                        },
                        modifier = Modifier.align(Alignment.BottomEnd)
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play")
                    }
                }
            }

            AnimatedVisibility(
                visible = durationVisible,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
            ) {
                Text(
                    text = videoTime(exoPlayer.duration - exoPlayer.currentPosition),
                    style = TextStyle(shadow = Shadow(MaterialTheme.colors.onSurface,offset = Offset(2f,2f),blurRadius = 2f)),
                )
            }

            if(!isFeedScreen) {
                IconButton(
                    onClick = {
                        changeVideoQuality()
                    },
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Video Settings"
                    )
                }
            }


        } else {
            thumbnail?.asImageBitmap()?.let{
                Image(
                    bitmap = it,
                    contentScale = if(isFeedScreen) ContentScale.Crop else ContentScale.Fit,
                    contentDescription = "thumbnail",
                    modifier = Modifier
                        .matchParentSize()
                        .align(Alignment.Center)
                )
            } ?:
            Image(
                painter = painterResource(id = R.drawable.ic_image),
                contentScale = ContentScale.Crop,
                contentDescription = "thumbnail",
                modifier = Modifier.matchParentSize()
            )

            Icon(
                imageVector = Icons.Outlined.PlayCircleOutline,
                contentDescription = "PlayIcon",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(32.dp)
            )
        }
    }

}