package com.samarth.memesmagic.notification.models

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import com.google.gson.Gson
import com.samarth.memesmagic.data.local.entities.models.LocalNotification
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.notification.NotificationChannels.ACTIVITY_CHANNEL
import com.samarth.memesmagic.notification.NotificationHelper
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.services.MyFirebaseMessagingService.Companion.INTENT_ACTION_NEW_FOLLOWER
import com.samarth.memesmagic.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class NewFollowerNotification(
    val follower:UserInfo
):BaseNotification(NOTIFICATION_TYPE_NEW_FOLLOWER,true){



    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    override fun showNotification(context: Context,localNotification: LocalNotification,gson: Gson,memeRepo: MemeRepo) {
        Log.d("Notification follower","Hello")

        CoroutineScope(Dispatchers.IO).launch {
            memeRepo.saveNotification(localNotification)
        }

        val intent = Intent(context,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.action = INTENT_ACTION_NEW_FOLLOWER
        intent.putExtra("follower",follower.email)
        intent.putExtra("notificationId",localNotification.notificationId)

        NotificationHelper.displayNotification(
            context = context,
            title = "New Follower!",
            body = "${follower.name} started Following You.",
            intent = intent,
            channelName = ACTIVITY_CHANNEL
        )
    }
}