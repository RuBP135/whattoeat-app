package com.rubp.whattoeat.core.ui.components.dialog

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rubp.whattoeat.core.theme.WhatToEatPreviewTheme
import com.rubp.whattoeat.core.theme.WteTheme

/**
 * 通用确认弹窗，由调用方控制显示、关闭和业务操作。
 *
 * @param title 弹窗标题。
 * @param message 提示内容。
 * @param modifier 弹窗面板的布局与尺寸修饰。
 * @param confirmText 确定按钮文字。
 * @param dismissText 取消按钮文字。
 * @param isDestructive 是否使用危险操作的确认按钮颜色。
 * @param onConfirm 确定按钮回调，不会自动关闭弹窗。
 * @param onDismiss 取消按钮、点击外部或返回触发的关闭请求。
 */
@Composable
fun WteConfirmDialog(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    confirmText: String = "确定",
    dismissText: String = "取消",
    isDestructive: Boolean = false,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(20.dp)

    AlertDialog(
        modifier = modifier.border(1.dp, WteTheme.extendedColors.paperBorder, shape),
        onDismissRequest = onDismiss,
        shape = shape,
        containerColor = WteTheme.extendedColors.paper,
        titleContentColor = colorScheme.onSurface,
        textContentColor = colorScheme.onSurfaceVariant,
        tonalElevation = 0.dp,
        title = {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Text(text = message, style = MaterialTheme.typography.bodyMedium)
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDestructive) colorScheme.error else colorScheme.primary,
                    contentColor = if (isDestructive) colorScheme.onError else colorScheme.onPrimary
                )
            ) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = colorScheme.onSurfaceVariant)
            ) {
                Text(text = dismissText)
            }
        }
    )
}

@Preview
@Composable
private fun WteConfirmDialogPreview() {
    WhatToEatPreviewTheme {
        WteConfirmDialog(
            title = "确认操作",
            message = "确定继续此操作吗？",
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview
@Composable
private fun WteConfirmDialogDarkPreview() {
    WhatToEatPreviewTheme(darkTheme = true) {
        WteConfirmDialog(
            title = "确认操作",
            message = "确定继续此操作吗？",
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview
@Composable
private fun WteConfirmDialogDestructivePreview() {
    WhatToEatPreviewTheme {
        WteConfirmDialog(
            title = "删除确认",
            message = "确定删除所选内容吗？",
            isDestructive = true,
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview
@Composable
private fun WteConfirmDialogDestructiveDarkPreview() {
    WhatToEatPreviewTheme(darkTheme = true) {
        WteConfirmDialog(
            title = "删除确认",
            message = "确定删除所选内容吗？",
            isDestructive = true,
            onConfirm = {},
            onDismiss = {}
        )
    }
}
