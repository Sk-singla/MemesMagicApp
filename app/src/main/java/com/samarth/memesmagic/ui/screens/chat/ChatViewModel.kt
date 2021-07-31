package com.samarth.memesmagic.ui.screens.chat

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.doodlekong.util.DispatcherProvider
import com.samarth.data.models.webosocket_models.JoinServerHandshake
import com.samarth.memesmagic.data.local.dao.MemeDao
import com.samarth.memesmagic.data.remote.WebSocketApi
import com.samarth.memesmagic.data.remote.ws.models.*
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.util.ChatUtils
import com.samarth.memesmagic.util.TokenHandler.getEmail
import com.tinder.scarlet.WebSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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


    fun observeConnectionEvents() = viewModelScope.launch(dispatchers.io){
        connectionEvent.collect { event ->
            when(event){
                is WebSocket.Event.OnConnectionOpened<*> -> {
                    connect()
                }
                else -> Unit
            }
        }
    }


    fun sendChatMessage() = viewModelScope.launch {
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

            // todo: optimize getting rooms list

            privateMessages.forEach { privateChatMessage->
                val isMessageReceived = privateChatMessage.to == curUserEmail
                val otherUserEmail = if(isMessageReceived) privateChatMessage.from else privateChatMessage.to

                val tempChatRoom = chatRooms.value.find { it.userEmail ==  otherUserEmail}
                if(tempChatRoom != null){
                    chatRooms.value -= tempChatRoom
                    chatRooms.value += PrivateChatRoom(
                        userEmail = otherUserEmail,
                        name = privateChatMessage.name,
                        profilePic = privateChatMessage.profilePic,
                        lastMessage = privateChatMessage
                    )
                } else {
                    chatRooms.value += PrivateChatRoom(
                        userEmail = otherUserEmail,
                        name = privateChatMessage.name,
                        profilePic = privateChatMessage.profilePic,
                        lastMessage = privateChatMessage
                    )
                }
            }
        }
    }

    fun observeWebSocketBaseModelEvents()  = viewModelScope.launch(dispatchers.io){
        webSocketApi.observeBaseModel().collect { data ->
            when(data){
                is PrivateChatMessage -> {
                    memeDao.savePrivateMessage(data)
                    messageReceived(data.id,data.from)
                }
                is MessageReceived -> {
                    memeDao.messageReceived(data.msgId)
                }
                is MessageSeen -> {
                    memeDao.messageSeen(data.msgId)
                }
                else -> Unit
            }
        }
    }

    fun messageSeen(msgId:String,messageSender:String) = viewModelScope.launch(dispatchers.io){
        webSocketApi.sendBaseModel(
            MessageSeen(
                msgId = msgId,
                msgSender = messageSender
            )
        )
        memeDao.messageSeen(msgId)
    }
    fun messageReceived(msgId:String,messageSender:String) = viewModelScope.launch(dispatchers.io){
        webSocketApi.sendBaseModel(
            MessageReceived(
                msgId = msgId,
                msgSender = messageSender
            )
        )
        memeDao.messageReceived(msgId)
    }


    fun disconnect(){
        webSocketApi.sendBaseModel(DisconnectRequest())
    }
    fun connect(){
        webSocketApi.sendBaseModel(JoinServerHandshake())
    }


    private fun clearChatMessageTextField(){
        currentMessage.value = ""
    }

}