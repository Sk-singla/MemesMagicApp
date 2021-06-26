package com.samarth.memesmagic.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = Green500,
    primaryVariant = Green700,
    secondary = Green500,
    secondaryVariant = Green700,
    background = Gray700,
    surface = Gray700,
    error= Red,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black,
    onPrimary = Color.White,
    onSecondary = Color.White,
)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = Color.White,
    primaryVariant = Color.White,
    secondary = Green500,
    secondaryVariant = Green700,
    background = Color.White,
    surface = Color.White,
    error= DarkRed,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.Black,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
)




@Composable
fun MemesMagicTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if(darkTheme) DarkColorPalette else LightColorPalette
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}