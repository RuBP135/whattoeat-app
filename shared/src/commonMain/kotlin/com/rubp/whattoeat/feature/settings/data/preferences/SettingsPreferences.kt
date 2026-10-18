package com.rubp.whattoeat.feature.settings.data.preferences

import com.rubp.whattoeat.core.i18n.AppLanguage
import com.rubp.whattoeat.core.theme.ThemeMode
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getStringFlow
import kotlinx.coroutines.flow.map

private const val THEME_MODE = "theme_mode"
private const val APP_LANGUAGE = "app_language"



class SettingsPreferences(
    private val settings: ObservableSettings
) {

    @OptIn(ExperimentalSettingsApi::class)
    val themeModeFlow = settings
        .getStringFlow(THEME_MODE, ThemeMode.System.name)
        .map { storedThemeModeName ->
            ThemeMode.entries.firstOrNull { it.name == storedThemeModeName } ?: ThemeMode.System
        }

    @OptIn(ExperimentalSettingsApi::class)
    val appLanguageFlow = settings
        .getStringFlow(APP_LANGUAGE, AppLanguage.Chinese.name)
        .map { storedName ->
            AppLanguage.entries.firstOrNull { it.name == storedName } ?: AppLanguage.Chinese
        }

    fun saveThemeMode(themeMode: ThemeMode) {
        settings.putString(THEME_MODE, themeMode.name)
    }

    fun saveAppLanguage(language: AppLanguage) {
        settings.putString(APP_LANGUAGE, language.name)
    }


    /**
     * 同步读取已保存的语言，作为 ViewModel 初始值，避免首帧闪回中文。
     *
     * @return 已保存的语言；无记录或值非法时为中文。
     */
    fun currentAppLanguage(): AppLanguage {
        val storedName = settings.getString(APP_LANGUAGE, AppLanguage.Chinese.name)
        return AppLanguage.entries.firstOrNull { it.name == storedName } ?: AppLanguage.Chinese
    }

}
