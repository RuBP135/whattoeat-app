package com.rubp.whattoeat.feature.logs.data.parser

private val logHeaderPattern = Regex(
    """^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(?:\.\d+)?(?:Z|[+-]\d{2}:\d{2}) (?:Verbose|Debug|Info|Warn|Error|Assert): """,
    RegexOption.MULTILINE
)

/**
 * 按当前 Kermit 文件日志的时间戳和级别前缀，划分单次日志调用输出的内容。
 *
 * 多行消息、空行及异常堆栈均归入同一条记录，保留原始换行以便完整复制。
 * 没有匹配到日志头的内容也会保留。
 *
 * @param content 日志文件的完整原文。
 */
internal fun splitLogRecords(content: String): List<String> {
    if (content.isEmpty()) return emptyList()

    val starts = logHeaderPattern.findAll(content).map { it.range.first }.toMutableList()
    if (starts.firstOrNull() != 0) starts.add(0, 0)

    return starts.mapIndexed { index, start ->
        content.substring(start, starts.getOrNull(index + 1) ?: content.length)
    }
}
