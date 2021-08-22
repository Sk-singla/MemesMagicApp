package com.samarth.memesmagic.notification.models

import android.content.Context

abstract class BaseNotification(
    val type:String
){

    companion object{
        const val NOTIFICATION_TYPE_NEW_FOLLOWER = "NOTIFICATION_TYPE_NEW_FOLLOWER"
    }

    abstract fun showNotification(context: Context)

}