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
fun ChatMessageLocalDeleteDialog(
    modifier: Modifier = Modifier,
    title: String,
    onDialogDismiss: () -> Unit,
    onDelete: () -> Unit
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

                  Row(
                      horizontalArrangement = Arrangement.End,
                      verticalAlignment = Alignment.CenterVertically,
                      modifier = Modifier.fillMaxWidth(0.7f)
                  ){
                      CustomSmallButton(
                          text = "Cancel",
                          onclick = onDialogDismiss,
                          modifier = Modifier
                              .padding(8.dp),
                          textStyle = MaterialTheme.typography.body2
                      )
                      CustomSmallButton(
                          text = "Delete",
                          onclick = {
                                    onDelete()
                                    onDialogDismiss()
                          },
                          modifier = Modifier
                              .padding(8.dp),
                          textStyle = MaterialTheme.typography.body2
                      )
                  }
              }
        },
        modifier = modifier
    )
}



























