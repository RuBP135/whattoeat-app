package com.rubp.whattoeat.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composables.icons.materialicons.MaterialIcons
import com.composables.icons.materialicons.filled.Chevron_left
import com.composables.icons.materialicons.filled.More_vert
import com.rubp.whattoeat.core.theme.WhatToEatPreviewTheme
import com.rubp.whattoeat.core.theme.WteTheme

/**
 * 可以添加标题或者增加额外菜单的顶部栏
 *
 * @param onClickReturn 点击返回图标时触发的回调
 * @param title 页面文字标题
 * @param menu 额外的下拉菜单，将在图标下方展开
 */
@Composable
fun WteTopBar(
    onClickReturn: () -> Unit,
    title: String? = null,
    menu: (@Composable ColumnScope.(closeMenu: () -> Unit) -> Unit)? = null
){
    Surface(
        modifier = Modifier.fillMaxWidth(),
    ){
        Column(
            modifier = Modifier.fillMaxWidth()
        ){
            Row(
                modifier = Modifier
                    .windowInsetsPadding(TopAppBarDefaults.windowInsets)
                    .fillMaxWidth()
                    .heightIn(min = 56.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(onClickReturn){
                    Icon(
                        imageVector = MaterialIcons.Filled.Chevron_left,
                        contentDescription = "返回",
                    )
                }

                title?.let{
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleLarge
                    )
                }


                if(menu == null) {
                    IconButton( // 仅作为占位
                        onClick = {},
                        enabled = false
                    ) {}
                } else {
                    var isShowMenu by remember { mutableStateOf(false) }
                    Box{
                        IconButton({ isShowMenu = true }){
                            Icon(
                                imageVector = MaterialIcons.Filled.More_vert,
                                contentDescription = "更多",
                            )
                        }
                        // 锚定父容器Box
                        DropdownMenu(
                            expanded = isShowMenu,
                            onDismissRequest = { isShowMenu = false }, // 点击菜单外的任何地方，消耗点击事件
                            content = {
                                menu { isShowMenu = false }
                            }
                        )
                    }
                }
            }

            HorizontalDivider(
                thickness = Dp.Hairline,
                color = WteTheme.extendedColors.paperBorder
            )
        }

    }


}

@Preview
@Composable
private fun TopBarPreview(){
    WhatToEatPreviewTheme {
        WteTopBar(
            onClickReturn = {},
            title = "title",
            menu = {}
        )
    }
}
