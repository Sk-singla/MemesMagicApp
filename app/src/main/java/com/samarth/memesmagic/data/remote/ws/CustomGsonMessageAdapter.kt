package com.samarth.memesmagic.data.remote.ws

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.samarth.data.models.webosocket_models.JoinServerHandshake
import com.samarth.memesmagic.data.remote.ws.models.*
import com.samarth.memesmagic.util.Constants.TYPE_DELETE_MESSAGE
import com.samarth.memesmagic.util.Constants.TYPE_DISCONNECT_REQUEST
import com.samarth.memesmagic.util.Constants.TYPE_JOIN_SERVER_HANDSHAKE
import com.samarth.memesmagic.util.Constants.TYPE_LIKE_POST
import com.samarth.memesmagic.util.Constants.TYPE_MESSAGE_RECEIVED
import com.samarth.memesmagic.util.Constants.TYPE_MESSAGE_RECEIVED_ON_SERVER_ACK
import com.samarth.memesmagic.util.Constants.TYPE_MESSAGE_SEEN
import com.samarth.memesmagic.util.Constants.TYPE_PRIVATE_CHAT_MESSAGE
import com.tinder.scarlet.Message
import com.tinder.scarlet.MessageAdapter
import java.lang.reflect.Type



class CustomGsonMessageAdapter<T> private constructor(
    val gson: Gson
): MessageAdapter<T> {

    override fun fromMessage(message: Message): T {
        val stringValue = when(message) {
            is Message.Text -> message.value
            is Message.Bytes -> message.value.toString()
        }

        val jsonObject = JsonParser.parseString(stringValue).asJsonObject
        val type = when(jsonObject.get("type").asString) {
            TYPE_LIKE_POST -> LikePost::class.java
            TYPE_PRIVATE_CHAT_MESSAGE -> PrivateChatMessage::class.java
            TYPE_JOIN_SERVER_HANDSHAKE -> JoinServerHandshake::class.java
            TYPE_DISCONNECT_REQUEST -> DisconnectRequest::class.java
            TYPE_MESSAGE_RECEIVED -> MessageReceived::class.java
            TYPE_MESSAGE_SEEN -> MessageSeen::class.java
            TYPE_MESSAGE_RECEIVED_ON_SERVER_ACK -> MessageReachedServerAcknowledgement::class.java
            TYPE_DELETE_MESSAGE -> DeleteMessage::class.java
            else -> BaseModel::class.java
        }
        val obj = gson.fromJson(stringValue,type)
        return obj as T
    }

    override fun toMessage(data: T): Message {
        var convertedData = data as BaseModel
        convertedData = when(convertedData.type) {
            TYPE_LIKE_POST -> convertedData as LikePost
            TYPE_PRIVATE_CHAT_MESSAGE -> convertedData as PrivateChatMessage
            TYPE_JOIN_SERVER_HANDSHAKE -> convertedData as JoinServerHandshake
            TYPE_DISCONNECT_REQUEST -> convertedData as DisconnectRequest
            TYPE_MESSAGE_SEEN -> convertedData as MessageSeen
            TYPE_MESSAGE_RECEIVED -> convertedData as MessageReceived
            TYPE_MESSAGE_RECEIVED_ON_SERVER_ACK -> convertedData as MessageReachedServerAcknowledgement
            TYPE_DELETE_MESSAGE -> convertedData as DeleteMessage
            else -> convertedData
        }

        return Message.Text(gson.toJson(convertedData))
    }

    class Factory(
        private val gson: Gson
    ): MessageAdapter.Factory {
        override fun create(type: Type, annotations: Array<Annotation>): MessageAdapter<*> {
            return CustomGsonMessageAdapter<Any>(gson)
        }
    }
}