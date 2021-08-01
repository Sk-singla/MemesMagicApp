package com.samarth.memesmagic.data.local.coverters

import androidx.room.TypeConverter
import com.samarth.memesmagic.data.remote.models.PrivateChatMessageStatus

class PrivateChatMessageConverters {

    @TypeConverter
    fun toPrivateChatMessageStatus(value:String) = enumValueOf<PrivateChatMessageStatus>(value)

    @TypeConverter
    fun fromPrivateChatMessageStatus(value:PrivateChatMessageStatus) = value.name
}