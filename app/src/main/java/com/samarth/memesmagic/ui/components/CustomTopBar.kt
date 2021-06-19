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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.samarth.memesmagic.ui.theme.Gray200
import com.samarth.memesmagic.ui.theme.Gray500

@Composable
fun CustomTopBar(
    title:String,
    modifier:Modifier = Modifier
) {

    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primaryVariant
            )
        },
        modifier = modifier.fillMaxWidth().shadow(8.dp),
        backgroundColor = Gray500
    )

}