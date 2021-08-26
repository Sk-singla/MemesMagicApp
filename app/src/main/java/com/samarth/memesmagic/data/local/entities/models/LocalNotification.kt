package com.samarth.memesmagic.data.local.entities.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.samarth.memesmagic.notification.models.BaseNotification
import java.util.*

@Entity
data class LocalNotification(
    val notification:String,
    val time:Long,
    val seen:Boolean,
    @PrimaryKey(autoGenerate = false)
    val notificationId:String = UUID.randomUUID().toString()
)