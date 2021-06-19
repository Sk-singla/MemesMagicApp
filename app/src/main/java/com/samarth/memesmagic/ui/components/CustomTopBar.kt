package com.samarth.memesmagic.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CustomTopBar(
    title:String,
    modifier:Modifier = Modifier,
    titleColor:Color = MaterialTheme.colors.onSecondary,
) {

    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                color = titleColor
            )
        },
        modifier = modifier.fillMaxWidth().shadow(4.dp),
    )

}