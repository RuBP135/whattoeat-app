package com.rubp.whattoeat

import androidx.compose.ui.window.ComposeUIViewController
import com.rubp.whattoeat.app.App
import com.rubp.whattoeat.app.database.buildAppDatabase
import com.rubp.whattoeat.app.database.createDatabaseBuilder
import com.rubp.whattoeat.app.di.AppContainer
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings

private val appContainer by lazy {
    AppContainer(
        buildAppDatabase(createDatabaseBuilder()),
        Settings() as ObservableSettings
    )
}

@Suppress("FunctionName", "unused")
fun MainViewController() = ComposeUIViewController { App(appContainer) }
