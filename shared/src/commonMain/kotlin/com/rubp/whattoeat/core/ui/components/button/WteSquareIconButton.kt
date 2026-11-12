package com.rubp.whattoeat.core.ui.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.materialicons.MaterialIcons
import com.composables.icons.materialicons.filled.Edit
import com.rubp.whattoeat.core.theme.WhatToEatPreviewTheme

/**
 * 带细描边的方形圆角图标按钮，默认最小尺寸为 48.dp
 */
@Composable
fun WteSquareIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    content: @Composable () -> Unit
) {
    WteIconButtonBase(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(12.dp),
        content = content
    )
}

@Preview(name = "Square icon button - Light")
@Composable
private fun WteSquareIconButtonLightPreview() {
    WteSquareIconButtonPreview(darkTheme = false)
}

@Preview(name = "Square icon button - Dark")
@Composable
private fun WteSquareIconButtonDarkPreview() {
    WteSquareIconButtonPreview(darkTheme = true)
}

@Composable
private fun WteSquareIconButtonPreview(darkTheme: Boolean) {
    WhatToEatPreviewTheme(darkTheme = darkTheme) {
        val icon: @Composable () -> Unit = {
            Icon(
                imageVector = MaterialIcons.Filled.Edit,
                contentDescription = "编辑"
            )
        }
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WteSquareIconButton(onClick = {}, content = icon)
            WteSquareIconButton(onClick = {}, enabled = false, content = icon)
            WteSquareIconButton(onClick = {}, modifier = Modifier.size(64.dp), content = icon)
        }
    }
}
