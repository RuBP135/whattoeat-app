package com.rubp.whattoeat.core.ui.feedback

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet

class UiFeedback {
    // 使用原子递增的 ID，确保相同内容也能作为新的反馈发布。
    private val idStateFlow = MutableStateFlow(0L)

    private val _messageStateFlow = MutableStateFlow<Message?>(null)
    val messageStateFlow: StateFlow<Message?> = _messageStateFlow.asStateFlow()

    private val _errorStateFlow = MutableStateFlow<ErrorFeedback?>(null)
    val errorStateFlow: StateFlow<ErrorFeedback?> = _errorStateFlow.asStateFlow()

    data class Message(val id: Long, val message: String)

    data class ErrorFeedback(val id: Long, val exception: Exception)

    fun showMessage(message: String) {
        val id = idStateFlow.updateAndGet { it + 1 }
        _messageStateFlow.value = Message(id, message)
    }

    fun showError(exception: Exception) {
        val id = idStateFlow.updateAndGet { it + 1 }
        _errorStateFlow.value = ErrorFeedback(id, exception)
    }

    fun clearMessage(id: Long) {
        _messageStateFlow.update { message ->
            message?.takeUnless { it.id == id }
        }
    }

    fun clearError(id: Long) {
        _errorStateFlow.update { error ->
            error?.takeUnless { it.id == id }
        }
    }
}
