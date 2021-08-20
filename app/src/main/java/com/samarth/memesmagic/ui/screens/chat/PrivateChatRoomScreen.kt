package com.samarth.memesmagic.ui.screens.chat

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import com.samarth.memesmagic.ui.components.dialogs.ChatMessageLocalDeleteDialog
import com.samarth.memesmagic.ui.components.dialogs.ChatMessageRemoteDeleteDialog
import com.samarth.memesmagic.ui.theme.Green200
import com.samarth.memesmagic.util.*
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@Composable
fun PrivateChatRoomScreen(
    navController: NavController,
    chatViewModel: ChatViewModel
) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit ){
        chatViewModel.createChatRoom()
        chatViewModel.currentUserEmail = TokenHandler.getEmail(context) ?: ""
        chatViewModel.observeSingleChatLocalDatabase()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomTopBar(
                title = if(ChatUtils.currentChatRoom != null && chatViewModel.chatRoomState.value == ChatViewModel.ChatRoomState.NormalState){
                                    ChatUtils.currentChatRoom!!.name
                         } else "",
                actions = {
                    if(chatViewModel.chatRoomState.value is ChatViewModel.ChatRoomState.MessagesSelectedState){
                        IconButton(
                            onClick = {
                                val senders = (chatViewModel.chatRoomState.value as ChatViewModel.ChatRoomState.MessagesSelectedState)
                                    .selectedMessages
                                    .groupBy {
                                        it.from
                                    }
                                if(senders.size == 1 && senders[chatViewModel.currentUserEmail] != null){
                                    chatViewModel.isRemoteDeleteDialogVisible.value = true
                                } else {
                                    chatViewModel.isLocalDeleteDialogVisible.value = true
                                }
                            }
                        ) {
                            Icon(painter = painterResource(id = R.drawable.ic_baseline_delete_24), contentDescription = "Delete Message")
                        }
                    }
                }
            )
        }
    ) {

        BackHandler {
            chatViewModel.currentChatRoomMessageJob?.cancel()
            if(chatViewModel.chatRoomState.value is ChatViewModel.ChatRoomState.MessagesSelectedState){
                (chatViewModel.chatRoomState.value as ChatViewModel.ChatRoomState.MessagesSelectedState).selectedMessages = mutableListOf()
                chatViewModel.chatRoomState.value = ChatViewModel.ChatRoomState.NormalState
                chatViewModel.isMessageSelected.clear()
            }
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
                    chatViewModel.currentChatRoomMessages.value.reversed() ?: listOf(),
                    key = { _, msg ->
                        msg.id
                    }
                ) {  index, privateChatMessage->

                    if(!chatViewModel.isMessageSelected.containsKey(privateChatMessage.id)){
                        chatViewModel.isMessageSelected[privateChatMessage.id] = false
                    }

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
                        isFirst = isFirst,
                        modifier = Modifier
                            .fillMaxWidth()
                            .combinedClickable(
                                onLongClick = {
                                    chatViewModel.isMessageSelected[privateChatMessage.id] = !chatViewModel.isMessageSelected[privateChatMessage.id]!!
//                                    isMessageSelected = !isMessageSelected
                                    if (chatViewModel.isMessageSelected[privateChatMessage.id] == true) {
                                        chatViewModel.onMessageSelection(
                                            privateChatMessage
                                        )
                                    } else {
                                        chatViewModel.onMessageDeselect(
                                            privateChatMessage
                                        )
                                    }
                                },
                                onClick = {
                                    if (chatViewModel.chatRoomState.value is ChatViewModel.ChatRoomState.MessagesSelectedState) {
                                        chatViewModel.isMessageSelected[privateChatMessage.id] = !(chatViewModel.isMessageSelected[privateChatMessage.id] ?: false)
//                                    isMessageSelected = !isMessageSelected
                                        if (chatViewModel.isMessageSelected[privateChatMessage.id] == true) {
                                            chatViewModel.onMessageSelection(
                                                privateChatMessage
                                            )
                                        } else {
                                            chatViewModel.onMessageDeselect(
                                                privateChatMessage
                                            )
                                        }
                                    }
                                }
                            ),
                        isSelected = chatViewModel.isMessageSelected[privateChatMessage.id] == true
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

            if(chatViewModel.isLocalDeleteDialogVisible.value){
                ChatMessageLocalDeleteDialog(
                    title = "Delete Message",
                    onDialogDismiss = {
                                      chatViewModel.isLocalDeleteDialogVisible.value= false
                    },
                    onDelete = {
                        (chatViewModel.chatRoomState.value as ChatViewModel.ChatRoomState.MessagesSelectedState).selectedMessages
                            .forEach {
                                chatViewModel.deleteMessageFromLocalDatabase(
                                    it.id
                                )
                            }
                        (chatViewModel.chatRoomState.value as ChatViewModel.ChatRoomState.MessagesSelectedState).selectedMessages = listOf()
                        chatViewModel.chatRoomState.value = ChatViewModel.ChatRoomState.NormalState
                        chatViewModel.isMessageSelected.clear()
                    }
                )
            }

            if(chatViewModel.isRemoteDeleteDialogVisible.value){
                ChatMessageRemoteDeleteDialog(
                    title = "Delete Message",
                    onDialogDismiss = {
                        chatViewModel.isRemoteDeleteDialogVisible.value= false
                    },
                    onLocalDelete = {
                        (chatViewModel.chatRoomState.value as ChatViewModel.ChatRoomState.MessagesSelectedState).selectedMessages
                            .forEach {
                                chatViewModel.deleteMessageFromLocalDatabase(
                                    it.id
                                )
                            }
                        (chatViewModel.chatRoomState.value as ChatViewModel.ChatRoomState.MessagesSelectedState).selectedMessages = listOf()
                        chatViewModel.chatRoomState.value = ChatViewModel.ChatRoomState.NormalState
                        chatViewModel.isMessageSelected.clear()
                    },
                    onDeleteForEveryone = {

                        (chatViewModel.chatRoomState.value as ChatViewModel.ChatRoomState.MessagesSelectedState).selectedMessages
                            .forEach {
                                chatViewModel.deleteMessageForAll(
                                    it.id,
                                    it.to
                                )
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(it.message)
                                }
                            }
                        (chatViewModel.chatRoomState.value as ChatViewModel.ChatRoomState.MessagesSelectedState).selectedMessages = listOf()
                        chatViewModel.chatRoomState.value = ChatViewModel.ChatRoomState.NormalState
                        chatViewModel.isMessageSelected.clear()
                    }
                )
            }


        }

    }
}

