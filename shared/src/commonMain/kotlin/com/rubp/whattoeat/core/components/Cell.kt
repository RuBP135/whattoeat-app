package com.rubp.whattoeat.core.components

import androidx.compose.runtime.Composable

class Cell(
    val content: @Composable () -> Unit,
    val weight: Float
)
