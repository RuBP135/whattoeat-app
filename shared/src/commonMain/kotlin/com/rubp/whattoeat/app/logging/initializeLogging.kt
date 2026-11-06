package com.rubp.whattoeat.app.logging

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.io.RollingFileLogWriter
import co.touchlab.kermit.io.RollingFileLogWriterConfig
import co.touchlab.kermit.platformLogWriter
import kotlinx.io.files.Path

/**
 *
 * 配置将Error等级以上日志同时写进系统日志和日志文件中。
 *
 * @return 同步初始化成功时返回 null；
 * 如果文件日志部分初始化出现异常，将返回这个异常，但系统日志仍然可以正常使用
 */
fun initializeLogging(
    logDirectory: () -> Path
) : LoggingInitializationException? {

    Logger.setMinSeverity(Severity.Error)

    return try {

        val fileWriter = RollingFileLogWriter(
            config = RollingFileLogWriterConfig(
                logFileName = "error",
                logFilePath = logDirectory(),
                rollOnSize = 1024L * 1024L, // 1MiB
                maxLogFiles = 5,
                logTag = true,
                prependTimestamp = true
            )
        )

        Logger.setLogWriters(platformLogWriter(), fileWriter)

        null
    } catch(exception: Exception) {

        val initException = exception as? LoggingInitializationException
            ?: LoggingInitializationException(
                cause = exception
            )

        Logger.setLogWriters(platformLogWriter())

        Logger.e(throwable = exception) {
            "file logging initialization failed, using system logging only"
        }

        initException
    }
}