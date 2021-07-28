package com.samarth.memesmagic.data.remote.ws.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.util.Constants.TYPE_PRIVATE_CHAT_MESSAGE
import java.util.*


@Entity(tableName = "PrivateChatMessage")
data class PrivateChatMessage(
    var from: String = "",
    var to: String = "",
    var message: String = "",
    var timeStamp: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = false)
    var id: String = UUID.randomUUID().toString(),
    var seen: Boolean = false,
    var received: Boolean = false,
    var replyOf: String?=null,
    var profilePic: String? = null,
    val name: String = ""
): BaseModel(TYPE_PRIVATE_CHAT_MESSAGE)