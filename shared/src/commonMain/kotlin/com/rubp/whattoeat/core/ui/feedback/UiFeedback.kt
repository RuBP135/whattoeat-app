package com.rubp.whattoeat.core.ui.feedback

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class UiFeedback {

    private val idStateFlow = MutableStateFlow(0L)

    private val _messageSharedFlow = MutableSharedFlow<String>()
    val messageSharedFlow: SharedFlow<String> = _messageSharedFlow.asSharedFlow()

    private val _errorSharedFlow = MutableSharedFlow<Exception>()
    val errorSharedFlow: SharedFlow<Exception> = _errorSharedFlow.asSharedFlow()


    suspend fun showMessage(message: String) {
        _messageSharedFlow.emit(message)
    }

    suspend fun showError(exception: Exception) {
        _errorSharedFlow.emit(exception)
    }

}
