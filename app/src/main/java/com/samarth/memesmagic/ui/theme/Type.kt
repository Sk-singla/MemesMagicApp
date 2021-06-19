package com.samarth.memesmagic.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.samarth.memesmagic.R

// Set of Material typography styles to start with

val Roboto = FontFamily(
    Font(R.font.roboto_light, FontWeight.Light),
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_bold, FontWeight.Bold),
)

val RobotoCondensed = FontFamily(
    Font(R.font.roboto_condensed_light, FontWeight.Light),
    Font(R.font.roboto_condensed_regular, FontWeight.Normal),
)


private val defaultTypography = Typography()
val Typography = Typography(
    h1 = defaultTypography.h1.copy(fontFamily = Roboto),
    h2 = defaultTypography.h2.copy(fontFamily = Roboto),
    h3 = defaultTypography.h3.copy(fontFamily = Roboto),
    h4 = defaultTypography.h4.copy(fontFamily = Roboto),
    h5 = defaultTypography.h5.copy(fontFamily = Roboto),
    h6 = defaultTypography.h6.copy(fontFamily = Roboto),
    subtitle1 = defaultTypography.subtitle1.copy(fontFamily = Roboto),
    subtitle2 = defaultTypography.subtitle2.copy(fontFamily = Roboto),
    body1 = defaultTypography.body1.copy(fontFamily = Roboto),
    body2 = defaultTypography.body2.copy(fontFamily = Roboto),
    button = defaultTypography.button.copy(fontFamily = Roboto),
    caption = defaultTypography.caption.copy(fontFamily = Roboto),
    overline = defaultTypography.overline.copy(fontFamily = Roboto)
)
