package com.samarth.memesmagic.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CustomTopBar(
    title:String,
    modifier:Modifier = Modifier,
    titleColor:Color = MaterialTheme.colors.onSecondary,
    navigationIcon:@Composable (()->Unit)?=null,
    actions: @Composable RowScope.()->Unit = {}
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
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp),
        navigationIcon = navigationIcon,
        actions = actions
    )
}


@Composable
fun CustomTopBar(
    title:AnnotatedString,
    modifier:Modifier = Modifier,
) {

    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp),
    )
}