package com.samarth.memesmagic.ui.components

import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.request.ImageRequest
import com.google.accompanist.coil.rememberCoilPainter
import com.samarth.memesmagic.R
import com.samarth.memesmagic.data.remote.models.MemeBadgeType
import com.samarth.memesmagic.data.remote.response.Reward
import com.samarth.memesmagic.data.remote.response.UserInfo
import com.samarth.memesmagic.ui.theme.Green700

@Composable
fun CongratsDialogBox(
    showDialog:Boolean,
    onDismiss:()->Unit,
    reward: Reward,
    onClick:()->Unit
) {

    if (showDialog) {

        Dialog(
            onDismissRequest = {
                onDismiss()
            }
        ) {
            Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(
                        id = if (reward.memeBadgeType == MemeBadgeType.MEMER_OF_THE_MONTH)
                            R.drawable.memer_of_the_month
                        else
                            R.drawable.memer_of_the_year
                    ),
                    contentDescription = "Badge",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onClick()
                        }
                )
            }
        }
    }
}


@Composable
fun AdvertiseDialogBox(
    showDialog:Boolean,
    onDismiss:()->Unit,
    reward: Reward,
    rewardWinnerInfo:UserInfo,
    isFollowingToUser:Boolean,
    onFollowBtnPressed:()->Unit,
    onClick:()->Unit
) {
    if (showDialog) {

        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            buttons = {
                Box(modifier = Modifier.fillMaxWidth().padding(8.dp),contentAlignment = Alignment.CenterEnd) {
                    OutlinedButton(
                        onClick = {
                            onFollowBtnPressed()
                        },
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .shadow(2.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = MaterialTheme.colors.surface,
                            contentColor = Green700
                        ),
                        enabled = !isFollowingToUser
                    ) {
                        Text(text = if (isFollowingToUser) "Following!" else "Follow")
                    }
                }
            },
            title = {
                Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically){
                    Image(
                        painter = painterResource(
                            id = if (reward.memeBadgeType == MemeBadgeType.MEMER_OF_THE_MONTH)
                                R.drawable.month_badge
                            else
                                R.drawable.year_badge,
                        ),
                        contentDescription = "Badge",
                        modifier = Modifier.size(48.dp)
                    )
                    
                    Spacer(modifier = Modifier.padding(16.dp))

                    Text(
                        text = if (reward.memeBadgeType == MemeBadgeType.MEMER_OF_THE_MONTH) "Memer of the Month" else "Memer of the Year",
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Image(
                            painter = rememberCoilPainter(
                                request = ImageRequest.Builder(LocalContext.current)
                                    .data(rewardWinnerInfo.profilePic ?: R.drawable.ic_person)
                                    .placeholder(R.drawable.ic_person)
                                    .error(R.drawable.ic_person)
                                    .build(),
                                fadeIn = true,
                            ),
                            contentScale = ContentScale.Crop,
                            contentDescription = "User Image",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .clickable {
                                    onClick()
                                },
                        )
                        
                        Spacer(modifier = Modifier.padding(24.dp))

                        Text(
                            text = rewardWinnerInfo.name,
                            modifier = Modifier.clickable {
                                onClick()
                            },
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.Bold
                        )
                    }


            }
        )
    }
}



