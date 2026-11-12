package com.rubp.whattoeat

import androidx.compose.ui.window.ComposeUIViewController
import co.touchlab.kermit.Logger
import com.rubp.whattoeat.app.App
import com.rubp.whattoeat.app.database.buildAppDatabase
import com.rubp.whattoeat.app.database.createDatabaseBuilder
import com.rubp.whattoeat.app.di.AppContainer
import com.rubp.whattoeat.app.logging.DeduplicatingExceptionLogRecorder
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
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.setUnhandledExceptionHook
import kotlin.native.terminateWithUnhandledException

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class, ExperimentalNativeApi::class)
private val appContainer by lazy {

    var logDirectory: Path? = null
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
            logDirectory = Path(directory)
            SystemFileSystem.createDirectories(
                path = logDirectory,
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

            logDirectory
        }
    }

    val recorder = DeduplicatingExceptionLogRecorder()
    var previousHook: ((Throwable) -> Unit)? = null

    previousHook = setUnhandledExceptionHook { throwable ->
        try {
            recorder.recordOnce(throwable) {
                Logger.withTag("UncaughtException").e(throwable = throwable) {
                    "Unhandled Kotlin exception"
                }
            }
        } finally {
            try {
                previousHook?.invoke(throwable)
            } finally {
                terminateWithUnhandledException(throwable)
            }
        }
    }

    AppContainer(
        database = buildAppDatabase(createDatabaseBuilder()),
        settings = Settings() as ObservableSettings,
        logInitException = logInitException,
        logDirectory = logDirectory,
        deduplicatingExceptionLogRecorder = recorder
    )
}

@Suppress("FunctionName", "unused")
fun MainViewController() = ComposeUIViewController { App(appContainer) }
