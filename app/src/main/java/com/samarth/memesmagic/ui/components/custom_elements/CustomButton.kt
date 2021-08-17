package com.samarth.memesmagic.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    text:String,
    modifier:Modifier = Modifier,
    textColor:Color = MaterialTheme.colors.secondaryVariant,
    textStyle: TextStyle = MaterialTheme.typography.h6,
    backgroundColor:Color = MaterialTheme.colors.surface,
    enabled: Boolean = true,
    @DrawableRes icon:Int? = null,
    onclick:()->Unit
){
    Button(
        onClick = onclick,
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(10.dp)
            ),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        shape = RoundedCornerShape(10.dp)
    ) {

        if(icon!=null) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)

            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                )
                Text(
                    text = text,
                    modifier = Modifier.fillMaxWidth(),
                    color = textColor,
                    style = textStyle,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        } else {
            Text(
                text = text,
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                color = textColor,
                style = textStyle,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
@Composable
fun CustomSmallButton(
    text:String,
    modifier:Modifier = Modifier,
    textColor:Color = MaterialTheme.colors.secondaryVariant,
    textStyle: TextStyle = MaterialTheme.typography.h6,
    backgroundColor:Color = MaterialTheme.colors.surface,
    enabled: Boolean = true,
    onclick:()->Unit
){
    Button(
        onClick = onclick,
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(5.dp)
            ),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        shape = RoundedCornerShape(5.dp)
    ) {

        Text(
            text = text,
            modifier = Modifier.padding(4.dp),
            color = textColor,
            style = textStyle,
            textAlign = TextAlign.Center,
            maxLines = 1
        )

    }
}