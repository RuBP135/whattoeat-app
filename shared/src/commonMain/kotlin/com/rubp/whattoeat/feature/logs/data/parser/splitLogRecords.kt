package com.rubp.whattoeat.feature.logs.data.parser

private val logHeaderPattern = Regex(
    """^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(?:\.\d+)?(?:Z|[+-]\d{2}:\d{2}) (?:Verbose|Debug|Info|Warn|Error|Assert): """,
    RegexOption.MULTILINE
)

/**
 * 按 Kermit 文件日志头（行首时间戳 + 级别）切分单次日志调用的输出：多行消息、空行与堆栈
 * 归入所在记录，未匹配日志头的内容也不丢弃；换行原样保留，便于整条复制。
 *
 * @param content 日志文件全文。
 */
internal fun splitLogRecords(content: String): List<String> {
    if (content.isEmpty()) return emptyList()

    val starts = logHeaderPattern.findAll(content).map { it.range.first }.toMutableList()
    if (starts.firstOrNull() != 0) starts.add(0, 0)

    return starts.mapIndexed { index, start ->
        content.substring(start, starts.getOrNull(index + 1) ?: content.length)
    }
}
