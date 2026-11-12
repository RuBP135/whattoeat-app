package com.rubp.whattoeat.feature.settings.data.preferences

import com.rubp.whattoeat.core.theme.ThemeMode
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getStringFlow
import kotlinx.coroutines.flow.map

private const val THEME_MODE = "theme_mode"

class SettingsPreferences(
    private val settings: ObservableSettings
) {

    @OptIn(ExperimentalSettingsApi::class)
    val themeModeFlow = settings
        .getStringFlow(THEME_MODE, ThemeMode.System.name)
        .map { storedThemeModeName ->
            ThemeMode.entries.firstOrNull { it.name == storedThemeModeName } ?: ThemeMode.System
        }

    fun saveThemeMode(themeMode: ThemeMode) {
        settings.putString(THEME_MODE, themeMode.name)
    }

}
