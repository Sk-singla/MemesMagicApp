package com.samarth.memesmagic.data.remote.response

import com.samarth.memesmagic.data.remote.models.MemeBadgeType

data class Reward(
    val memeBadgeType: MemeBadgeType,
    val time:Long,
    val userEmail: String,
    val id:String
)
