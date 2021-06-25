package com.samarth.memesmagic.ui.Screens.home.create

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CreateScreen() {

    Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center){
        Text(text = "Create")
    }
}