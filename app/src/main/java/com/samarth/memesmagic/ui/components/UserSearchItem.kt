package com.samarth.memesmagic.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import com.google.accompanist.coil.rememberCoilPainter
import com.samarth.memesmagic.R
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.ui.theme.Green700

@Composable
fun UserSearchItem(
    userInfo: UserInfo,
    following: Boolean,
    onFollowUnFollowBtnPressed: (onSuccess:()->Unit) -> Unit,
    modifier: Modifier = Modifier
) {

    var isFollowingToUser by remember {
        mutableStateOf(following)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {


        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = rememberCoilPainter(
                    request = ImageRequest.Builder(LocalContext.current)
                        .data(userInfo.profilePic ?: R.drawable.ic_person)
                        .placeholder(R.drawable.ic_person)
                        .error(R.drawable.ic_person)
                        .build(),
                    fadeIn = true,
                ),
                contentScale = ContentScale.Crop,
                contentDescription = "User Image",
                modifier = Modifier
                    .padding(start = 8.dp,end = 16.dp)
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.8f),
                        shape = CircleShape
                    )
            )


            Text(
                text = userInfo.name,
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }



        OutlinedButton(onClick = {
                onFollowUnFollowBtnPressed {
                    isFollowingToUser = !isFollowingToUser
                }
            },
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .shadow(2.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = Green700
            )
        ) {
            Text(text = if(isFollowingToUser) "Unfollow" else "Follow")
        }


    }

















}