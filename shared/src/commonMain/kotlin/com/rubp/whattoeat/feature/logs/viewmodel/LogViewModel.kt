package com.rubp.whattoeat.feature.logs.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubp.whattoeat.core.ui.async.ReadState
import com.rubp.whattoeat.core.ui.async.UiFlowHandler
import com.rubp.whattoeat.core.ui.async.UiTaskRunner
import com.rubp.whattoeat.core.ui.feedback.UiFeedback
import com.rubp.whattoeat.feature.logs.data.model.LogSnapshot
import com.rubp.whattoeat.feature.logs.data.repository.LogRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update


class LogViewModel(
    private val logRepository: LogRepository,
    feedback: UiFeedback
) : ViewModel() {
    private data class ReadRequest(val fileName: String? = null, val refreshKey: Int = 0)

    private val tag = "LogViewModel"
    private val requestStateFlow = MutableStateFlow(ReadRequest())
    private val handler = UiFlowHandler(
        tag = tag,
        onMessage = feedback::showMessage
    )
    private val runner = UiTaskRunner(
        scope = viewModelScope,
        tag = tag,
        onMessage = feedback::showMessage,
        onError = feedback::showError
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val logStateFlow: StateFlow<ReadState<LogSnapshot>> = requestStateFlow.flatMapLatest { request ->
        handler.asReadState(
            flow = flow { emit(logRepository.readLogs(request.fileName)) },
            errorLogMessage = "Failed to read log files"
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ReadState.Loading
    )

    fun selectFile(fileName: String) {
        requestStateFlow.update { it.copy(fileName = fileName) }
    }

    fun refresh() {
        requestStateFlow.update { it.copy(refreshKey = it.refreshKey + 1) }
    }


    fun copyLog(content: String, copyToClipboard: suspend (String) -> Unit) = runner.launch(
        successMessage = "日志已复制",
        errorLogMessage = "Failed to copy log record"
    ) {
        copyToClipboard(content)
    }
}
