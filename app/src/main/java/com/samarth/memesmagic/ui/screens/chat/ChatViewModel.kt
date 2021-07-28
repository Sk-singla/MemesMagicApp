package com.samarth.memesmagic.ui.screens.chat

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.doodlekong.util.DispatcherProvider
import com.samarth.data.models.webosocket_models.JoinServerHandshake
import com.samarth.memesmagic.data.local.dao.MemeDao
import com.samarth.memesmagic.data.remote.WebSocketApi
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatMessage
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatRoom
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.util.ChatUtils
import com.samarth.memesmagic.util.Constants.CHAT_MESSAGES_LIMIT
import com.samarth.memesmagic.util.TokenHandler.getEmail
import com.tinder.scarlet.WebSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    val webSocketApi: WebSocketApi,
    val memeRepo: MemeRepo,
    val dispatchers: DispatcherProvider,
    val memeDao: MemeDao
): ViewModel() {


    private val connectionEventChannel = Channel<WebSocket.Event>()
    val connectionEvent = connectionEventChannel.receiveAsFlow().flowOn(dispatchers.io)


    val chatRooms = mutableStateOf(listOf<PrivateChatRoom>())

    val currentChatRoomMessages = mutableStateOf(mutableListOf<PrivateChatMessage>())
    val currentMessage = mutableStateOf("")

    var currentUserEmail = ""
    var openedChatRoomsListFirstTime = true


    init {
        listenToConnectionEvent()
        send()
        observeWebSocketModelEvents()
    }

    fun send() = viewModelScope.launch(dispatchers.io){
        connectionEvent.collect { event ->
            when(event){
                is WebSocket.Event.OnConnectionOpened<*> -> {
                    webSocketApi.sendBaseModel(
                        JoinServerHandshake()
                    )
                }
                else -> Unit
            }
        }
    }


    fun sendMessage() = viewModelScope.launch {
        val chatMessage = currentMessage.value.trim()
        if(chatMessage.isEmpty() || ChatUtils.currentChatRoom == null){
            return@launch
        }

        val privateChatMessage = PrivateChatMessage(
            from = currentUserEmail,
            to = ChatUtils.currentChatRoom!!.userEmail,
            message = chatMessage,
            timeStamp = System.currentTimeMillis(),
            id = UUID.randomUUID().toString(),
            name = ChatUtils.currentChatRoom!!.name,
            profilePic = ChatUtils.currentChatRoom!!.profilePic
        )
        webSocketApi.sendBaseModel(privateChatMessage)
        memeDao.savePrivateMessage(privateChatMessage)
        clearChatMessageTextField()
    }

    fun listenToConnectionEvent() = viewModelScope.launch(dispatchers.io){
        webSocketApi.observeEvent().collect { event->
            connectionEventChannel.send(event)
        }
    }


    fun observeSingleChatLocalDatabase(
        userEmail: String = ChatUtils.currentChatRoom?.userEmail ?: ""
    ) = viewModelScope.launch{
        memeDao.getAllMessagesFromUser(
            email = userEmail,
        ).collect { privateMessages ->
            currentChatRoomMessages.value = privateMessages.toMutableList()

            // todo: remove this every time sorting
            currentChatRoomMessages.value.sortByDescending {
                it.timeStamp
            }
        }
    }

    fun observeLocalDatabase(context: Context) = viewModelScope.launch(dispatchers.io){
        val curUserEmail = getEmail(context) ?: ""
        memeDao.getAllMessages().collect { privateMessages ->

             chatRooms.value = privateMessages.groupBy { privateChatMessage ->
                val isMessageReceived = privateChatMessage.to == curUserEmail
                if(isMessageReceived){
                    privateChatMessage.from
                } else {
                    privateChatMessage.to
                }
             }.entries.map { map ->
                val lastMessage = map.value.last()
                PrivateChatRoom(
                    userEmail = map.key,
                    name = lastMessage.name,
                    profilePic = lastMessage.profilePic,
                    lastMessage = lastMessage
                )
             }


        }
    }

    fun observeWebSocketModelEvents()  = viewModelScope.launch(dispatchers.io){
        webSocketApi.observeBaseModel().collect { data ->
            when(data){
                is PrivateChatMessage -> {
                    Log.d("private_chat",data.message)
                    memeDao.savePrivateMessage(data)
                }
                else -> Unit
            }

        }
    }



    private fun clearChatMessageTextField(){
        currentMessage.value = ""
    }

}