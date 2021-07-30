package com.samarth.memesmagic.ui.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.vector.DefaultTintColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.samarth.memesmagic.R
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatMessage
import com.samarth.memesmagic.ui.components.CustomTopBar
import com.samarth.memesmagic.util.*

@Composable
fun PrivateChatRoomScreen(
    navController: NavController,
    chatViewModel: ChatViewModel
) {

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit ){
        chatViewModel.observeSingleChatLocalDatabase()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomTopBar(
                title = ChatUtils.currentChatRoom?.name ?: ""
            )
        }
    ) {


        Box(
            modifier = Modifier.fillMaxSize(),
        ) {


            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                reverseLayout = true
            ) {
                item {
                    Spacer(modifier = Modifier.padding(40.dp))
                }
                itemsIndexed(chatViewModel.currentChatRoomMessages.value) {  index, privateChatMessage->

                    val isReceived = privateChatMessage.to == chatViewModel.currentUserEmail
                    val isFirst = index == chatViewModel.currentChatRoomMessages.value.size -1 || chatViewModel.currentChatRoomMessages.value[index+1].from != chatViewModel.currentChatRoomMessages.value[index].from

                    if(isReceived && !privateChatMessage.received){
                        chatViewModel.messageReceived(
                            msgId = privateChatMessage.id,
                            messageSender = privateChatMessage.from
                        )
                    }

                    if(isReceived && !privateChatMessage.seen){
                        chatViewModel.messageSeen(
                            msgId = privateChatMessage.id,
                            messageSender = privateChatMessage.from
                        )
                    }
                    PrivateChatMessageItem(
                        privateChatMessage = privateChatMessage,
                        isReceived = isReceived,
                        isFirst = isFirst
                    )

                }



            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(color = MaterialTheme.colors.background),
                contentAlignment = Alignment.Center
            ){
                Row {
                    TextField(
                        value = chatViewModel.currentMessage.value,
                        onValueChange = {
                            chatViewModel.currentMessage.value = it
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                chatViewModel.sendChatMessage()
                            }) {
                                Icon(painter = painterResource(
                                    id = R.drawable.ic_baseline_send_24),
                                    contentDescription = "Send Message"
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 4.dp,
                                end = 4.dp,
                                bottom = 4.dp
                            )
                    )
                }
            }


        }

    }

}


@Composable
fun PrivateChatMessageItem(
    privateChatMessage: PrivateChatMessage,
    isReceived: Boolean,
    isFirst: Boolean
) {

    Column(
        modifier = Modifier.fillMaxWidth()
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
                        .background(color = MaterialTheme.colors.surface.copy(alpha = 0.7f))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colors.onSurface.copy(0.7f),
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
                        .background(color = MaterialTheme.colors.primaryVariant.copy(alpha = 0.4f))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colors.onSurface.copy(0.7f),
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
                                id= if(!privateChatMessage.received) R.drawable.message_sent_not_received
                                    else if(privateChatMessage.received && !privateChatMessage.seen) R.drawable.ic_message_received
                                    else R.drawable.ic_message_seen
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

