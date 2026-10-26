package com.rubp.whattoeat.core.data.exception

import androidx.sqlite.SQLiteException
import io.ktor.utils.io.CancellationException

sealed class DataAccessException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause) {

    companion object {
        fun wrapOrThrowCancellationException(cause: Throwable): DataAccessException =
            when(cause) {
                is CancellationException -> throw cause
                is SQLiteException -> DatabaseException(cause = cause)
                else -> DataAccessUnknownException(cause = cause)
            }
    }
}

/**
 * 将原始异常进行包装
 */
suspend fun <T> withDataAccess(block: suspend () -> T) : T =
    try {
        block()
    } catch (cause: Throwable) {
        throw DataAccessException.wrapOrThrowCancellationException(cause)
    }


class DatabaseException(
    message: String? = null,
    cause: Throwable? = null
) : DataAccessException(message, cause)

class DataAccessUnknownException(
    message: String? = null,
    cause: Throwable? = null
) : DataAccessException(message, cause)