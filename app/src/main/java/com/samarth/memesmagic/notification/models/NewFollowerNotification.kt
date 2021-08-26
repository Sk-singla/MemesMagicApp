package com.samarth.memesmagic.notification.models

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import com.samarth.memesmagic.data.local.dao.MemeDao
import com.samarth.memesmagic.data.local.entities.models.LocalNotification
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.notification.NotificationHelper
import com.samarth.memesmagic.services.MyFirebaseMessagingService.Companion.INTENT_ACTION_NEW_FOLLOWER
import com.samarth.memesmagic.ui.MainActivity

data class NewFollowerNotification(
    val follower:UserInfo
):BaseNotification(NOTIFICATION_TYPE_NEW_FOLLOWER){



    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    override fun showNotification(context: Context,notificationId:String) {
        val intent = Intent(context,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.action = INTENT_ACTION_NEW_FOLLOWER
        intent.putExtra("follower",follower.email)
        intent.putExtra("notificationId",notificationId)

        NotificationHelper.displayNotification(
            context = context,
            title = "New Follower!",
            body = "${follower.name} started Following You.",
            intent = intent,
            channelName = "NewFollower_MemesMagic"
        )
    }
}