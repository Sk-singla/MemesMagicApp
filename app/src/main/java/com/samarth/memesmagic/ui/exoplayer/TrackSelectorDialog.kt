package com.samarth.memesmagic.ui.exoplayer

import android.app.Dialog
import android.content.Context
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.TrackSelectionDialogBuilder
import dagger.hilt.android.qualifiers.ApplicationContext

fun provideTrackSelectorDialog(
    context: Context,
    trackSelector: DefaultTrackSelector,
    exoplayer: SimpleExoPlayer
): Dialog {
    val trackSelectorDialogBuilder = TrackSelectionDialogBuilder(
        context,
        "Video Quality",
        trackSelector,
        exoplayer.currentWindowIndex
    )
    trackSelectorDialogBuilder
        .setTrackFormatComparator{ format1, format2 ->
            when {
                format1.height < format2.height -> -1
                format1.height == format2.height -> 0
                else -> 1
            }
        }
    return trackSelectorDialogBuilder
        .setTrackNameProvider{
            when {
                it.height < 200 -> {
                    "144p"
                }
                it.height < 300 -> {
                    "240p"
                }
                it.height < 400 -> {
                    "360p"
                }
                it.height < 520 -> {
                    "480p"
                }
                it.height < 800 -> {
                    "720p"
                }
                it.height < 1500 -> {
                    "1080p"
                }
                else -> {
                    "4k"
                }
            }
        }.build()
}
