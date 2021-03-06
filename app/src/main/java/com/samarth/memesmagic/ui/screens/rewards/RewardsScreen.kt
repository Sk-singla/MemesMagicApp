package com.samarth.memesmagic.ui.screens.rewards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.samarth.memesmagic.ui.components.CustomTopBar
import com.samarth.memesmagic.ui.components.RetryView
import com.samarth.memesmagic.ui.components.RewardItem


@Composable
fun RewardScreen(
    userEmail:String?,
    rewardsViewModel: RewardsViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        rewardsViewModel.getRewards(context,userEmail)
    }

    Scaffold(
        topBar = {
            CustomTopBar(title = "Rewards")
        }
    ) {
        when {
            rewardsViewModel.isLoading.value -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            rewardsViewModel.loadError.value.isNotEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                    RetryView(loadError = rewardsViewModel.loadError.value) {
                        rewardsViewModel.getRewards(context,userEmail)
                    }
                }

            }
            rewardsViewModel.rewards.value.isEmpty() -> {

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No Badge!",
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    items(rewardsViewModel.rewards.value){ reward ->
                        RewardItem(
                            reward = reward,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                }
            }
        }
    }
}