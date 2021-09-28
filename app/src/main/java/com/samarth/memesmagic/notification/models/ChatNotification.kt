package com.samarth.memesmagic.notification.models

import android.content.Context
import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import com.google.gson.Gson
import com.samarth.memesmagic.data.local.entities.models.LocalNotification
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatMessage
import com.samarth.memesmagic.notification.NotificationChannels.CHAT_CHANNEL
import com.samarth.memesmagic.notification.NotificationHelper
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.services.MyFirebaseMessagingService
import com.samarth.memesmagic.ui.MainActivity


@ExperimentalAnimationApi
data class ChatNotification (
    val privateChatMessage: PrivateChatMessage
): BaseNotification(NOTIFICATION_TYPE_NEW_CHAT_MESSAGE,false) {




    @ExperimentalFoundationApi
    override fun showNotification(context: Context, localNotification: LocalNotification,gson:Gson,memeRepo: MemeRepo) {
//        Log.d("notification"," Show =====================")


        // todo: collect unseen messages from database and show notifications accordingly   or   remove message from notification if seen

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.action = MyFirebaseMessagingService.INTENT_ACTION_CHAT_MESSAGE
        intent.putExtra(
            "msgSenderUserInfo",
            Gson().toJson(
                UserInfo(privateChatMessage.name,privateChatMessage.from,privateChatMessage.profilePic)
            )
        )

        NotificationHelper.displayNotification(
            context = context,
            title = "${privateChatMessage.name} Sent a Message!",
            body = privateChatMessage.message,
            intent = intent,
            channelName = CHAT_CHANNEL,
            groupKey = "Chat",
            notificationId = privateChatMessage.from.hashCode()
        )
    }
}
