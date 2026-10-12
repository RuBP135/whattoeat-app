package com.rubp.whattoeat.core.data

import androidx.sqlite.SQLiteException
import com.rubp.whattoeat.core.data.exception.DatabaseReadException
import com.rubp.whattoeat.core.data.exception.DatabaseWriteException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

internal fun <T> withDatabaseReadFlow(
    operation: String,
    block: () -> Flow<T>
): Flow<T> = flow {
    emitAll(block())
}.catch { cause ->
    if (cause is SQLiteException) {
        throw DatabaseReadException(operation, cause)
    }
    throw cause
}

internal suspend fun <T> withDatabaseWrite(
    operation: String,
    block: suspend () -> T
): T {
    return try {
        block()
    } catch (e: SQLiteException) {
        throw DatabaseWriteException(operation, e)
    }
}
