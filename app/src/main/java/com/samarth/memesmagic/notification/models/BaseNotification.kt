package com.samarth.memesmagic.notification.models

import android.content.Context
import com.samarth.memesmagic.data.local.dao.MemeDao
import com.samarth.memesmagic.data.local.entities.models.LocalNotification

abstract class BaseNotification(
    val type:String
){

    companion object{
        const val NOTIFICATION_TYPE_NEW_FOLLOWER = "NOTIFICATION_TYPE_NEW_FOLLOWER"
    }

    abstract fun showNotification(context: Context,notificationId:String)


}