package com.samarth.memesmagic.ui.screens.chat

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.doodlekong.util.DispatcherProvider
import com.samarth.data.models.webosocket_models.JoinServerHandshake
import com.samarth.memesmagic.data.remote.WebSocketApi
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatMessage
import com.tinder.scarlet.WebSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    val webSocketApi: WebSocketApi,
    val dispatchers: DispatcherProvider
): ViewModel() {


    private val connectionEventChannel = Channel<WebSocket.Event>()
    val connectionEvent = connectionEventChannel.receiveAsFlow().flowOn(dispatchers.io)

    init {
        listenToConnectionEvent()
        send()

    }

    fun send() = viewModelScope.launch {
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
        webSocketApi.sendBaseModel(
            PrivateChatMessage(
                "asd",
                "abc@gmail.com",
                "helllo",
                1234,
                "asdff"
            )
        )
    }
    fun listenToConnectionEvent() = viewModelScope.launch(dispatchers.io){
        webSocketApi.observeEvent().collect { event->
            connectionEventChannel.send(event)
        }
    }

    fun oberserve(context: Context)  = viewModelScope.launch(dispatchers.io){
        webSocketApi.observeBaseModel().collect { model ->
            when(model){
                is PrivateChatMessage -> {
                    Log.d("private chat",model.message)
                    withContext(dispatchers.main){
                        Toast.makeText(context, model.message, Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {
                    Log.d("model","$model")
                    withContext(dispatchers.main){
                        Toast.makeText(context, model.type, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

    }



}