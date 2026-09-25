package com.rubp.whattoeat.app.config

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings

object Config {
    private val settings: Settings = Settings()
    val observableSettings: ObservableSettings = settings as ObservableSettings
}