package com.rubp.whattoeat.app.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.materialicons.MaterialIcons
import com.composables.icons.materialicons.filled.Home
import com.composables.icons.materialicons.filled.Settings
import com.composables.icons.materialicons.outlined.Home
import com.composables.icons.materialicons.outlined.Settings
import com.rubp.whattoeat.core.theme.WhatToEatPreviewTheme
import com.rubp.whattoeat.core.theme.WteTheme

private data class MainBottomBarItem(
    val label: String,
    val selected: Boolean,
    val filledVector: ImageVector,
    val outlinedVector: ImageVector,
    val destination: MainDestination
)

/**
 * 导航栏和占位空白的统一外层容器
 *
 * @Param content 放置导航栏或占位空白
 */
@Composable
fun MainBottomBarLayout(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(NavigationBarDefaults.windowInsets) // 避开系统底部栏
            .padding(horizontal = 16.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 560.dp)
                .fillMaxWidth()
                .height(72.dp),
            content = content
        )
    }
}

/**
 * 包含主页面底部导航栏的组件
 *
 * @param mainDestination 当前顶级页面；为 null 时隐藏导航栏。
 * @param onDestinationSelected 用户选择导航项时的回调。
 */
@Composable
fun MainBottomBar(
    mainDestination: MainDestination?,
    modifier: Modifier = Modifier,
    onDestinationSelected: (MainDestination) -> Unit
) {

    val items = listOf(
        MainBottomBarItem(
            label = "首页",
            selected = mainDestination == MainDestination.Home,
            filledVector = MaterialIcons.Filled.Home,
            outlinedVector = MaterialIcons.Outlined.Home,
            destination = MainDestination.Home
        ),
        MainBottomBarItem(
            label = "设置",
            selected = mainDestination == MainDestination.Settings,
            filledVector = MaterialIcons.Filled.Settings,
            outlinedVector = MaterialIcons.Outlined.Settings,
            destination = MainDestination.Settings
        )
    )


    AnimatedVisibility(
        modifier = modifier,
        visible = mainDestination != null,
        enter = slideInVertically{ it },
        exit = slideOutVertically{ it }
    ){
        val barShape = RoundedCornerShape(20.dp)

        MainBottomBarLayout {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(start = 4.dp, top = 4.dp)
                    .background(
                        color = WteTheme.extendedColors.offsetShadow,
                        shape = barShape
                    )
            )
            Surface(
                modifier = Modifier
                    .matchParentSize()
                    .padding(end = 4.dp, bottom = 4.dp),
                color = WteTheme.extendedColors.paper,
                contentColor = MaterialTheme.colorScheme.onSurface,
                shape = barShape,
                border = BorderStroke(
                    width = 1.dp,
                    color = WteTheme.extendedColors.paperBorder
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .selectableGroup(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items.forEach { item ->
                        NavigationBarItem(
                            selected = item.selected,
                            onClick = { onDestinationSelected(item.destination) },
                            icon = {
                                Icon(
                                    imageVector = if (item.selected) {
                                        item.filledVector
                                    } else {
                                        item.outlinedVector
                                    },
                                    contentDescription = null
                                )
                            },
                            label = {
                                Text(
                                    text = item.label,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            },
                            alwaysShowLabel = true,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = Color.Transparent,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MainBottomBarSpacer(){
    MainBottomBarLayout{
        Spacer(modifier = Modifier.matchParentSize())
    }
}

@Preview
@Composable
private fun MainBottomBarLightPreview() {
    WhatToEatPreviewTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            MainBottomBar(
                MainDestination.Home,
                onDestinationSelected = {}
            )
        }
    }
}

@Preview
@Composable
private fun MainBottomBarDarkPreview() {
    WhatToEatPreviewTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            MainBottomBar(
                MainDestination.Home,
                onDestinationSelected = {}
            )
        }
    }
}

