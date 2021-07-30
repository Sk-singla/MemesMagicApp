package com.samarth.memesmagic.notification

import android.app.PendingIntent
import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.samarth.memesmagic.R

object NotificationHelper {

    @ExperimentalFoundationApi
    fun displayNotification(
        context: Context,
        title: String,
        body: String,
        pendingIntent: PendingIntent,
        CHANNEL_ID: String,
        NOTIFICATION_ID: Int
    ) {
        val notificationBuilder = NotificationCompat.Builder(
            context,
            CHANNEL_ID
        ).setSmallIcon(R.drawable.memes_magic_logo)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val mNotificationManager = NotificationManagerCompat.from(context)
        mNotificationManager.notify(NOTIFICATION_ID,notificationBuilder.build())

    }
}