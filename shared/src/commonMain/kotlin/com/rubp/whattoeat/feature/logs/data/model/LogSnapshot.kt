package com.rubp.whattoeat.feature.logs.data.model


data class LogFileInfo(
    val name: String,
    val sizeBytes: Long
)


data class LogSnapshot(
    val files: List<LogFileInfo>,
    val selectedFileName: String?,
    val contentBlocks: List<String>
)
