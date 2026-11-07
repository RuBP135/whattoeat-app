package com.rubp.whattoeat.core.ui.state

import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlin.coroutines.cancellation.CancellationException

class ReadStateMapper(
    tag: String
) {
    private val log = Logger.withTag(tag)

    /**
     * 将原始flow的每个元素转成ReadState，同时记录异常日志
     *
     * @param errorLogMessage 读取时发生异常/错误（除开协程取消）时记录的错误日志描述
     */
    fun <T> map(flow: Flow<T>, errorLogMessage: String): Flow<ReadState<T>> =
        flow.map<T, ReadState<T>> { data ->
            ReadState.Success(data)
        }.onStart {
            emit(ReadState.Loading)
        }.catch { cause ->
            when(cause) {
                is CancellationException -> throw cause // 取消协程的信号
                is Exception -> {
                    log.e(cause) { errorLogMessage }
                    emit(ReadState.Error(cause))
                }
                else -> {
                    log.e(cause) { errorLogMessage }
                    throw cause
                }
            }
        }
}