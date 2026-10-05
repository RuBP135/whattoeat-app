package com.rubp.whattoeat.core.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rubp.whattoeat.core.theme.WhatToEatPreviewTheme
import com.rubp.whattoeat.core.theme.WteTheme

@Composable
fun WteSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
){
    Snackbar(
        snackbarData = snackbarData,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        containerColor = WteTheme.extendedColors.paper,
        contentColor = MaterialTheme.colorScheme.onSurface,
        actionColor = MaterialTheme.colorScheme.secondary,
        actionContentColor = MaterialTheme.colorScheme.secondary,
        dismissActionContentColor = MaterialTheme.colorScheme.onSurface
    )
}

@Preview
@Composable
private fun WteSnackbarMessageOnlyPreview(){

    val snackbarData = object : SnackbarData {
        override val visuals: SnackbarVisuals = object : SnackbarVisuals {
            override val message: String = "这是一条提示消息"
            override val actionLabel: String? = null
            override val withDismissAction: Boolean = false
            override val duration: SnackbarDuration = SnackbarDuration.Short
        }

        override fun performAction() {}

        override fun dismiss() {}
    }

    WhatToEatPreviewTheme {
        WteSnackbar(snackbarData)
    }
}

@Preview
@Composable
private fun WteSnackbarWithActionAndDismissPreview(){

    val snackbarData = object : SnackbarData {
        override val visuals: SnackbarVisuals = object : SnackbarVisuals {
            override val message: String = "这是一条提示消息"
            override val actionLabel: String = "撤销操作？"
            override val withDismissAction: Boolean = true
            override val duration: SnackbarDuration = SnackbarDuration.Short
        }

        override fun performAction() {}

        override fun dismiss() {}
    }

    WhatToEatPreviewTheme {
        WteSnackbar(snackbarData)
    }
}