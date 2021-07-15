package com.samarth.memesmagic.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            Box(
                modifier = modifier.background(color = MaterialTheme.colors.surface),
                contentAlignment = Alignment.Center
            ){

                if(isLoading){
                    CircularProgressIndicator(modifier = Modifier)
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
    )

}