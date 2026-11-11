package com.rubp.whattoeat

import android.app.Application
import com.rubp.whattoeat.app.database.AppDatabase
import com.rubp.whattoeat.app.database.buildAppDatabase
import com.rubp.whattoeat.app.database.createDatabaseBuilder
import com.rubp.whattoeat.app.di.AppContainer
import com.rubp.whattoeat.app.logging.initializeLogging
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

class MainApplication : Application() {
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()

        var logDirectory: Path? = null
        val logInitException = initializeLogging {
            logDirectory = Path(this.noBackupFilesDir.resolve("logs").absolutePath)
            SystemFileSystem.createDirectories(
                path = logDirectory,
                mustCreate = false
            )

            logDirectory
        }

        val appDatabase: AppDatabase = buildAppDatabase(createDatabaseBuilder(this))
        this.appContainer = AppContainer(
            database = appDatabase,
            settings = Settings() as ObservableSettings,
            logInitException = logInitException,
            logDirectory = logDirectory
        )
    }
}