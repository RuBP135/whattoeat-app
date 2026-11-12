package com.rubp.whattoeat.core.ui.clipboard

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard

@OptIn(ExperimentalComposeUiApi::class)
actual suspend fun Clipboard.setPlainText(text: String) {
    setClipEntry(ClipEntry.withPlainText(text))
}

@OptIn(ExperimentalComposeUiApi::class)
actual suspend fun Clipboard.getPlainText(): String? {
    return getClipEntry()?.getPlainText()
}
