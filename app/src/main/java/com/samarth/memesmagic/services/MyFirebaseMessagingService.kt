package com.samarth.memesmagic.services

import android.content.Intent
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.samarth.memesmagic.data.local.dao.MemeDao
import com.samarth.memesmagic.data.local.entities.models.LocalNotification
import com.samarth.memesmagic.notification.models.BaseNotification
import com.samarth.memesmagic.notification.models.BaseNotification.Companion.NOTIFICATION_TYPE_NEW_CHAT_MESSAGE
import com.samarth.memesmagic.notification.models.BaseNotification.Companion.NOTIFICATION_TYPE_NEW_FOLLOWER
import com.samarth.memesmagic.notification.models.ChatNotification
import com.samarth.memesmagic.notification.models.NewFollowerNotification
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.TokenHandler.saveFcmToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService(){


    @Inject
    lateinit var memeRepo: MemeRepo

    @Inject
    lateinit var gson:Gson

    override fun onNewToken(token: String) {
        runBlocking {
            val result = memeRepo.updateFcmToken(
                token
            )
            if(result is Resource.Success){
                saveFcmToken(this@MyFirebaseMessagingService,token)
            }
        }
    }

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val strMessage = remoteMessage.data["message"]!!
        val jsonObject = JsonParser.parseString(strMessage).asJsonObject
        val type = when(jsonObject.get("type").asString){
            NOTIFICATION_TYPE_NEW_FOLLOWER -> NewFollowerNotification::class.java
            NOTIFICATION_TYPE_NEW_CHAT_MESSAGE -> ChatNotification::class.java
            else -> BaseNotification::class.java
        }

        Log.d("notification",type.toString())
        val notificationObj = gson.fromJson(strMessage,type)
        val localNotification = LocalNotification(
            notification = strMessage,
            time = System.currentTimeMillis(),
            seen = false
        )
        notificationObj.showNotification(this,localNotification,gson,memeRepo)


    }

    private fun passMessageToActivity(message: String) {
        val intent = Intent().apply {
            action = INTENT_ACTION_FCM_MESSAGE
            putExtra("message",message)
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    companion object {
        const val INTENT_ACTION_FCM_MESSAGE = "INTENT_ACTION_FCM_MESSAGE"

        const val INTENT_ACTION_NEW_FOLLOWER = "INTENT_ACTION_NEW_FOLLOWER"
        const val INTENT_ACTION_CHAT_MESSAGE = "INTENT_ACTION_CHAT_MESSAGE"
    }



}