package com.samarth.memesmagic.ui.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatMessage
import com.samarth.memesmagic.ui.theme.Green200
import com.samarth.memesmagic.util.*


@Composable
fun PrivateChatMessageItem(
    privateChatMessage: PrivateChatMessage,
    isReceived: Boolean,
    isFirst: Boolean,
    isSelected:Boolean = false,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = if(isSelected){
            modifier
                .background(color= MaterialTheme.colors.primary.copy(alpha = 0.6f))
        } else {
            modifier
        }
    ) {

        if(isReceived){
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(0.75f)
                    .align(Alignment.Start),
                contentAlignment = Alignment.CenterStart
            ) {

                Column(
                    modifier = Modifier
                        .shadow(
                            elevation = 2.dp,
                            shape = if (isFirst) ReceivedFirstMessageShape
                            else MiddleChatMessageShape
                        )
                        .background(
                            color = MaterialTheme.colors.surface,
                            shape = if (isFirst) ReceivedFirstMessageShape
                            else MiddleChatMessageShape
                        )
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ) {

                    Text(
                        text = privateChatMessage.message,
                        color = MaterialTheme.colors.onSurface,
                        fontSize = 16.sp
                    )

                    Text(
                        text = getChatMessageTime(privateChatMessage.timeStamp),
                        color = MaterialTheme.colors.onSurface.copy(0.65f),
                        fontSize = 8.sp
                    )

                }

            }

        } else {

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .padding(vertical = 4.dp)
                    .align(Alignment.End),
                contentAlignment = Alignment.CenterEnd
            ) {
                Column(
                    modifier = Modifier
                        .shadow(
                            2.dp, shape = if (isFirst) SentFirstMessageShape
                            else MiddleChatMessageShape
                        )
                        .background(
                            color = Green200,
                            shape = if (isFirst) SentFirstMessageShape
                            else MiddleChatMessageShape
                        )
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = privateChatMessage.message,
                        color = MaterialTheme.colors.onSurface,
                        fontSize = 16.sp
                    )

                    Row(
                        modifier = Modifier.height(12.dp)
                    ){
                        Text(
                            text = getChatMessageTime(privateChatMessage.timeStamp),
                            color = MaterialTheme.colors.onSurface.copy(0.65f),
                            fontSize = 8.sp
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Image(
                            painter = painterResource(
                                id= getIconAccordingToMessageStatus(privateChatMessage.msgStatus)
                            ),
                            contentDescription = "Message Status",
                            modifier = Modifier.fillMaxHeight(),
                            colorFilter = ColorFilter.tint(
                                MaterialTheme.colors.onSurface
                            )
                        )
                    }

                }
            }
        }

    }
}

