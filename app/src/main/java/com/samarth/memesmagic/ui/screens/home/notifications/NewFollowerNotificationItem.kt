package com.samarth.memesmagic.ui.screens.home.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.samarth.memesmagic.notification.models.NewFollowerNotification
import com.samarth.memesmagic.ui.components.ProfileImage
import com.samarth.memesmagic.util.getNotificationTime

@Composable
fun NewFollowerNotificationItem(
    notification:NewFollowerNotification,
    time:Long,
    modifier:Modifier = Modifier
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        ProfileImage(
            name = notification.follower.name,
            imageUrl = notification.follower.profilePic,
            modifier = Modifier
                .padding(start = 4.dp, end = 8.dp)
                .size(48.dp)
        )
        Column (
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.Bold)
                    ){
                        append(notification.follower.name)
                    }
                    append(" started following You!")
                },
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = getNotificationTime(time),
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }

}