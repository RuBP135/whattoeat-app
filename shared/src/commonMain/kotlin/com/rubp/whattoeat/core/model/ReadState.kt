package com.rubp.whattoeat.core.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlin.coroutines.cancellation.CancellationException

sealed interface ReadState<out T> {
    data object Loading: ReadState<Nothing>
    data class Success<T>(val data: T): ReadState<T>
    data class Error(val exception: Exception): ReadState<Nothing>
}


fun <T> Flow<T>.asReadState(): Flow<ReadState<T>> {
    return map<T, ReadState<T>> { data ->
        ReadState.Success(data)
    }.onStart {
        emit(ReadState.Loading)
    }.catch { cause ->
        when(cause) {
            is CancellationException -> throw cause // 取消协程的信号
            is Exception -> emit(ReadState.Error(cause))
            else -> throw cause
        }
    }
}
