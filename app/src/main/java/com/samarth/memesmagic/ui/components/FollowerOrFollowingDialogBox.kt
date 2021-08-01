package com.samarth.memesmagic.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.samarth.memesmagic.data.remote.response.UserInfo

@Composable
fun FollowersOrFollowingDialogBox(
    users: List<UserInfo>,
    onDismiss:()->Unit,
    isLoading:Boolean,
    modifier: Modifier = Modifier,
    isFollowingToUser: (userEmail:String)->Boolean,
    followUser: (emailOfUserToFollowOrUnfollow:String, onSuccess:() ->Unit)->Unit,
    unFollowUser:(emailOfUserToFollowOrUnfollow:String, onSuccess:()->Unit) -> Unit,
    onClick: (String)->Unit
) {

    Dialog(
        onDismissRequest = {
               onDismiss()
        },
        content = {
            Surface(
                modifier = modifier
                    .background(color = MaterialTheme.colors.surface),
                shape = RoundedCornerShape(10.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colors.surface,shape = RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {

                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier)
                    } else if (users.isEmpty()) {
                        Text(
                            text = "0 Followers",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            items(users) { user ->
                                UserSearchItem(
                                    userInfo = user,
                                    following = isFollowingToUser(user.email),
                                    onFollowUnFollowBtnPressed = { onSuccess ->
                                        if (isFollowingToUser(user.email)) {
                                            unFollowUser(user.email, onSuccess)
                                        } else {
                                            followUser(user.email, onSuccess)
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                        .clickable { onClick(user.email) }
                                        .padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }

            }
        }
    )

}