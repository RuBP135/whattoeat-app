package com.rubp.whattoeat.core.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * 前景内容决定默认尺寸，菜单只跟随测量结果，并在物理右侧展开。
 * 空 Popup 用于接收外部关闭事件。拖动直接更新偏移，松手按 35% 阈值切换状态；
 * 新拖动会取消正在执行的回弹。
 *
 * @param modifier 控制整个左滑容器的尺寸、边距等布局约束。
 * @param menuWidthDp 菜单的期望宽度，必须大于 0.dp；展开距离不超过容器实际宽度。
 * @param menuContent 背后的菜单内容，可通过 BoxScope 在菜单区域内对齐。
 * @param content 前景内容，接收承载水平平移和拖动手势的 Modifier。
 * 必须将该 Modifier 应用到前景根组件，并由前景自行提供背景与圆角。
 */
@Composable
fun WteLeftSwipeBox(
    modifier: Modifier = Modifier,
    menuWidthDp: Dp,
    menuContent: @Composable BoxScope.() -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    require(menuWidthDp > 0.dp)
    val requestedMenuWidthPx = with(LocalDensity.current) { menuWidthDp.toPx() }
    val containerWidthState = remember { mutableIntStateOf(0) }
    val menuWidthPx = if (containerWidthState.intValue > 0) {
        min(requestedMenuWidthPx, containerWidthState.intValue.toFloat())
    } else {
        requestedMenuWidthPx
    }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var isMenuOpen by remember { mutableStateOf(false) }
    var animationJob by remember { mutableStateOf<Job?>(null) }
    val scope = rememberCoroutineScope()

    fun settleMenu(open: Boolean) {
        isMenuOpen = open
        animationJob?.cancel()
        animationJob = scope.launch {
            animate(
                initialValue = offsetX,
                targetValue = if (open) -menuWidthPx else 0f,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
            ) { value, _ ->
                offsetX = value.coerceIn(-menuWidthPx, 0f)
            }
        }
    }

    LaunchedEffect(menuWidthPx) {
        animationJob?.cancel()
        offsetX = if (isMenuOpen) -menuWidthPx else 0f
    }

    Box(
        modifier = modifier
            .sizeIn(minWidth = menuWidthDp, minHeight = 48.dp)
            .onSizeChanged { containerWidthState.intValue = it.width },
        propagateMinConstraints = true
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .wrapContentWidth(AbsoluteAlignment.Right)
                .width(menuWidthDp),
            content = menuContent
        )
        content(
            Modifier
                .absoluteOffset { IntOffset(offsetX.roundToInt(), 0) }
                .pointerInput(menuWidthPx) {
                    detectHorizontalDragGestures(
                        onDragStart = { animationJob?.cancel() },
                        onDragEnd = {
                            val threshold = if (isMenuOpen) 0.65f else 0.35f
                            settleMenu(offsetX < -menuWidthPx * threshold)
                        },
                        onDragCancel = { settleMenu(isMenuOpen) },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            offsetX = (offsetX + dragAmount).coerceIn(-menuWidthPx, 0f)
                        }
                    )
                }
        )
        if (isMenuOpen) {
            Popup(onDismissRequest = { settleMenu(false) }) {}
        }
    }
}
