package com.rubp.whattoeat.core.ui.task

import com.rubp.whattoeat.core.domain.exception.ExpectedException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UiTaskRunner(
    private val scope: CoroutineScope,
    private val onMessage: suspend (String) -> Unit,
    private val onError: suspend (Exception) -> Unit
) {
    /** 在提供的协程作用域中执行任务；
     * 可处理异常将提示错误消息；
     * 其他异常弹出错误窗口，允许用户上传报错信息
     */
    fun launch(
        successMessage: String? = null, // null 表示成功时不提示。
        task: suspend () -> Unit
    ): Job = scope.launch {
        try {
            task()
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: ExpectedException) {
            onMessage(exception.message)
            return@launch
        } catch (exception: Exception) {
            onError(exception)
            return@launch
        }

        successMessage?.let{ onMessage(it) }
    }
}
