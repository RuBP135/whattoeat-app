package com.rubp.whattoeat.core.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import whattoeat.shared.generated.resources.Res
import whattoeat.shared.generated.resources.common_cancel
import whattoeat.shared.generated.resources.common_confirm
import whattoeat.shared.generated.resources.common_save

@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    confirmText: String? = null,
    dismissText: String? = null,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
){

    val confirmLabel = confirmText ?: stringResource(Res.string.common_confirm)
    val dismissLabel = dismissText ?: stringResource(Res.string.common_cancel)

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirm){
                Text(text = confirmLabel)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = dismissLabel, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    )
}

@Composable
fun EditDialog(
    title: String,
    modifier: Modifier = Modifier,
    initialText: String = "",
    labelText: String? = null,
    confirmText: String? = null,
    dismissText: String? = null,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
){
    val confirmLabel = confirmText ?: stringResource(Res.string.common_save)
    val dismissLabel = dismissText ?: stringResource(Res.string.common_cancel)

    var localText by remember { mutableStateOf(initialText) }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = localText,
                onValueChange = { localText = it },
                singleLine = true,
                label = labelText?.let { { Text(it) } }
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(localText) }) {
                Text(text = confirmLabel)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = dismissLabel, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    )
}