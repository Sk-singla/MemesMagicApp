package com.samarth.memesmagic.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import com.google.accompanist.coil.rememberCoilPainter
import com.samarth.memesmagic.R
import com.samarth.memesmagic.ui.theme.Green500

@Composable
fun ProfileImage(
    name: String,
    imageUrl: String? = null,
    modifier: Modifier = Modifier,
    fontStyle: TextStyle = MaterialTheme.typography.h6
) {

    if(imageUrl == null || imageUrl.isEmpty()) {
        var imageStr = ""
        name.split(' ').forEach {
            imageStr += it[0]
        }
        imageStr.capitalize(Locale.current)
        Box(
            modifier = modifier
                .background(color = Green500, shape = CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.8f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = imageStr,
                style = fontStyle,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                maxLines = 1,
            )
        }

    } else {
        Image(
            painter = rememberCoilPainter(
                request = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .build(),
                fadeIn = true,
            ),
            contentScale = ContentScale.Crop,
            contentDescription = "User Image",
            modifier = modifier
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.8f),
                    shape = CircleShape
                )
        )

    }

}