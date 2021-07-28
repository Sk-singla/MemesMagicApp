package com.samarth.memesmagic.ui.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.request.ImageRequest
import com.google.accompanist.coil.rememberCoilPainter
import com.samarth.memesmagic.R
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatRoom
import com.samarth.memesmagic.ui.components.CustomButton
import com.samarth.memesmagic.util.ChatUtils
import com.samarth.memesmagic.util.Screens.CHAT_ROOM_SCREEN
import com.samarth.memesmagic.util.Screens.FIND_ANOTHER_USER_FOR_CHAT
import com.samarth.memesmagic.util.TokenHandler.getEmail
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
        chatViewModel.observeLocalDatabase(context)
    }

    Scaffold(
        scaffoldState = scaffoldState
    ) {


        if(chatViewModel.chatRooms.value.isNotEmpty()){
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                items(
                    chatViewModel.chatRooms.value.sortedByDescending {
                        it.lastMessage?.timeStamp
                    }
                ) { chatRoom ->
                    PrivateChatRoomItem(
                        privateChatRoom = chatRoom,
                        onChatRoomClick = {
                            ChatUtils.currentChatRoom = chatRoom
                            navController.navigate(CHAT_ROOM_SCREEN)
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("Clicked!")
                            }
                        }
                    )

                }

            }
        } else {
            Box(
                modifier =Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column{
                    Text(
                        text = "You don't have any chat with anyone!",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    CustomButton(
                        text = "Search Users to chat",
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        navController.navigate(FIND_ANOTHER_USER_FOR_CHAT)
                    }
                }
            }
        }


    }
}


@Composable
fun PrivateChatRoomItem(
    privateChatRoom:PrivateChatRoom,
    modifier: Modifier = Modifier,
    onChatRoomClick: () -> Unit
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .clickable { onChatRoomClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = rememberCoilPainter(
                request = ImageRequest.Builder(LocalContext.current)
                    .data(privateChatRoom.profilePic ?: R.drawable.ic_person)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_error)
                    .build(),
                fadeIn = true,
            ),
            contentScale = ContentScale.Crop,
            contentDescription = "User Image",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.8f),
                    shape = CircleShape
                )
        )

        Column(
            modifier = Modifier.padding(start = 16.dp)
        ){
            Text(
                text = privateChatRoom.name,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if(privateChatRoom.lastMessage != null) {
                Text(
                    text = privateChatRoom.lastMessage!!.message,
                    style = MaterialTheme.typography.body1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

            }

        }


    }


}