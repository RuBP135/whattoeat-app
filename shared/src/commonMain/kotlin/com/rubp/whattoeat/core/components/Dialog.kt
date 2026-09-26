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

@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    confirmText: String = "确定",
    dismissText: String = "取消",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
){
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirm){
                Text(text = confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { 
                Text(text = dismissText, color = MaterialTheme.colorScheme.onSurface)
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
    confirmText: String = "保存",
    dismissText: String = "取消",
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
){
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
                Text(text = confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = dismissText, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    )
}