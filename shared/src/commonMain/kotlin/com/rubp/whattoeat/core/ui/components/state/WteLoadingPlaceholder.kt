package com.rubp.whattoeat.core.ui.components.state

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.rubp.whattoeat.core.theme.WhatToEatPreviewTheme
import com.rubp.whattoeat.core.theme.WteTheme
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun WteLoadingPlaceholder(
    modifier: Modifier = Modifier
) {

    var size by remember { mutableStateOf(IntSize.Zero) }
    val (width, height) = size
    val density = LocalDensity.current

    val lineColor = WteTheme.extendedColors.placeholderSwapLine


    val halfSpan = with(density) {
        Offset(10.dp.toPx(), 10.dp.toPx())
    }

    val (start, end) = with(density) {
        val pos = max(width, height) - 0.5f * abs(width - height)
        -halfSpan to Offset(pos, pos) + halfSpan
    }

    val durationMillis = with(density) {
        val speed = 100.dp.toPx()
        val duration = (end - start).getDistance() / speed
        (duration * 1000).roundToInt()
    }

    val process = key(durationMillis) {
        val transition = rememberInfiniteTransition()

        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = durationMillis,
                    easing = LinearEasing
                )
            )
        )
    }


    Spacer(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(WteTheme.extendedColors.placeholderBackground)
            .onSizeChanged{ size = it }
            .drawBehind {
                val center = start + (end - start) * process.value
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            lineColor,
                            Color.Transparent
                        ),
                        start = center - halfSpan,
                        end = center + halfSpan
                    )
                )
            }
    )
}

@Preview
@Composable
private fun WteLoadingPlaceholderLightPreview() {
    WhatToEatPreviewTheme {
        WteLoadingPlaceholder(
            modifier = Modifier
                .width(64.dp)
                .height(80.dp)
        )
    }
}

@Preview
@Composable
private fun WteLoadingPlaceholderDarkPreview() {
    WhatToEatPreviewTheme(darkTheme = true){
        WteLoadingPlaceholder(
            modifier = Modifier
                .width(64.dp)
                .height(80.dp)
        )
    }
}