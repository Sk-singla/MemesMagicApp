package com.samarth.memesmagic.ui.exoplayer

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.samarth.memesmagic.data.remote.response.Post

var lastPlayingPost:Post? = null

val postPlaybackDetails = mutableMapOf<String,ExoplayerStateProps>()

@Composable
fun UpdateCurrentlyPlayingItem(
    post: Post?,
    player:SimpleExoPlayer,
    dataSourceFactory:DefaultDataSourceFactory,

){
    LaunchedEffect(key1 = post){
        player.apply {
            if(post != null){

                if(lastPlayingPost!= null)
                    postPlaybackDetails[lastPlayingPost!!.id] = ExoplayerStateProps(player.currentPosition,player.currentWindowIndex)



                Log.d("current video","=========================")

                // ======= FOR DASH FILE ===============
    //                 DashMediaSource.Factory(DefaultHttpDataSource.Factory())
    //                    .createMediaSource(
    //                        MediaItem.fromUri(
    //                        "http://rdmedia.bbc.co.uk/dash/ondemand/bbb/2/client_manifest-separate_init.mpd"
    //                        )
    //                    )

                val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(
                        MediaItem.fromUri(
                            post.mediaLink
                        ))
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
                    postPlaybackDetails[lastPlayingPost!!.id] = ExoplayerStateProps(player.currentPosition,player.currentWindowIndex)
                stop()
            }
        }

    }
}