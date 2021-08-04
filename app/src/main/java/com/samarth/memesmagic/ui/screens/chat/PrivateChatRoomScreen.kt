package com.samarth.memesmagic.ui.screens.chat

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.samarth.memesmagic.R
import com.samarth.memesmagic.data.remote.models.PrivateChatMessageStatus
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatMessage
import com.samarth.memesmagic.ui.components.CustomTextField
import com.samarth.memesmagic.ui.components.CustomTopBar
import com.samarth.memesmagic.ui.theme.Green200
import com.samarth.memesmagic.util.*

@Composable
fun PrivateChatRoomScreen(
    navController: NavController,
    chatViewModel: ChatViewModel
) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit ){
        chatViewModel.createChatRoom()
        chatViewModel.currentUserEmail = TokenHandler.getEmail(context) ?: ""
        chatViewModel.observeSingleChatLocalDatabase()

//        if(chatViewModel.chatRoomMessages.value[ChatUtils.currentChatRoom?.userEmail] == null){
//            chatViewModel.chatRoomMessages.value[ChatUtils.currentChatRoom?.userEmail ?: ""] = listOf()
//        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomTopBar(
                title = ChatUtils.currentChatRoom?.name ?: "",
            )
        }
    ) {

        BackHandler {
            chatViewModel.currentChatRoomMessageJob?.cancel()
            navController.popBackStack()
        }

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

                itemsIndexed(
                    chatViewModel.currentChatRoomMessages.value.reversed() ?: listOf()
                ) {  index, privateChatMessage->

                    val isReceived = privateChatMessage.to == chatViewModel.currentUserEmail
                    val isFirst = index == chatViewModel.currentChatRoomMessages.value.size -1 || chatViewModel.currentChatRoomMessages.value[index+1].from != chatViewModel.currentChatRoomMessages.value[index].from

                    if(isReceived && privateChatMessage.msgStatus == PrivateChatMessageStatus.SENT){
                        chatViewModel.messageReceived(
                            msgId = privateChatMessage.id,
                            messageSender = privateChatMessage.from
                        )
                    }

                    if(isReceived && privateChatMessage.msgStatus == PrivateChatMessageStatus.RECEIVED){
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


            // ========= MESSAGE EDIT TEXT ==============

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(color = MaterialTheme.colors.background),
                contentAlignment = Alignment.Center
            ){
                Row {
                    CustomTextField(
                        value = chatViewModel.currentMessage.value,
                        onValueChange = {
                            chatViewModel.currentMessage.value = it
                        },
                        placeholder = "Enter a message...",
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

