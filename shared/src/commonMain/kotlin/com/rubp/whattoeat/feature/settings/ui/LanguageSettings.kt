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
import com.composables.icons.materialicons.filled.Language
import com.rubp.whattoeat.core.components.card.WtePaperCard
import com.rubp.whattoeat.core.i18n.AppLanguage
import com.rubp.whattoeat.core.theme.WhatToEatPreviewTheme
import org.jetbrains.compose.resources.stringResource
import whattoeat.shared.generated.resources.Res
import whattoeat.shared.generated.resources.settings_language_chinese_description
import whattoeat.shared.generated.resources.settings_language_description
import whattoeat.shared.generated.resources.settings_language_english_description
import whattoeat.shared.generated.resources.settings_language_title

private data class AppLanguageOption(
    val language: AppLanguage,
    val title: String,
    val description: String,
    val icon: ImageVector
)

/**
 * 语言设置卡。
 *
 * 选项标题固定使用该语言自身的写法（中文 / English）；说明文字随界面语言变化。
 *
 * @param appLanguage 当前选中的语言。
 * @param onAppLanguageChange 用户选择新语言时的回调。
 * @param modifier 设置卡的布局修饰符。
 */
@Composable
internal fun LanguageSettings(
    appLanguage: AppLanguage,
    onAppLanguageChange: (AppLanguage) -> Unit,
    modifier: Modifier
) {
    val options = listOf(
        AppLanguageOption(
            language = AppLanguage.Chinese,
            title = "中文",
            description = stringResource(Res.string.settings_language_chinese_description),
            icon = MaterialIcons.Filled.Language
        ),
        AppLanguageOption(
            language = AppLanguage.English,
            title = "English",
            description = stringResource(Res.string.settings_language_english_description),
            icon = MaterialIcons.Filled.Language
        )
    )

    WtePaperCard(modifier = modifier){
        Text(
            text = stringResource(Res.string.settings_language_title),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = stringResource(Res.string.settings_language_description),
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
            options.forEach { option ->
                AppLanguageOptionRow(
                    option = option,
                    selected = option.language == appLanguage,
                    onClick = { onAppLanguageChange(option.language) }
                )
            }
        }
    }
}

/**
 * 单个语言选项。
 *
 * @param option 当前选项的语言、标题、说明和图标。
 * @param selected 是否为当前选中的语言。
 * @param onClick 用户点击整行时的回调。
 */
@Composable
private fun AppLanguageOptionRow(
    option: AppLanguageOption,
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
private fun LanguageSettingsPreview(){
    WhatToEatPreviewTheme {
        LanguageSettings(
            appLanguage = AppLanguage.Chinese,
            onAppLanguageChange = {},
            modifier = Modifier
        )
    }
}