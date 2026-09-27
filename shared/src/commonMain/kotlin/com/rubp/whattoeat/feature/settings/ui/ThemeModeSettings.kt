package com.rubp.whattoeat.feature.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.materialicons.MaterialIcons
import com.composables.icons.materialicons.filled.Brightness_auto
import com.composables.icons.materialicons.filled.Dark_mode
import com.composables.icons.materialicons.filled.Light_mode
import com.rubp.whattoeat.core.components.WtePaperCard
import com.rubp.whattoeat.core.theme.ThemeMode
import com.rubp.whattoeat.core.theme.WhatToEatPreviewTheme

private data class ThemeModeOption(
    val mode: ThemeMode,
    val title: String,
    val description: String,
    val icon: ImageVector
)

private val themeModeOptions = listOf(
    ThemeModeOption(
        mode = ThemeMode.System,
        title = "跟随系统",
        description = "根据设备外观自动切换",
        icon = MaterialIcons.Filled.Brightness_auto
    ),
    ThemeModeOption(
        mode = ThemeMode.Light,
        title = "浅色模式",
        description = "始终使用浅色外观",
        icon = MaterialIcons.Filled.Light_mode
    ),
    ThemeModeOption(
        mode = ThemeMode.Dark,
        title = "深色模式",
        description = "始终使用深色外观",
        icon = MaterialIcons.Filled.Dark_mode
    )
)

/**
 * 显示模式设置卡。
 *
 * @param themeMode 当前选中的显示模式。
 * @param onThemeModeChange 用户选择新显示模式时的回调。
 * @param modifier 设置卡的布局修饰符。
 */
@Composable
internal fun ThemeModeSettings(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    modifier: Modifier
) {

    WtePaperCard(modifier = modifier){
        Text(
            text = "外观设置",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "选择 What To Eat 的显示模式",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            themeModeOptions.forEach { option ->
                ThemeModeOptionRow(
                    option = option,
                    selected = option.mode == themeMode,
                    onClick = { onThemeModeChange(option.mode) }
                )
            }
        }
    }
}

/**
 * 单个显示模式选项。
 *
 * @param option 当前选项的模式、标题、说明和图标。
 * @param selected 是否为当前选中的模式。
 * @param onClick 用户点击整行时的回调。
 */
@Composable
private fun ThemeModeOptionRow(
    option: ThemeModeOption,
    selected: Boolean,
    onClick: () -> Unit
) {
    val containerColor = if (selected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        Color.Transparent
    }
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Surface(
        color = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = selected,
                    role = Role.RadioButton,
                    onClick = onClick
                )
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = option.icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = option.title,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = option.description,
                    color = LocalContentColor.current.copy(alpha = 0.72f),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            RadioButton(
                selected = selected,
                onClick = null
            )
        }
    }
}

@Preview
@Composable
private fun ThemeModeSettingsPreview(){
    WhatToEatPreviewTheme {
        ThemeModeSettings(
            themeMode = ThemeMode.Light,
            onThemeModeChange = {},
            modifier = Modifier
        )
    }
}
