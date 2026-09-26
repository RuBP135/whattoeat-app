package com.rubp.whattoeat.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.composables.icons.materialicons.MaterialIcons
import com.composables.icons.materialicons.filled.Chevron_left
import com.composables.icons.materialicons.filled.More_vert


@Composable
fun AppTopBar(
    onClickReturn: (() -> Unit),
    title: String? = null,
    menu: (@Composable ColumnScope.(closeMenu: () -> Unit) -> Unit)? = null
){
    val componentColor = MaterialTheme.colorScheme.onPrimary
    Row(
        modifier = Modifier.fillMaxWidth().background(color = MaterialTheme.colorScheme.primary),
        horizontalArrangement = Arrangement.SpaceBetween, // 左边的放最左，右边的放最右，其余均分排布
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(onClickReturn){
            Icon(
                imageVector = MaterialIcons.Filled.Chevron_left,
                contentDescription = "返回",
                tint = componentColor
            )
        }

        title?.let{
            Text(
                text = it,
                color = componentColor,
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
                        tint = componentColor
                    )
                }
                // 锚点父容器Box，而父容器Box完全取决于IconButton“更多”，这就实现了跟在“更多”下方
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
}

@Preview
@Composable
private fun TopBarPreview(){
    AppTopBar({}, "title", {})
}