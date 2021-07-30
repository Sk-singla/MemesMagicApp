package com.samarth.memesmagic.services

import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.samarth.memesmagic.data.local.dao.MemeDao
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.TokenHandler.saveFcmToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService(){


    @Inject
    lateinit var memeRepo: MemeRepo

    @Inject
    lateinit var memeDao:MemeDao


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


        // todo: when click on notification -> go to main activity and show alert box where written follow back or whatever else!
        // todo: like post with websocket ( or real time ) and notification

        val strMessage = remoteMessage.data["message"]!!
        passMessageToActivity(strMessage)

//        if(remoteMessage.notification != null){
//
//            val pi = PendingIntent.getBroadcast(
//                this,
//                12,
//                Intent("NewFollower"),
//                PendingIntent.FLAG_UPDATE_CURRENT
//            )
//            NotificationHelper.displayNotification(
//                this,
//                title = remoteMessage.notification!!.title ?: "",
//                body = remoteMessage.notification!!.body ?: "",
//                pendingIntent = pi,
//                "MEME_CHANNEL",
//                1
//            )
//        }
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