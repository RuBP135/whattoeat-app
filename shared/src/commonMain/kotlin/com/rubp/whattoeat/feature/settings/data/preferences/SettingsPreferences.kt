package com.rubp.whattoeat.feature.settings.data.preferences

import com.rubp.whattoeat.core.theme.ColorTheme
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getStringFlow
import kotlinx.coroutines.flow.map

const val COLOR_THEME = "color_theme"

class SettingsPreferences(
    private val settings: ObservableSettings
) {

    @OptIn(ExperimentalSettingsApi::class)
    val colorThemeFlow = settings
        .getStringFlow(COLOR_THEME, ColorTheme.Pink.name)
        .map { ColorTheme.valueOf(it) }


    fun saveColorTheme(theme: ColorTheme) {
        settings.putString(COLOR_THEME, theme.name)
    }

}