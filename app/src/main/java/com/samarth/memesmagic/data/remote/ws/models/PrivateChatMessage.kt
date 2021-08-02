package com.samarth.memesmagic.data.remote.ws.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.samarth.memesmagic.data.remote.models.PrivateChatMessageStatus
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.util.Constants.TYPE_PRIVATE_CHAT_MESSAGE
import java.util.*


@Entity
data class PrivateChatMessage(
    var from: String = "",
    var to: String = "",
    var message: String = "",
    var timeStamp: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = false)
    var id: String = UUID.randomUUID().toString(),
    var msgStatus: PrivateChatMessageStatus = PrivateChatMessageStatus.LOCAL,
    var replyOf: String?=null,
    var profilePic: String? = null,
    val name: String = "",
    val otherUserEmail:String = ""
): BaseModel(TYPE_PRIVATE_CHAT_MESSAGE)