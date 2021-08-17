package com.samarth.memesmagic.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.samarth.memesmagic.util.Constants.PARTIALLY_CLICKABLE_TEXT_TAG

@Composable
fun PartiallyClickableText(
    modifier:Modifier = Modifier,
    nonClickableText:String,
    clickableText:String,
    nonClickableTextColor:Color = MaterialTheme.colors.secondaryVariant,
    clickableTextColor:Color = MaterialTheme.colors.secondaryVariant,
    onClick:()->Unit
) {


    val annotatedText = remember {
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = nonClickableTextColor)) {
                append(nonClickableText)
            }

            pushStringAnnotation(PARTIALLY_CLICKABLE_TEXT_TAG,PARTIALLY_CLICKABLE_TEXT_TAG)
            withStyle(style = SpanStyle(color = clickableTextColor,fontWeight = FontWeight.Bold)) {
                append(clickableText)
            }
            pop()
        }
    }

    Box(modifier = modifier,contentAlignment = Alignment.Center) {
        ClickableText(
            text = annotatedText,
            onClick = { offset ->

                if (offset > nonClickableText.length && offset < (nonClickableText.length + clickableText.length)) {
                    annotatedText.getStringAnnotations(
                        tag = PARTIALLY_CLICKABLE_TEXT_TAG,
                        start = offset,
                        end = offset
                    )[0].let { annotation ->
                        onClick()
                    }
                }
            },
            style = MaterialTheme.typography.body1,
        )
    }


}