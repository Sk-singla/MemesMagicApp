package com.samarth.memesmagic.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalPrivateChatMessage(
    val from: String,
    val to: String,
    val message: String,
    val timeStamp: Long,
    @PrimaryKey val id: String,
    val replyOf: String?=null
)
