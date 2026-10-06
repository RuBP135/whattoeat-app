package com.rubp.whattoeat.feature.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rubp.whattoeat.BuildKonfig
import com.rubp.whattoeat.app.navigation.MainBottomBarSpacer
import com.rubp.whattoeat.core.components.button.CardButton
import com.rubp.whattoeat.core.components.card.WtePaperCard
import com.rubp.whattoeat.core.icons.GitHubIcon
import com.rubp.whattoeat.core.theme.ThemeMode
import com.rubp.whattoeat.core.theme.WhatToEatPreviewTheme
import com.rubp.whattoeat.feature.settings.viewmodel.SettingsViewModel


@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel
){
    val themeMode by settingsViewModel.themeModeStateFlow.collectAsState()
    SettingsContent(
        themeMode = themeMode,
        onThemeModeChange = settingsViewModel::saveThemeMode
    )
}

@Composable
fun SettingsContent(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit
){
    val cardModifier = Modifier
        .widthIn(max = 560.dp)
        .fillMaxWidth()

    Scaffold(
        bottomBar = { MainBottomBarSpacer() }
    ){ paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(
                start = 24.dp,
                top = 40.dp,
                end = 24.dp,
                bottom = 32.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.Top)
        ){
            item {
                ThemeModeSettings(
                    themeMode = themeMode,
                    onThemeModeChange = onThemeModeChange,
                    modifier = cardModifier
                )
            }
            item {
                AppInfo(cardModifier)
            }
        }
    }


}


@Composable
private fun AppInfo(
    modifier: Modifier
){
    val uriHandler = LocalUriHandler.current

    WtePaperCard(
        modifier = modifier
    ){
        Text(
            text = "软件信息",
            style = MaterialTheme.typography.titleMedium
        )
        CardButton(
            title = "关于本程序",
            subtitle = "版本号：${BuildKonfig.VERSION_NAME}",
            icon = {
                Icon(
                    imageVector = GitHubIcon,
                    contentDescription = "跳转至github仓库",
                    modifier = Modifier.size(36.dp)
                )
            }
        ){
            uriHandler.openUri("https://github.com/RuBP-cmd/WhatToEat2")
        }
    }
}

@Preview
@Composable
private fun SettingsContentLightPreview(){
    WhatToEatPreviewTheme (false){
        SettingsContent(
            themeMode = ThemeMode.Light,
            onThemeModeChange = {}
        )
    }
}

@Preview
@Composable
private fun SettingsContentDarkPreview(){
    WhatToEatPreviewTheme (true){
        SettingsContent(
            themeMode = ThemeMode.Dark,
            onThemeModeChange = {}
        )
    }
}