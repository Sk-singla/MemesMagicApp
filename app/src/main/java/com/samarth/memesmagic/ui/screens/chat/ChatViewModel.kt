package com.samarth.memesmagic.ui.screens.chat

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.doodlekong.util.DispatcherProvider
import com.samarth.data.models.webosocket_models.JoinServerHandshake
import com.samarth.memesmagic.data.local.dao.MemeDao
import com.samarth.memesmagic.data.local.entities.relations.PrivateChatRoomWithPrivateChatMessages
import com.samarth.memesmagic.data.remote.WebSocketApi
import com.samarth.memesmagic.data.remote.models.PrivateChatMessageStatus
import com.samarth.memesmagic.data.remote.ws.models.*
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.util.ChatUtils
import com.samarth.memesmagic.util.TokenHandler.getEmail
import com.tinder.scarlet.WebSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

@HiltViewModel
class ChatViewModel @Inject constructor(
    val webSocketApi: WebSocketApi,
    val memeRepo: MemeRepo,
    val dispatchers: DispatcherProvider,
    val memeDao: MemeDao
): ViewModel() {


    private val connectionEventChannel = Channel<WebSocket.Event>()
    val connectionEvent = connectionEventChannel.receiveAsFlow().flowOn(dispatchers.io)


    val chatRooms = mutableStateOf(listOf<PrivateChatRoomWithPrivateChatMessages>())
    val currentChatRoomMessages = mutableStateOf(mutableListOf<PrivateChatMessage>())
    var currentChatRoomMessageJob: Job? = null
    val currentMessage = mutableStateOf("")
    var currentUserEmail = ""
    var unSeenMessagesCount = mutableStateOf(0)


    fun observeConnectionEvents() = viewModelScope.launch(dispatchers.io){
        connectionEvent.collect { event ->
            when(event){
                is WebSocket.Event.OnConnectionOpened<*> -> {
                    connect()
                    getAllMessagesOfStatusLocalAndSendThemAgain()
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
            profilePic = ChatUtils.currentChatRoom!!.profilePic,
            otherUserEmail = ChatUtils.currentChatRoom!!.userEmail
        )
        webSocketApi.sendBaseModel(privateChatMessage)
        savePrivateChatMessageToLocalDatabase(privateChatMessage)
        clearChatMessageTextField()
    }

    fun listenToConnectionEvent() = viewModelScope.launch(dispatchers.io){
        webSocketApi.observeEvent().collect { event->
            connectionEventChannel.send(event)
        }
    }

    fun getAllMessagesOfStatusLocalAndSendThemAgain() = viewModelScope.launch(dispatchers.io){
        memeDao.getAllMessagesWhereStatus(PrivateChatMessageStatus.LOCAL.name).forEach {
            Log.d("local",it.message)
            webSocketApi.sendBaseModel(it)
        }
    }

    fun createChatRoom(
        privateChatRoom: PrivateChatRoom = ChatUtils.currentChatRoom!!
    ) = viewModelScope.launch(dispatchers.io){
        memeDao.insertPrivateChatRoom(privateChatRoom)
    }
    fun observeSingleChatLocalDatabase(
        userEmail: String = ChatUtils.currentChatRoom?.userEmail ?: ""
    ) {
        currentChatRoomMessageJob = viewModelScope.launch(dispatchers.io){
            memeDao.getAllMessagesFromUser(
                email = userEmail,
            )?.collect { privateMessages ->
                currentChatRoomMessages.value = privateMessages.privateChatMessages.toMutableList()
            }
        }
    }

    fun observeLocalDatabase() = viewModelScope.launch(dispatchers.io){
//        val curUserEmail = getEmail(context) ?: ""
        memeDao.getAllPrivateChatRooms()?.collect { privateChatRooms ->
            chatRooms.value = privateChatRooms
        }
    }

    suspend fun savePrivateChatMessageToLocalDatabase(data: PrivateChatMessage){
        memeDao.savePrivateMessage(data)
        memeDao.insertPrivateChatRoom(
            PrivateChatRoom(
                data.otherUserEmail,
                data.name,
                data.profilePic
            )
        )
    }

    fun observeWebSocketBaseModelEvents()  = viewModelScope.launch(dispatchers.io){
        webSocketApi.observeBaseModel().collect { data ->
            when(data){
                is PrivateChatMessage -> {
                    savePrivateChatMessageToLocalDatabase(data)
                    messageReceived(data.id,data.from)
                }
                is MessageReachedServerAcknowledgement -> {
                    memeDao.updatePrivateChatMessageStatus(data.msgId,PrivateChatMessageStatus.SENT.name)
                }
                is MessageReceived -> {
                    memeDao.updatePrivateChatMessageStatus(data.msgId,PrivateChatMessageStatus.RECEIVED.name)
                }
                is MessageSeen -> {
                    memeDao.updatePrivateChatMessageStatus(data.msgId,PrivateChatMessageStatus.SEEN.name)
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
        memeDao.updatePrivateChatMessageStatus(msgId,PrivateChatMessageStatus.SEEN.name)
    }
    fun messageReceived(msgId:String,messageSender:String) = viewModelScope.launch(dispatchers.io){
        webSocketApi.sendBaseModel(
            MessageReceived(
                msgId = msgId,
                msgSender = messageSender
            )
        )
        memeDao.updatePrivateChatMessageStatus(msgId,PrivateChatMessageStatus.RECEIVED.name)
    }

    fun observeUnseenMessages(
        context:Context
    ) = viewModelScope.launch(dispatchers.io){
        memeDao.getAllUnSeenMessagesCount(
            getEmail(context)!!
        ).collect {
            unSeenMessagesCount.value = it
        }
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