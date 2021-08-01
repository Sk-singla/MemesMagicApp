package com.samarth.memesmagic.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.samarth.memesmagic.R
import com.samarth.memesmagic.data.remote.models.MemeBadgeType
import com.samarth.memesmagic.data.remote.response.Reward
import com.samarth.memesmagic.util.getRewardDate

@Composable
fun RewardItem(
    modifier:Modifier = Modifier,
    reward: Reward
) {


    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(
                id = if (reward.memeBadgeType == MemeBadgeType.MEMER_OF_THE_MONTH)
                    R.drawable.month_badge
                else
                    R.drawable.year_badge,
            ),
            contentDescription = "Badge",
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.padding(16.dp))

        Column(
            modifier = Modifier.height(64.dp)
        ) {


            Text(
                text = if (reward.memeBadgeType == MemeBadgeType.MEMER_OF_THE_MONTH) "Memer of the Month" else "Memer of the Year",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = getRewardDate(reward.time,reward.memeBadgeType == MemeBadgeType.MEMER_OF_THE_MONTH),
                style = MaterialTheme.typography.body2
            )

        }
    }
    

    
    
}