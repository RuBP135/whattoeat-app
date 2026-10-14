package com.rubp.whattoeat.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rubp.whattoeat.core.ui.components.dialog.WteConfirmDialog
import com.rubp.whattoeat.core.ui.components.snackbar.WteSnackbar
import com.rubp.whattoeat.core.ui.feedback.LocalUiFeedback
import com.rubp.whattoeat.core.ui.feedback.UiFeedback
import com.rubp.whattoeat.core.ui.task.UiTaskRunner
import kotlinx.coroutines.flow.collectLatest

val localUiTaskRunner = staticCompositionLocalOf<UiTaskRunner> {
    error("未提供 UiTaskRunner")
}

@Composable
fun WteFeedbackHost(
    feedback: UiFeedback,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val currentError by feedback.errorStateFlow.collectAsState()
    val snackbarHostState = remember(feedback) { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val runner = remember(scope, feedback) {
        UiTaskRunner(
            scope = scope,
            onMessage = feedback::showMessage,
            onError = feedback::showError
        )
    }

    LaunchedEffect(feedback, snackbarHostState) {
        feedback.messageStateFlow.collectLatest { message ->
            if (message != null) {
                snackbarHostState.showSnackbar(message.message)
                feedback.clearMessage(message.id)
            }
        }
    }

    Box(modifier = modifier) {
        CompositionLocalProvider(
            LocalUiFeedback provides feedback,
            localUiTaskRunner provides runner
        ) {
            content()
        }

        val error = currentError
        if (error != null) {
            val errorDetails = remember(error) { error.exception.stackTraceToString() }
            WteConfirmDialog(
                title = "未预期的错误",
                message = "哦不，遇到了一个未预期的错误。您可以点击“确定”上传异常日志，帮助我们排查并修复问题。" +
                        "日志可能包含部分本地信息，您可以先查看下方内容，再决定是否上传。如果不想上传，点击“取消”即可。\n\n$errorDetails",
                onConfirm = { feedback.clearError(error.id) },
                onDismiss = { feedback.clearError(error.id) }
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding() // 避开系统底部的导航栏
                .imePadding(), // 避开软键盘
            snackbar = { WteSnackbar(it) }
        )
    }
}
