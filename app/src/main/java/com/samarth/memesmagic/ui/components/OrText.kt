package com.samarth.memesmagic.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun OrText(
    modifier: Modifier = Modifier
) {
    Text(
        text = "————————— OR —————————",
        style = MaterialTheme.typography.body2,
        modifier = modifier
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        maxLines = 1,
    )
}