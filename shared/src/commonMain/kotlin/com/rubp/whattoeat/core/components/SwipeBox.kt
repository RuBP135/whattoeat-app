package com.rubp.whattoeat.core.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun LeftSwipeBox(
    menuWidthDp: Dp,
    modifier: Modifier = Modifier,
    menuContent: @Composable BoxScope.() -> Unit,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val menuWidthPx = with(density) { menuWidthDp.toPx() }

    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    // 是否已经完全滑开（展开menu）
    var isMenuOpen by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        // 背后的menu
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(menuWidthDp)
                .fillMaxHeight(),
            content = menuContent
        )

        // 前面的主要内容
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .offset { IntOffset(offsetX.value.roundToInt(), 0) } // 实时平移
                .pointerInput(isMenuOpen) {
                    detectHorizontalDragGestures( // 单项拖拽
                        onDragStart = { }, // 开始拖拽
                        onDragEnd = { // 拖拽结束
                            // 手指抬起：纯数学计算停靠点，无惧任何老旧或新版 Compose API 的不统一
                            scope.launch {
                                if (!isMenuOpen) {
                                    // 处于“关闭”状态时：向左滑过 35% 则吸附展开，否则弹回
                                    if (offsetX.value < -menuWidthPx * 0.35f) {
                                        isMenuOpen = true // 需要先标记，否则还在滑动动画过程中isMenuOpen还是false，无法被中途点击外部自动关闭
                                        offsetX.animateTo(-menuWidthPx, spring(stiffness = Spring.StiffnessMediumLow))
                                    } else {
                                        offsetX.animateTo(0f, spring(stiffness = Spring.StiffnessMediumLow))
                                    }
                                } else {
                                    // 处于“展开”状态时：向右滑过 35% 则收起，否则继续保持展开
                                    if (offsetX.value > -menuWidthPx * 0.65f) {
                                        isMenuOpen = false
                                        offsetX.animateTo(0f, spring(stiffness = Spring.StiffnessMediumLow))
                                    } else {
                                        offsetX.animateTo(-menuWidthPx, spring(stiffness = Spring.StiffnessMediumLow))
                                    }
                                }
                            }
                        },
                        onDragCancel = {
                            // 异常中断（如弹窗打断）能恢复到当前正确的状态
                            scope.launch {
                                offsetX.animateTo(if (isMenuOpen) -menuWidthPx else 0f)
                            }
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            // 父组件不再处理上下滚动
                            change.consume()

                            scope.launch {
                                // 限制边界在 [-menuWidthPx, 0f]，绝对不会飞出屏幕或滑到右边去
                                val newValue = (offsetX.value + dragAmount).coerceIn(-menuWidthPx, 0f)
                                offsetX.snapTo(newValue)
                            }
                        }
                    )
                }

        ) {
            content()
        }

        if(isMenuOpen){ // 收回menu
            Popup(
                onDismissRequest = {
                    scope.launch {
                        isMenuOpen = false
                        offsetX.animateTo(0f, spring(stiffness = Spring.StiffnessMediumLow))
                    }
                }
            ){}
        }
    }
}