package com.samarth.memesmagic.notification.notification_helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.core.app.NotificationCompat
import com.samarth.memesmagic.R
import kotlin.random.Random

object ChatNotificationHelper {


    private val inboxStyles = hashMapOf<Int,NotificationCompat.InboxStyle>()

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    fun displayNotification(
        context: Context,
        title: String,
        body: String,
        intent:Intent,
        channelName: String,
        groupKey:String? = null,
        notificationId:Int = Random.nextInt(),
        largeIcon: Bitmap? = null
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager,channelName)
        }

        val pi = PendingIntent.getActivity(context,0,intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationBuilder = NotificationCompat.Builder(context,channelName)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.memes_magic_logo)
            .setAutoCancel(true)
            .setContentIntent(pi)
            .setLargeIcon(largeIcon)
            .setGroup(groupKey)
            .setGroupSummary(true)


        if(inboxStyles[notificationId] == null){
            inboxStyles[notificationId] = NotificationCompat.InboxStyle()
        }
        inboxStyles[notificationId]!!.setBigContentTitle(title)
        inboxStyles[notificationId]!!.setSummaryText("asdf")
        inboxStyles[notificationId]!!.addLine(body)
        notificationBuilder.setStyle(inboxStyles[notificationId])

        notificationManager.notify(notificationId,notificationBuilder.build())
    }




    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager, channelName:String){

        val channel = NotificationChannel(channelName,channelName, NotificationManager.IMPORTANCE_HIGH).apply {
            description = "$channelName Channel"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }



}