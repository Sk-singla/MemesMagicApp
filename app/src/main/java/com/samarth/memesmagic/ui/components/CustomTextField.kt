package com.samarth.memesmagic.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation

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
    trailingIconDrawable:Int?=null,
    onTrailingIconClick:(()->Unit)?=null,
    placeholder:String = "",
    visualTransformation: VisualTransformation =  VisualTransformation.None,
    focusedLabelColor:Color = MaterialTheme.colors.secondary,
    keyboardOptions:KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
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
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        label = {
            Text(text = placeholder)
        },
        trailingIcon = {
            if(trailingIconDrawable != null)
                Icon(
                    painter = painterResource(id = trailingIconDrawable),
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        onTrailingIconClick?.invoke()
                    }
                )
        },
        visualTransformation = visualTransformation
    )

}

















