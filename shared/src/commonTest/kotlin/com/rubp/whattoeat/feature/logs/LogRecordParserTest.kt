package com.rubp.whattoeat.feature.logs

import co.touchlab.kermit.DefaultFormatter
import co.touchlab.kermit.Message
import co.touchlab.kermit.Severity
import co.touchlab.kermit.Tag
import com.rubp.whattoeat.feature.logs.data.parser.splitLogRecords
import kotlin.test.Test
import kotlin.test.assertEquals

class LogRecordParserTest {
    @Test
    fun keepsStackTraceSuppressedAndCauseChainTogether() {
        val record = header("2026-11-11T12:00:00.123Z", "Failed to read logs") + "\n" +
            "DataAccessUnknownException: read failed\n" +
            "\tat Reader.read(Reader.kt:1)\n" +
            "\tat Reader.read(Reader.kt:2)\n" +
            "\tSuppressed: IllegalStateException: close failed\n" +
            "\t\tat Reader.close(Reader.kt:12)\n" +
            "Caused by: IOException: disk unavailable\n\tat FileSystem.read(FileSystem.kt:8)\n\n"

        assertEquals(listOf(record), splitLogRecords(record))
    }

    @Test
    fun separatesCallsAndPreservesOriginalText() {
        val first = header("2026-11-11T12:00:00Z", "First failure") + "\n" +
            "IllegalStateException: first\n\tat First.run(First.kt:1)\n\n"
        val second = header("2026-11-11T12:00:01Z", "Second failure") + "\n" +
            "IOException: second\nCaused by: IllegalStateException: cause\n\n"

        assertEquals(listOf(first, second), splitLogRecords(first + second))
    }

    @Test
    fun retainsMultilineMessageAndBlankLines() {
        val record = header("2026-11-11T12:00:00Z", "Failed to process response") + "\n\n" +
            "2026-11-11T12:00:01Z is part of the message\n" +
            "\t2026-11-11T12:00:02Z Error: indented message\n\n" +
            "IOException: request failed\nCaused by: IllegalStateException: invalid response\n"
        val next = header("2026-11-11T12:00:03Z", "Another call without throwable") + "\n"

        assertEquals(listOf(record, next), splitLogRecords(record + next))
    }

    @Test
    fun preservesUnrecognizedContentAndIncompletePrefix() {
        val prefix = "Caused by: IOException: existing incomplete record\n"
        val record = header("2026-11-11T12:00:00Z", "Next call") + "\n"

        assertEquals(listOf(prefix), splitLogRecords(prefix))
        assertEquals(listOf(prefix, record), splitLogRecords(prefix + record))
    }

    private fun header(timestamp: String, message: String): String =
        "$timestamp " + DefaultFormatter.formatMessage(
            severity = Severity.Error,
            tag = Tag("LogViewModel"),
            message = Message(message)
        )
}
