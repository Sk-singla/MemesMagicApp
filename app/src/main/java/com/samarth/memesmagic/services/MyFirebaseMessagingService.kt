package com.samarth.memesmagic.services

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.samarth.memesmagic.MainActivity
import com.samarth.memesmagic.data.remote.response.fcm_messages.FcmFollowerAddedMessage
import com.samarth.memesmagic.data.remote.response.fcm_messages.FcmMessage
import com.samarth.memesmagic.data.remote.response.fcm_messages.FcmMessageData
import com.samarth.memesmagic.notification.NotificationHelper
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.util.Constants.FCM_TYPE_FOLLOWER_ADDED
import com.samarth.memesmagic.util.TokenHandler.getJwtToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService(){


    @Inject
    lateinit var memeRepo: MemeRepo


    override fun onNewToken(token: String) {
        runBlocking {
            memeRepo.updateFcmToken(
                getJwtToken(this@MyFirebaseMessagingService) ?: "",
                token
            )
        }
    }

    @ExperimentalFoundationApi
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)


        // todo: when click on notification -> go to main activity and show alert box where written follow back or whatever else!
        // todo: like post with websocket ( or real time ) and notification

        val strMessage = remoteMessage.data["message"]!!
        passMessageToActivity(strMessage)
    }

    private fun passMessageToActivity(message: String) {
        val intent = Intent().apply {
            action = INTENT_ACTION_SEND_MESSAGE
            putExtra("message",message)
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    companion object {
        const val INTENT_ACTION_SEND_MESSAGE = "INTENT_ACTION_SEND_MESSAGE"
        const val INTENT_ACTION_NEW_FOLLOWER = "INTENT_ACTION_NEW_FOLLOWER"
    }



}