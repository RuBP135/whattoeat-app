package com.rubp.whattoeat.feature.logs.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rubp.whattoeat.core.theme.WhatToEatPreviewTheme
import com.rubp.whattoeat.core.theme.WteTheme
import com.rubp.whattoeat.core.ui.async.ReadState
import com.rubp.whattoeat.core.ui.components.WteTopBar
import com.rubp.whattoeat.core.ui.clipboard.setPlainText
import com.rubp.whattoeat.core.ui.components.state.WteErrorPanel
import com.rubp.whattoeat.feature.logs.data.model.LogFileInfo
import com.rubp.whattoeat.feature.logs.data.model.LogSnapshot
import com.rubp.whattoeat.feature.logs.viewmodel.LogViewModel

@Composable
fun LogScreen(
    logViewModel: LogViewModel,
    onReturnToSettings: () -> Unit
) {
    val logState by logViewModel.logStateFlow.collectAsState()
    val clipboard = LocalClipboard.current
    LogContent(
        logState = logState,
        onFileSelected = logViewModel::selectFile,
        onRefresh = logViewModel::refresh,
        onCopy = { content -> logViewModel.copyLog(content, clipboard::setPlainText) },
        onReturnToSettings = onReturnToSettings
    )
}

@Composable
private fun LogContent(
    logState: ReadState<LogSnapshot>,
    onFileSelected: (String) -> Unit,
    onRefresh: () -> Unit,
    onCopy: (String) -> Unit,
    onReturnToSettings: () -> Unit
) {
    Scaffold(
        topBar = {
            WteTopBar(
                title = "日志",
                onClickReturn = onReturnToSettings
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            when (logState) {
                ReadState.Loading -> CircularProgressIndicator()
                is ReadState.Error -> WteErrorPanel(
                    message = "日志读取失败，请刷新重试",
                    retryText = "刷新",
                    retry = onRefresh
                )
                is ReadState.Success -> LogDetails(
                    snapshot = logState.data,
                    onFileSelected = onFileSelected,
                    onCopy = onCopy,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun LogDetails(
    snapshot: LogSnapshot,
    onFileSelected: (String) -> Unit,
    onCopy: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (snapshot.files.isEmpty()) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(
                text = "暂无日志",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        return
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(snapshot.files, key = { it.name }) { file ->
                FilterChip(
                    selected = file.name == snapshot.selectedFileName,
                    onClick = { onFileSelected(file.name) },
                    label = { Text(file.name) },
                    modifier = Modifier.heightIn(min = 48.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = WteTheme.extendedColors.paper,
                        selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
            }
        }

        val selectedFile = snapshot.files.find { it.name == snapshot.selectedFileName }
        Text(
            text = "${snapshot.selectedFileName} · ${selectedFile?.sizeBytes ?: 0} 字节",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (snapshot.contentBlocks.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "此文件暂无日志", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            key(snapshot.selectedFileName) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(snapshot.contentBlocks) { index, block ->
                        LogRecordItem(
                            number = index + 1,
                            content = block,
                            onCopy = onCopy,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun LogContentLightPreview() {
    LogContentPreview(darkTheme = false)
}

@Preview
@Composable
private fun LogContentDarkPreview() {
    LogContentPreview(darkTheme = true)
}

@Composable
private fun LogContentPreview(darkTheme: Boolean) {
    WhatToEatPreviewTheme(darkTheme) {
        LogContent(
            logState = ReadState.Success(
                LogSnapshot(
                    files = listOf(LogFileInfo("error.log", 128), LogFileInfo("error-1.log", 256)),
                    selectedFileName = "error.log",
                    contentBlocks = listOf(
                        "2026-11-11T12:00:00Z Error: (SettingsViewModel) Failed to observe theme mode\n" +
                            "DataAccessUnknownException: Example\nCaused by: IOException: Read failed\n",
                        "2026-11-11T12:01:00Z Error: (SettingsViewModel) Failed to save theme mode\n" +
                            "IOException: Write failed\n"
                    )
                )
            ),
            onFileSelected = {},
            onRefresh = {},
            onCopy = {},
            onReturnToSettings = {}
        )
    }
}
