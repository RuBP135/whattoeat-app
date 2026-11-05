package com.rubp.whattoeat.core.ui.components.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rubp.whattoeat.core.theme.WhatToEatPreviewTheme
import com.rubp.whattoeat.core.ui.components.button.WteSecondaryButton

@Composable
fun WteErrorPanel(
    message: String,
    modifier: Modifier = Modifier,
    retryText: String = "重试",
    retry: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = Color.Gray,
            style = MaterialTheme.typography.bodyLarge
        )
        WteSecondaryButton(
            text = retryText,
            onClick = retry
        )
    }
}


@Preview
@Composable
private fun WteErrorPanelLightPreview() {
    WhatToEatPreviewTheme {
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(280.dp),
            contentAlignment = Alignment.Center
        ) {
            WteErrorPanel(
                message = "遇到了一点错误..."
            ) { }
        }
    }
}

@Preview
@Composable
private fun WteErrorPanelDarkPreview() {
    WhatToEatPreviewTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(280.dp),
            contentAlignment = Alignment.Center
        ) {
            WteErrorPanel(
                message = "遇到了一点错误..."
            ) { }
        }
    }
}