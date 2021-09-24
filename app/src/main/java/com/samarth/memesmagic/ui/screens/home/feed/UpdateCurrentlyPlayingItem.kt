package com.samarth.memesmagic.ui.screens.home.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util
import com.samarth.memesmagic.data.remote.response.Post


val postPlaybackDetails = mutableMapOf<String,ExoplayerStateProps>()

data class ExoplayerStateProps(
    val position:Long,
    val windowIndex:Int
)

var lastPlayingPost:Post? = null

@Composable
fun UpdateCurrentlyPlayingItem(
    exoPlayer: SimpleExoPlayer,
    post:Post?,
    dataSourceFactory: DefaultDataSourceFactory
) {

    LaunchedEffect(post){
        exoPlayer.apply {
            if(post != null){

                if(lastPlayingPost!= null)
                    postPlaybackDetails[lastPlayingPost!!.id] = ExoplayerStateProps(exoPlayer.currentPosition,exoPlayer.currentWindowIndex)



                Log.d("video","=========================")
                val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(post.mediaLink))
                setMediaSource(source)
                if(postPlaybackDetails[post.id] == null){
                    postPlaybackDetails[post.id] = ExoplayerStateProps(0,0)
                }

                seekTo(postPlaybackDetails[post.id]!!.windowIndex,postPlaybackDetails[post.id]!!.position)
                prepare()
                playWhenReady = true
                repeatMode = ExoPlayer.REPEAT_MODE_ALL

                lastPlayingPost = post
            } else {
                if(lastPlayingPost!= null)
                    postPlaybackDetails[lastPlayingPost!!.id] = ExoplayerStateProps(exoPlayer.currentPosition,exoPlayer.currentWindowIndex)
                stop()
            }
        }
    }












}