package com.samarth.memesmagic.ui.components.items

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toBitmap
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.google.accompanist.coil.rememberCoilPainter
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.samarth.memesmagic.R
import com.samarth.memesmagic.data.remote.response.Post
import com.samarth.memesmagic.util.CommentsUtil

@Composable
fun ExoPlayerCompose(
    exoPlayer:SimpleExoPlayer,
    modifier: Modifier = Modifier,
    playerViewVisible:Boolean,
    mediaUri:String,
    thumbnail:Bitmap?
) {
    val context = LocalContext.current
    Box(modifier = modifier){
        if(playerViewVisible){
            AndroidView(
                factory = {
                    PlayerView(it)
                },
                modifier = Modifier.matchParentSize()
            ) {
                it.setShutterBackgroundColor(Color.TRANSPARENT)
                it.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                it.player = exoPlayer
                it.hideController()
            }

            if(exoPlayer.isPlaying){
                IconButton(
                    onClick = {
                        exoPlayer.pause()
                    },
                    modifier =Modifier.align(Alignment.TopStart)
                ) {
                    Icon(imageVector = Icons.Default.Pause, contentDescription = "Pause")
                }
            } else {
                IconButton(
                    onClick = {
                        exoPlayer.playWhenReady = true
                    },
                    modifier =Modifier.align(Alignment.TopStart)
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play")
                }
            }


            if(exoPlayer.isLoading)
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(24.dp)
                )



        } else {
            thumbnail?.asImageBitmap()?.let{
                Image(
                    bitmap = it,
                    contentScale = ContentScale.Fit,
                    contentDescription = "thumbnail",
                    modifier = Modifier
//                        .aspectRatio(it.width/((it.height).toFloat()))
                        .align(Alignment.Center)
                )
            } ?:
            Image(
                painter = painterResource(id = R.drawable.ic_error),
                contentScale = ContentScale.Crop,
                contentDescription = "thumbnail",
                modifier = Modifier.matchParentSize()
            )

            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "PlayIcon",
                modifier = Modifier.align(Alignment.Center)
            )
        }

    }

}