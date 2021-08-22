package com.samarth.memesmagic.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.*
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.samarth.memesmagic.R
import com.samarth.memesmagic.ui.MainActivity
import kotlin.random.Random

object NotificationHelper {

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    fun displayNotification(
        context: Context,
        title: String,
        body: String,
        intent:Intent,
        channelName: String
    ) {

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random.nextInt()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager,channelName)
        }

        val pi = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_ONE_SHOT)
        val notification = NotificationCompat.Builder(context,channelName)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.memes_magic_logo)
            .setAutoCancel(true)
            .setContentIntent(pi)
            .build()
        notificationManager.notify(notificationId,notification)

    }




    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager,channelName:String){

        val channel = NotificationChannel(channelName,channelName, IMPORTANCE_HIGH).apply {
            description = "$channelName Channel"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }
}