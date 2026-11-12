package com.rubp.whattoeat.core.ui.async

import co.touchlab.kermit.Logger
import com.rubp.whattoeat.app.logging.DeduplicatingExceptionLogRecorder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlin.coroutines.cancellation.CancellationException

/**
 * 将读取失败转换为 UI 状态或反馈，并对继续外抛的严重错误去重记录。
 *
 * @param tag 日志标签。
 * @param recorder 与全局回调共用的异常日志去重记录器。
 * @param onMessage 普通读取失败时发送用户提示的回调。
 */
class UiFlowHandler(
    tag: String,
    private val recorder: DeduplicatingExceptionLogRecorder,
    private val onMessage: suspend (String) -> Unit,
) {
    private val log = Logger.withTag(tag)

    /**
     * 将原始 Flow 转换为 [ReadState]，并记录上游读取异常。
     *
     * 每次收集开始时发射 [ReadState.Loading]，成功读取的值映射为 [ReadState.Success]。
     * 上游抛出普通 [Exception] 时记录日志并发射 [ReadState.Error]，随后当前流正常结束。
     * 本方法不自动重新订阅，也不发送提示消息；错误状态由调用方展示。
     * [CancellationException] 直接传播；其他 [Throwable] 记录日志后继续抛出。
     *
     * @param errorLogMessage 读取时发生异常/错误（除开协程取消）时记录的错误日志描述
     */
    fun <T> asReadState(
        flow: Flow<T>,
        errorLogMessage: String
    ): Flow<ReadState<T>> =
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
                    try {
                        recorder.recordOnce(cause) {
                            log.e(cause) { errorLogMessage }
                        }
                    } finally {
                        throw cause
                    }
                }
            }
        }

    /**
     * 保留原始 Flow 的值类型，仅处理上游读取异常。
     *
     * 上游抛出普通 [Exception] 时记录日志，并通过消息回调发送 [errorMessage]；
     * 随后当前流正常结束，不发射替代值，也不自动重新订阅。
     * [CancellationException] 直接传播；其他 [Throwable] 记录日志后继续抛出。
     *
     * @param flow 要观察的原始数据流。
     * @param errorMessage 读取失败时传给消息回调的用户提示文本。
     * @param errorLogMessage 读取失败时记录的开发者日志描述。
     */
    fun <T> asReadValue(
        flow: Flow<T>,
        errorMessage: String,
        errorLogMessage: String
    ): Flow<T> = flow.catch { cause ->
        when (cause) {
            is CancellationException -> throw cause // 取消协程的信号
            is Exception -> {
                log.e(cause) { errorLogMessage }
                onMessage(errorMessage)
            }
            else -> {
                try {
                    recorder.recordOnce(cause) {
                        log.e(cause) { errorLogMessage }
                    }
                } finally {
                    throw cause
                }
            }
        }
    }
}
