package com.rubp.whattoeat.app.logging

import java.util.concurrent.locks.ReentrantLock

/** 使用 Android 平台的可重入锁实现同步互斥。 */
internal actual class LoggingLock actual constructor() {
    private val delegate = ReentrantLock()

    actual fun <T> withLock(block: () -> T): T {
        delegate.lock()
        try {
            return block()
        } finally {
            delegate.unlock()
        }
    }
}
