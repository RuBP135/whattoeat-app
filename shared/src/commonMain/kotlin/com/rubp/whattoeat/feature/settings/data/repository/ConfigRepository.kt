package com.rubp.whattoeat.feature.settings.data.repository

import com.rubp.whattoeat.core.theme.ColorTheme
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.getLongFlow
import com.russhwolf.settings.coroutines.getStringFlow
import kotlinx.coroutines.flow.map

object ConfigRepository {
    private val settings: Settings = Settings()
    private val observableSettings = settings as ObservableSettings

    private object Keys {
        const val COLOR_THEME = "color_theme"
        const val SAVED_TABLE_ID = "saved_table_id"
    }

    @OptIn(ExperimentalSettingsApi::class)
    val colorThemeFlow = observableSettings
        .getStringFlow(Keys.COLOR_THEME, ColorTheme.Pink.name)
        .map { ColorTheme.valueOf(it) }

    @OptIn(ExperimentalSettingsApi::class)
    val savedTableIdFlow = observableSettings.getLongFlow(Keys.SAVED_TABLE_ID, 1L)

    fun saveColorTheme(theme: ColorTheme) {
        settings.putString(Keys.COLOR_THEME, theme.name)
    }

    fun saveTableId(tableId: Long) {
        settings.putLong(Keys.SAVED_TABLE_ID, tableId)
    }

}