package com.samarth.memesmagic.util

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.samarth.memesmagic.R
import com.samarth.memesmagic.data.remote.models.PrivateChatMessageStatus
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatRoom

val ReceivedFirstMessageShape = RoundedCornerShape(
    topStart = 0.dp,
    topEnd =  8.dp,
    bottomStart = 8.dp,
    bottomEnd = 8.dp
)

val MiddleChatMessageShape = RoundedCornerShape(
    topStart = 8.dp,
    topEnd =  8.dp,
    bottomStart = 8.dp,
    bottomEnd = 8.dp
)

val SentFirstMessageShape = RoundedCornerShape(
    topStart = 8.dp,
    topEnd =  0.dp,
    bottomStart = 8.dp,
    bottomEnd = 8.dp
)

object ChatUtils{
    var currentChatRoom: PrivateChatRoom? = null
}

fun getIconAccordingToMessageStatus(msgStatus:PrivateChatMessageStatus) = when(msgStatus){
    PrivateChatMessageStatus.LOCAL -> R.drawable.ic_pending
    PrivateChatMessageStatus.SENT -> R.drawable.ic_done
    PrivateChatMessageStatus.RECEIVED -> R.drawable.ic_done_all
    else -> R.drawable.ic_done_outline
}

@Composable
fun getMessageStatusTrackerIconTint(msgStatus: PrivateChatMessageStatus) = ColorFilter.tint(
    if(msgStatus != PrivateChatMessageStatus.SEEN) {
        MaterialTheme.colors.onSurface
    }
    else{
        MaterialTheme.colors.primary
    }
)

fun getMessageCount(count:Int):String{
    return if(count < 100){
        "$count"
    } else {
        "99+"
    }
}