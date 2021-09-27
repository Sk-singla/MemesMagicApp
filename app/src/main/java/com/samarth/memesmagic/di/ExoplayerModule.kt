package com.samarth.memesmagic.di

import android.app.Dialog
import android.content.Context
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.TrackSelectionDialogBuilder
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExoplayerModule {

    @Singleton
    @Provides
    fun provideSimpleExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes,
        trackSelector:DefaultTrackSelector
    ): SimpleExoPlayer {
        return SimpleExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .build().apply {
                setAudioAttributes(audioAttributes,true)
                setHandleAudioBecomingNoisy(true)
            }
    }

    @Singleton
    @Provides
    fun provideTrackSelector(
        @ApplicationContext context: Context,
    ) = DefaultTrackSelector(context).apply {
        setParameters(buildUponParameters().setMaxVideoSizeSd())
    }


    @Singleton
    @Provides
    fun provideAudioAttributes() = AudioAttributes.Builder()
        .setContentType(C.CONTENT_TYPE_SONIFICATION)
        .setUsage(C.USAGE_MEDIA)
        .build()

    @Singleton
    @Provides
    fun provideDataSourceFactory(
        @ApplicationContext context: Context
    ) = DefaultDataSourceFactory(context, Util.getUserAgent(context,context.packageName))




}