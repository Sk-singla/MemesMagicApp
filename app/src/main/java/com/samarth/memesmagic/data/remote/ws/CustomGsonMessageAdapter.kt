package com.samarth.memesmagic.data.remote.ws

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.samarth.data.models.webosocket_models.JoinServerHandshake
import com.samarth.memesmagic.data.remote.ws.models.BaseModel
import com.samarth.memesmagic.data.remote.ws.models.LikePost
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatMessage
import com.samarth.memesmagic.util.Constants.TYPE_JOIN_SERVER_HANDSHAKE
import com.samarth.memesmagic.util.Constants.TYPE_LIKE_POST
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