package com.rubp.whattoeat.core.ui.clipboard

import androidx.compose.ui.platform.Clipboard

/**
 * 使用平台剪贴板写入纯文本。
 *
 * @param text 要复制的完整文本。
 */
expect suspend fun Clipboard.setPlainText(text: String)

/**
 * 读取剪贴板中的纯文本。
 *
 * @return 可读取的文本；剪贴板为空或没有文本时返回 null。
 */
expect suspend fun Clipboard.getPlainText(): String?
