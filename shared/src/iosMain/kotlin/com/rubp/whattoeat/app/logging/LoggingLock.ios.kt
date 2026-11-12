package com.rubp.whattoeat.app.logging

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSRecursiveLock

/** 使用 iOS 平台的可重入锁实现同步互斥。 */
@OptIn(ExperimentalForeignApi::class)
internal actual class LoggingLock actual constructor() {
    private val delegate = NSRecursiveLock()

    actual fun <T> withLock(block: () -> T): T {
        delegate.lock()
        try {
            return block()
        } finally {
            delegate.unlock()
        }
    }
}
