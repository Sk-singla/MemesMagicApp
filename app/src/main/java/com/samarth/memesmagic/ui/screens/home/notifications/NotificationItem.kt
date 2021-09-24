package com.samarth.memesmagic.ui.screens.home.notifications

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.samarth.memesmagic.data.local.entities.models.LocalNotification
import com.samarth.memesmagic.notification.models.BaseNotification
import com.samarth.memesmagic.notification.models.NewFollowerNotification
import com.samarth.memesmagic.util.Screens

@Composable
fun NotificationItem(
    localNotification:LocalNotification,
    navController: NavController,
    modifier: Modifier = Modifier,
    getOriginalNotification:(String)->BaseNotification
) {
    when(val notification = getOriginalNotification(localNotification.notification)){
        is NewFollowerNotification -> {
            NewFollowerNotificationItem(
                notification = notification,
                time = localNotification.time,
                modifier = modifier.clickable {
                    navController.navigate("${Screens.ANOTHER_USER_PROFILE_SCREEN}/${notification.follower.email}")
                }
            )
        }
        else ->Unit
    }
}