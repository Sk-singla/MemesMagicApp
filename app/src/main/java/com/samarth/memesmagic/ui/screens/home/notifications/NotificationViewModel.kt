package com.samarth.memesmagic.ui.screens.home.notifications

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.plcoding.doodlekong.util.DispatcherProvider
import com.samarth.memesmagic.data.local.entities.models.LocalNotification
import com.samarth.memesmagic.data.remote.ws.models.BaseModel
import com.samarth.memesmagic.notification.models.BaseNotification
import com.samarth.memesmagic.notification.models.BaseNotification.Companion.NOTIFICATION_TYPE_NEW_FOLLOWER
import com.samarth.memesmagic.notification.models.NewFollowerNotification
import com.samarth.memesmagic.repository.MemeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    val memeRepo:MemeRepo,
    val dispatcherProvider: DispatcherProvider,
):ViewModel(){

    val notifications = mutableStateListOf<LocalNotification>()

    init {
        getAllNotifications()
    }
    fun getAllNotifications() = viewModelScope.launch(dispatcherProvider.io){
        memeRepo.getAllNotifications().collect {
            notifications.clear()
            notifications.addAll(it)
        }
    }


    fun getOriginalNotification(notification: String):BaseNotification{
        val jsonObj = JsonParser.parseString(notification).asJsonObject
        val type = when(jsonObj.get("type").asString){
            NOTIFICATION_TYPE_NEW_FOLLOWER -> NewFollowerNotification::class.java
            else -> BaseNotification::class.java
        }
        return Gson().fromJson(notification,type)
    }


    fun notificationSeen(notificationId: String) = viewModelScope.launch(dispatcherProvider.io){
        memeRepo.seenNotification(notificationId)
    }

}