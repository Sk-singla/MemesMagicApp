package com.samarth.memesmagic.data.remote.response

import com.samarth.memesmagic.data.remote.models.MemeBadgeType

data class MemeBadge(
    val memeBadgeType: MemeBadgeType,
    val time:Long
)
