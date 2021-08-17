package com.samarth.memesmagic.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.samarth.memesmagic.data.remote.response.Reward

@Composable
fun RewardsDialogBox(
    rewards: List<Reward>,
    onDismiss:()->Unit,
    isLoading:Boolean,
    modifier: Modifier = Modifier
) {

    Dialog(
        onDismissRequest = {
               onDismiss()
        },
        content = {
            Surface(
                modifier = modifier
                    .background(color = MaterialTheme.colors.surface)
            ) {


                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier)
                    } else if (rewards.isEmpty()) {
                        Text(
                            text = "No Badge Earned Yet!",
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
                            items(rewards) {
                                RewardItem(
                                    reward = it,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }

            }
        }
    )

}