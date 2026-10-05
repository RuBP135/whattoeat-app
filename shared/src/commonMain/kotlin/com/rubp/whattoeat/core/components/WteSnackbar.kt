package com.rubp.whattoeat.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.materialicons.MaterialIcons
import com.composables.icons.materialicons.filled.Close
import com.rubp.whattoeat.core.theme.WhatToEatPreviewTheme
import com.rubp.whattoeat.core.theme.WteTheme

@Composable
fun WteSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
){
    val visuals = snackbarData.visuals

    Surface(
        modifier = modifier
            .padding(12.dp)
            .widthIn(max = 600.dp),
        shape = RoundedCornerShape(16.dp),
        color = WteTheme.extendedColors.paper,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shadowElevation = 4.dp
    ){
        Row(
            modifier = Modifier
                .heightIn(min = 48.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(
                text = visuals.message,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .weight(1f, fill = false),
                style = MaterialTheme.typography.bodyMedium
            )

            visuals.actionLabel?.let { label ->
                TextButton(
                    onClick = snackbarData::performAction,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
                ){
                    Text(text = label)
                }
            }

            if (visuals.withDismissAction) {
                IconButton(
                    onClick = snackbarData::dismiss
                ) {
                    Icon(
                        imageVector = MaterialIcons.Filled.Close,
                        contentDescription = "关闭提示"
                    )
                }
            }
        }

    }
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