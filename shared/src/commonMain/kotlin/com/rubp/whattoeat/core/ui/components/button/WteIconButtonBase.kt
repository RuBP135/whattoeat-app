package com.rubp.whattoeat.core.ui.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.rubp.whattoeat.core.theme.WteTheme

@Composable
internal fun WteIconButtonBase(
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    backgroundColor: Color,
    contentColor: Color,
    shape: Shape,
    content: @Composable () -> Unit
) {
    val borderColor = WteTheme.extendedColors.paperBorder

    OutlinedIconButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minWidth = 48.dp, minHeight = 48.dp),
        enabled = enabled,
        shape = shape,
        colors = IconButtonDefaults.outlinedIconButtonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = backgroundColor,
            disabledContentColor = contentColor.copy(alpha = contentColor.alpha * 0.38f)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (enabled) borderColor else borderColor.copy(alpha = borderColor.alpha * 0.38f)
        ),
        content = content
    )
}
