package com.samarth.memesmagic.notification.models

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import com.google.gson.Gson
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatMessage
import com.samarth.memesmagic.notification.NotificationChannels.CHAT_CHANNEL
import com.samarth.memesmagic.notification.notification_helpers.NotificationHelper
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.services.MyFirebaseMessagingService
import com.samarth.memesmagic.ui.MainActivity
import kotlinx.coroutines.launch


@ExperimentalAnimationApi
data class ChatNotification (
    val privateChatMessage: PrivateChatMessage
): BaseNotification(NOTIFICATION_TYPE_NEW_CHAT_MESSAGE,false) {



    private fun savePrivateChatMessageToLocalDatabase(memeRepo: MemeRepo) = coroutineScope.launch{
        memeRepo.savePrivateChatMessage(privateChatMessage)
        memeRepo.messageReceived(privateChatMessage.id,privateChatMessage.from)
    }

    @ExperimentalFoundationApi
    override fun showNotification(context: Context, notificationId: String,gson:Gson,memeRepo: MemeRepo) {
//        Log.d("notification"," Show =====================")
        savePrivateChatMessageToLocalDatabase(memeRepo)


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
