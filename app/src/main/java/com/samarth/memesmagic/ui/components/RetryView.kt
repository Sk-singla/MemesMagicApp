package com.samarth.memesmagic.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun RetryView(
    loadError:String,
    onRetry:()->Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = loadError,style = MaterialTheme.typography.h5,textAlign = TextAlign.Center)

        CustomButton(
            text = "Retry",
            onclick = {
                onRetry()
            },
            modifier = Modifier.padding(horizontal = 24.dp,vertical = 8.dp)
        )

    }

}