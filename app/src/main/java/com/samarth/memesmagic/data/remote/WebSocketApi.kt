package com.samarth.memesmagic.data.remote

import com.samarth.memesmagic.data.remote.ws.models.BaseModel
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Header

interface WebSocketApi {

    @Receive
    fun observeEvent(): Flow<WebSocket.Event>

    @Send
    fun sendBaseModel(baseModel: BaseModel): Boolean

    @Receive
    fun observeBaseModel(): Flow<BaseModel>


}