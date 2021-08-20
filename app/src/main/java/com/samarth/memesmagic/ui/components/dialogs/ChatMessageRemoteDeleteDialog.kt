package com.samarth.memesmagic.ui.components.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.samarth.memesmagic.ui.components.CustomButton
import com.samarth.memesmagic.ui.components.CustomSmallButton

@Composable
fun ChatMessageRemoteDeleteDialog(
    modifier: Modifier = Modifier,
    title: String,
    onDialogDismiss: () -> Unit,
    onLocalDelete: () -> Unit,
    onDeleteForEveryone: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDialogDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
        },
        buttons = {
              Column(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalAlignment = Alignment.End
              ) {
                  Spacer(modifier = Modifier.padding(16.dp))

                  CustomSmallButton(
                      text = "Cancel",
                      onclick = onDialogDismiss,
                      modifier = Modifier
                          .padding(8.dp),
                      textStyle = MaterialTheme.typography.body2
                  )
                  CustomSmallButton(
                      text = "Delete For Me",
                      onclick = {
                                onLocalDelete()
                                onDialogDismiss()
                      },
                      modifier = Modifier
                          .padding(8.dp),
                      textStyle = MaterialTheme.typography.body2
                  )
                  CustomSmallButton(
                      text = "Delete For EveryOne",
                      onclick = {
                          onDeleteForEveryone()
                          onDialogDismiss()
                      },
                      modifier = Modifier
                          .padding(8.dp),
                      textStyle = MaterialTheme.typography.body2
                  )

              }
        },
        modifier = modifier
    )
}



























