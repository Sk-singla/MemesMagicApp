package com.samarth.memesmagic.data.local.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatMessage
import com.samarth.memesmagic.data.remote.ws.models.PrivateChatRoom

data class PrivateChatRoomWithPrivateChatMessages(
    @Embedded
    val privateChatRoom: PrivateChatRoom,
    @Relation (
        parentColumn = "userEmail",
        entityColumn = "otherUserEmail"
    )
    val privateChatMessages: List<PrivateChatMessage>
)