package com.samarth.memesmagic.data.local.entities.models

import com.samarth.memesmagic.data.remote.ws.models.PrivateChatMessage

data class LocalPrivateChatMessage(
    val privateChatMessage: PrivateChatMessage,
    var isSelected: Boolean
)
