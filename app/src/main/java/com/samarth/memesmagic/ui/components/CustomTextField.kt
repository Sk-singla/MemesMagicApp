package com.samarth.memesmagic.ui.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun CustomTextField(
    value:String,
    onValueChange:(String)->Unit,
    modifier:Modifier = Modifier,
    textColor:Color = MaterialTheme.colors.onSurface,
    unFocusedBorderColor:Color = MaterialTheme.colors.secondaryVariant,
    focusedBorderColor:Color = MaterialTheme.colors.secondary,
    cursorColor:Color = MaterialTheme.colors.secondary,
    leadingIconColor:Color = MaterialTheme.colors.secondaryVariant,
    leadingIconDrawable:Int?=null,
    placeholder:String = "",
    focusedLabelColor:Color = MaterialTheme.colors.secondary
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        colors = TextFieldDefaults
            .outlinedTextFieldColors(
                textColor = textColor,
                unfocusedBorderColor = unFocusedBorderColor,
                focusedBorderColor = focusedBorderColor,
                cursorColor = cursorColor,
                leadingIconColor = leadingIconColor,
                focusedLabelColor = focusedLabelColor
            ),
        leadingIcon = {
            if(leadingIconDrawable != null)
            Icon(painter = painterResource(id = leadingIconDrawable), contentDescription = null)
        },
//        placeholder = {
//            Text(text = placeholder)
//        },
        label = {
            Text(text = placeholder)
        }
    )

}

















