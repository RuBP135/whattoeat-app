package com.rubp.whattoeat.core.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
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

/**
 * 可左滑展开菜单的容器，默认由前景内容决定尺寸。
 *
 * @param menuWidthDp 菜单区域宽度，同时作为左滑展开的距离。
 * @param modifier 调用方设置的尺寸、边距等布局约束。
 * @param menuContent 背后菜单的内容，在菜单区域内通过 BoxScope 对齐。
 * @param content 前景内容，接收外部最小尺寸约束，并自行提供背景和形状。
 */
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
        modifier = modifier,
        propagateMinConstraints = true // 将调用方的最小尺寸约束传递给前景。
    ) {
        // 背后的menu
        Box(
            modifier = Modifier
                .matchParentSize() // 跟随前景确定的尺寸，不参与撑大外层。
                .wrapContentWidth(align = Alignment.End) // 只放松菜单的宽度约束，并靠右放置。
                .width(menuWidthDp),
            content = menuContent
        )

        // 前面的主要内容
        Box(
            modifier = Modifier
                .heightIn(min = 48.dp) // 保留最小操作高度，仍遵守调用方的尺寸约束。
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
                },
            propagateMinConstraints = true // 让实际内容获得与前景容器一致的最小尺寸。
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