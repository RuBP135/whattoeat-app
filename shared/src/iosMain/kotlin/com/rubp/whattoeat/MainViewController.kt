package com.rubp.whattoeat

import androidx.compose.ui.window.ComposeUIViewController
import com.rubp.whattoeat.app.App
import com.rubp.whattoeat.app.database.buildAppDatabase
import com.rubp.whattoeat.app.database.createDatabaseBuilder
import com.rubp.whattoeat.app.di.AppContainer
import com.rubp.whattoeat.app.logging.LoggingInitializationException
import com.rubp.whattoeat.app.logging.initializeLogging
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSNumber
import platform.Foundation.NSURL
import platform.Foundation.NSURLIsExcludedFromBackupKey
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private val appContainer by lazy {

    val logInitException = initializeLogging {
        val fileManager = NSFileManager.defaultManager

        val directory = memScoped {
            val nsError = alloc<ObjCObjectVar<NSError?>>()
            val supportDirectory = fileManager.URLForDirectory(
                directory = NSApplicationSupportDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = true,
                error = nsError.ptr
            )?.path ?: throw LoggingInitializationException(
                message = "Failed to obtain application support directory: " +
                    "domain=${nsError.value?.domain}, " +
                    "code=${nsError.value?.code}, " +
                    "reason=${nsError.value?.localizedDescription ?: "unknown"}"
            )

            "${supportDirectory}/logs"
        }

        memScoped {
            val directoryPath = Path(directory)
            SystemFileSystem.createDirectories(
                path = directoryPath,
                mustCreate = false
            )

            val nsError = alloc<ObjCObjectVar<NSError?>>()
            val success = NSURL.fileURLWithPath(
                path = directory,
                isDirectory = true,
            ).setResourceValue(
                value = NSNumber(bool = true),
                forKey = NSURLIsExcludedFromBackupKey,
                error = nsError.ptr
            )
            if(!success) {
                throw LoggingInitializationException(
                    message = "Failed to exclude log directory from backup: " +
                            "domain=${nsError.value?.domain}, " +
                            "code=${nsError.value?.code}, " +
                            "reason=${nsError.value?.localizedDescription ?: "unknown"}"
                )
            }

            directoryPath
        }
    }

    AppContainer(
        database = buildAppDatabase(createDatabaseBuilder()),
        settings = Settings() as ObservableSettings,
        logInitException = logInitException
    )
}

@Suppress("FunctionName", "unused")
fun MainViewController() = ComposeUIViewController { App(appContainer) }
