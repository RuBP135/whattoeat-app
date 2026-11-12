package com.rubp.whattoeat.feature.logs.data.repository

import com.rubp.whattoeat.core.data.exception.withDataAccess
import com.rubp.whattoeat.feature.logs.data.model.LogFileInfo
import com.rubp.whattoeat.feature.logs.data.model.LogSnapshot
import com.rubp.whattoeat.feature.logs.data.parser.splitLogRecords
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString


class LogRepository(
    private val logDirectory: Path?
) {
    private val fileNamePattern = Regex("""error(?:-\d+)?\.log""")

    suspend fun readLogs(fileName: String? = null): LogSnapshot = withDataAccess {
        withContext(Dispatchers.IO) {
            val directory = checkNotNull(logDirectory) { "Log directory is unavailable" }
            val files = SystemFileSystem.list(directory).mapNotNull { path ->
                if (!fileNamePattern.matches(path.name)) return@mapNotNull null
                val metadata = SystemFileSystem.metadataOrNull(path)
                    ?: throw IOException("Failed to read log file metadata: ${path.name}")
                if (metadata.isRegularFile) LogFileInfo(path.name, metadata.size) else null
            }.sortedWith(
                compareBy<LogFileInfo> { it.name != "error.log" }.thenBy { it.name }
            )
            val selectedFile = files.find { it.name == fileName } ?: files.firstOrNull()
            val content = selectedFile?.let { file ->
                SystemFileSystem.source(Path(directory, file.name)).buffered().use { source ->
                    source.readString()
                }
            }.orEmpty()

            LogSnapshot(
                files = files,
                selectedFileName = selectedFile?.name,
                contentBlocks = splitLogRecords(content)
            )
        }
    }
}
