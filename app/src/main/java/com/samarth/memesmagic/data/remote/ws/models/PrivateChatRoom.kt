package com.samarth.memesmagic.data.remote.ws.models

import androidx.compose.runtime.MutableState
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.samarth.memesmagic.data.remote.response.UserInfo

@Entity
data class PrivateChatRoom(
    @PrimaryKey(autoGenerate = false)
    val userEmail:String,
    val name: String,
    val profilePic: String?,
)
