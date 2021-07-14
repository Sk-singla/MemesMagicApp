package com.samarth.memesmagic.data.remote.response.fcm_messages

abstract class FcmMessage(
    val type: String
)

data class FcmMessageData (
    val fcmType: String
):FcmMessage(fcmType)