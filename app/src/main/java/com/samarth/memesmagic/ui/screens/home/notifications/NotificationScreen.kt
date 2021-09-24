package com.samarth.memesmagic.ui.screens.home.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.samarth.memesmagic.ui.components.CustomTopBar

@Composable
fun NotificationsScreen(
    parentNavController: NavController,
    notificationViewModel: NotificationViewModel = hiltViewModel()
) {

    if(notificationViewModel.notifications.size <= 0){
        Box(
            modifier =Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No Notification!",
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            items(notificationViewModel.notifications.size) {
                if(!notificationViewModel.notifications[it].seen)
                    notificationViewModel.notificationSeen(notificationViewModel.notifications[it].notificationId)


                NotificationItem(
                    localNotification = notificationViewModel.notifications[it],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    getOriginalNotification = notificationViewModel::getOriginalNotification,
                    navController = parentNavController
                )
            }
            
            item { 
                Spacer(
                    modifier = Modifier
                    .fillMaxWidth()
                    .padding(64.dp)
                )
            }
        }
    }

}