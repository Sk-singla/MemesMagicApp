package com.samarth.memesmagic.util

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
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