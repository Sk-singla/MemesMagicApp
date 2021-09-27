package com.samarth.memesmagic.ui.exoplayer

sealed class VideoQuality(val width:Int,val height:Int){
    // (width, height) (px)
    object Quality144p: VideoQuality( 256,480)
    object Quality240p: VideoQuality(426,360)
    object Quality360p: VideoQuality(480,240)
    object Quality480p: VideoQuality(640,144)
}