package com.rubp.whattoeat.feature.settings.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.rubp.whattoeat.core.components.CardButton
import com.rubp.whattoeat.core.components.TitleCard
import com.rubp.whattoeat.core.icons.GitHubIcon
import com.rubp.whattoeat.core.theme.ColorTheme
import com.rubp.whattoeat.feature.settings.viewmodel.SettingsViewModel


@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel
){
    val colorTheme by settingsViewModel.colorThemeStateFlow.collectAsState()
    SettingsContent(
        colorTheme = colorTheme,
        onColorThemeChange = settingsViewModel::saveColorTheme
    )
}

@Composable
fun SettingsContent(
    colorTheme: ColorTheme,
    onColorThemeChange: (ColorTheme) -> Unit
){

    val titleCardModifier = Modifier.width(300.dp)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.Top)
    ){

        item {
            ColorSettings(
                nowColorTheme = colorTheme,
                onColorThemeChange = onColorThemeChange,
                modifier = titleCardModifier
            )
        }
        item {
            AppInfo(titleCardModifier)
        }

    }
}

@Composable
private fun ColorSettings(
    nowColorTheme: ColorTheme,
    onColorThemeChange: (ColorTheme) -> Unit,
    modifier: Modifier
){
    TitleCard(
        title = "颜色设置",
        modifier = modifier
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "更换主题",
                    style = MaterialTheme.typography.titleSmall
                )
            }


            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ){
                for(colorTheme in ColorTheme.entries){
                    ColorChooserItem(
                        colorTheme = colorTheme,
                        chosen = colorTheme == nowColorTheme,
                        onClickChosen = { onColorThemeChange(colorTheme) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SettingsContentPreview(){
    SettingsContent(
        colorTheme = ColorTheme.Pink,
        onColorThemeChange = {}
    )
}

@Composable
private fun AppInfo(
    modifier: Modifier
){
    val uriHandler = LocalUriHandler.current

    TitleCard(
        title = "软件信息",
        modifier = modifier
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
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
}

@Composable
private fun ColorChooserItem(
    colorTheme: ColorTheme,
    chosen: Boolean,
    onClickChosen: () -> Unit
){
    Surface(
        color = colorTheme.toColorScheme(isSystemInDarkTheme()).primary,
        modifier = (if(chosen) Modifier.border(
            width = 2.dp,
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(6.dp)
        ) else Modifier)
            .padding(3.dp)
            .width(30.dp)
            .height(30.dp),
        shape = RoundedCornerShape(5.dp),
        onClick = onClickChosen
    ) {}
}

