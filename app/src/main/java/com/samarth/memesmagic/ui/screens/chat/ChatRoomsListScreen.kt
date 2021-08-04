package com.samarth.memesmagic.ui.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.samarth.memesmagic.data.local.entities.relations.PrivateChatRoomWithPrivateChatMessages
import com.samarth.memesmagic.data.remote.models.PrivateChatMessageStatus
import com.samarth.memesmagic.ui.components.CustomTopBar
import com.samarth.memesmagic.ui.components.ProfileImage
import com.samarth.memesmagic.util.ChatUtils
import com.samarth.memesmagic.util.Screens.CHAT_ROOM_SCREEN
import com.samarth.memesmagic.util.Screens.FIND_ANOTHER_USER_FOR_CHAT
import com.samarth.memesmagic.util.TokenHandler.getEmail
import com.samarth.memesmagic.util.getIconAccordingToMessageStatus
import com.samarth.memesmagic.util.getMessageCount
import com.samarth.memesmagic.util.lastChatMessageTime
import kotlinx.coroutines.launch

@Composable
fun ChatRoomsListScreen(
    navController: NavController,
    chatViewModel: ChatViewModel
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current


    LaunchedEffect(key1 = true){
        chatViewModel.currentUserEmail = getEmail(context) ?: ""
        chatViewModel.observeLocalDatabase()

    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomTopBar(
                title = "Chat"
            )
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .size(48.dp)
                    .shadow(
                        4.dp,
                        shape = CircleShape
                    )
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ){
                IconButton(
                    onClick = {
                        navController.navigate(FIND_ANOTHER_USER_FOR_CHAT)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Search users to chat"
                    )
                }
            }
        }
    ) {


        if(chatViewModel.chatRooms.value.isNotEmpty()){
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

//                chatViewModel.chatRooms.value.sortedByDescending {
//                it.lastMessage?.timeStamp
//            }



                itemsIndexed(
                    chatViewModel.chatRooms.value.sortedByDescending {
                        it.privateChatMessages.lastOrNull()?.timeStamp
                    }
                ) { idx, chatRoom ->
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ){
                        PrivateChatRoomItem(
                            privateChatRoom = chatRoom,
                            onChatRoomClick = {
                                ChatUtils.currentChatRoom = chatRoom.privateChatRoom
                                navController.navigate(CHAT_ROOM_SCREEN)
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar("Clicked!")
                                }
                            },
                            currentUserEmail = chatViewModel.currentUserEmail
                        )

                        if(idx < chatViewModel.chatRooms.value.size -1)
                            Divider(modifier = Modifier.fillMaxWidth())
                    }
                }

            }
        } else {
            Box(
                modifier =Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "You don't have any chat with anyone!",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    textAlign = TextAlign.Center
                )
            }
        }


    }
}


/**
 * =========================== CHAT ROOM ITEM =========================
 */


@Composable
fun PrivateChatRoomItem(
    privateChatRoom:PrivateChatRoomWithPrivateChatMessages,
    modifier: Modifier = Modifier,
    currentUserEmail:String,
    onChatRoomClick: () -> Unit
) {

    val lastChatMessage = privateChatRoom.privateChatMessages.lastOrNull()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onChatRoomClick() }
            .padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        ProfileImage(
            name = privateChatRoom.privateChatRoom.name,
            imageUrl = privateChatRoom.privateChatRoom.profilePic,
            modifier = Modifier
                .size(48.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ){
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = privateChatRoom.privateChatRoom.name,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = lastChatMessage?.timeStamp?.let { lastChatMessageTime(it) } ?: "", //privateChatRoom.lastMessage?.timeStamp?.let { lastChatMessageTime(it) } ?: "",
                    style = MaterialTheme.typography.body2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if(lastChatMessage != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        /**
                         *  IF LAST MESSAGE IS FROM CURRENT USER ======= ( NOT RECEIVED )
                         */
                        if (lastChatMessage.from == currentUserEmail) {
                            Image(
                                painter = painterResource(
                                    id = getIconAccordingToMessageStatus(lastChatMessage.msgStatus)
                                ),
                                contentDescription = "Message Status",
                                modifier = Modifier
                                    .height(16.dp)
                                    .padding(end = 4.dp),
                                colorFilter = ColorFilter.tint(
                                    MaterialTheme.colors.onSurface
                                )
                            )
                        }
                        Text(
                            text = lastChatMessage.message,
                            style = MaterialTheme.typography.body1,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                    }



                    if (lastChatMessage.from != currentUserEmail && lastChatMessage.msgStatus != PrivateChatMessageStatus.SEEN) {
                        Box(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(20.dp)
                                .background(
                                    color = MaterialTheme.colors.primary,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = getMessageCount(
                                    privateChatRoom.privateChatMessages.filter {
                                        it.from != currentUserEmail && it.msgStatus == PrivateChatMessageStatus.RECEIVED
                                    }.size
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 1,
                                textAlign = TextAlign.Center,
                                fontSize = 8.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                }

            }

        }


    }


}