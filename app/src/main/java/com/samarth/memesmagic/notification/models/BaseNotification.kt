package com.samarth.memesmagic.notification.models

import android.content.Context
import com.google.gson.Gson
import com.samarth.memesmagic.data.local.dao.MemeDao
import com.samarth.memesmagic.data.local.entities.models.LocalNotification
import com.samarth.memesmagic.repository.MemeRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

abstract class BaseNotification(
    val type:String,
    val shouldSaveLocally:Boolean
){

    protected val coroutineScope = CoroutineScope(Dispatchers.IO)

    companion object{
        const val NOTIFICATION_TYPE_NEW_FOLLOWER = "NOTIFICATION_TYPE_NEW_FOLLOWER"
        const val NOTIFICATION_TYPE_NEW_CHAT_MESSAGE = "NOTIFICATION_TYPE_NEW_CHAT_MESSAGE"
    }

    abstract fun showNotification(context: Context,localNotification: LocalNotification,gson: Gson,memeRepo:MemeRepo)


}