package com.rubp.whattoeat

import android.app.Application
import com.rubp.whattoeat.app.database.AppDatabase
import com.rubp.whattoeat.app.database.buildAppDatabase
import com.rubp.whattoeat.app.database.createDatabaseBuilder
import com.rubp.whattoeat.app.di.AppContainer
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings

class MainApplication : Application() {
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()

        val appDatabase: AppDatabase = buildAppDatabase(createDatabaseBuilder(this))
        this.appContainer = AppContainer(
            appDatabase,
            Settings() as ObservableSettings
        )
    }
}