package com.samarth.memesmagic.data.remote.response.fcm_messages

import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.util.Constants.FCM_TYPE_FOLLOWER_ADDED

data class FcmFollowerAddedMessage(
    val message: String,
    val followerInfo: UserInfo
): FcmMessage(FCM_TYPE_FOLLOWER_ADDED)
