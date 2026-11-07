package com.rubp.whattoeat.core.ui.task

import co.touchlab.kermit.Logger
import com.rubp.whattoeat.core.domain.exception.BusinessException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * 用于异步执行 ui 任务，并且自动处理异常（预期异常显示提示消息，非预期异常显示错误弹窗）
 *
 * @param scope 用于启动任务并管理任务生命周期的协程作用域
 * @param tag 日志的标签
 * @param onMessage 反馈提示消息的挂起方法，应当传入 UiFeedback::showMessage
 * @param onError 反馈错误的挂机方法，应当传入 UiFeedback::showError
 */
class UiTaskRunner(
    private val scope: CoroutineScope,
    tag: String,
    private val onMessage: suspend (String) -> Unit,
    private val onError: suspend (Exception) -> Unit
) {
    private val log = Logger.withTag(tag)

    /**
     * 异步执行任务，并自动处理异常、给出成功提示
     *
     * @param successMessage 完成操作后的成功提示，展示给用户看（后续打算换成字符串资源引用）
     * @param errorLogMessage 任务发生非业务异常/错误（除开协程取消）时记录的错误日志描述
     * @param task 需要执行的任务
     */
    fun launch(
        successMessage: String? = null, // null 表示成功时不提示。
        errorLogMessage: String,
        task: suspend () -> Unit
    ): Job = scope.launch {
        try {
            task()
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: BusinessException) {
            onMessage(exception.message)
            return@launch
        } catch (exception: Exception) {
            log.e(exception) { errorLogMessage }
            onError(exception)
            return@launch
        }

        successMessage?.let{ onMessage(it) }
    }
}
