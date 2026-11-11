package com.rubp.whattoeat.core.ui.clipboard

import androidx.compose.ui.platform.Clipboard

/**
 * 使用平台剪贴板写入纯文本。
 *
 * @param text 要复制的完整文本。
 */
expect suspend fun Clipboard.setPlainText(text: String)
