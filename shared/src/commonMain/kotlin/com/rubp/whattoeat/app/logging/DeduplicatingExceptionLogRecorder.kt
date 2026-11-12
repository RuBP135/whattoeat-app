package com.rubp.whattoeat.app.logging

/** 保存最近 64 个异常引用，供局部处理与全局兜底共享。 */
class DeduplicatingExceptionLogRecorder {
    private val lock = LoggingLock()
    private val recordedExceptions = arrayOfNulls<Throwable>(64)
    private var nextIndex = 0

    /**
     * 未记录过时执行日志调用，正常返回后登记引用。
     *
     * @param cause 要记录的原始异常。
     * @param record 保留调用方 tag 和操作描述的日志调用。
     */
    fun recordOnce(cause: Throwable, record: () -> Unit) {
        lock.withLock {
            if (wasRecorded(cause)) return@withLock

            record()

            recordedExceptions[nextIndex] = cause
            nextIndex = (nextIndex + 1) % recordedExceptions.size
        }
    }

    private fun wasRecorded(cause: Throwable): Boolean =
        recordedExceptions.any { it === cause }
}

/** 在同一把同步锁下执行 block；执行结束或抛错时释放锁。 */
internal expect class LoggingLock() {
    fun <T> withLock(block: () -> T): T
}
