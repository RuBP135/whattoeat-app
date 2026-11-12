package com.rubp.whattoeat.feature.settings.data.repository

import com.rubp.whattoeat.core.data.exception.DataAccessException
import com.rubp.whattoeat.core.data.exception.withDataAccess
import com.rubp.whattoeat.core.theme.ThemeMode
import com.rubp.whattoeat.feature.settings.data.preferences.SettingsPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

class SettingsRepository(
    private val settingsPreferences: SettingsPreferences
){
    val themeModeFlow: Flow<ThemeMode> = settingsPreferences.themeModeFlow
        .catch { throw DataAccessException.wrapOrThrowCancellationException(it) }

    suspend fun saveThemeMode(themeMode: ThemeMode) =
        withDataAccess { settingsPreferences.saveThemeMode(themeMode) }
}