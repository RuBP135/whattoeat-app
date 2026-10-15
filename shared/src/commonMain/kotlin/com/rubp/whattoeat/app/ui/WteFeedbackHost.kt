package com.rubp.whattoeat.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rubp.whattoeat.core.ui.components.dialog.WteConfirmDialog
import com.rubp.whattoeat.core.ui.components.snackbar.WteSnackbar
import com.rubp.whattoeat.core.ui.feedback.LocalUiFeedback
import com.rubp.whattoeat.core.ui.feedback.UiFeedback
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WteFeedbackHost(
    feedback: UiFeedback,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val snackbarHostState = remember(feedback) { SnackbarHostState() }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(feedback.messageSharedFlow) {
        feedback.messageSharedFlow.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    LaunchedEffect(feedback.errorSharedFlow) {
        feedback.errorSharedFlow.collectLatest { showDialog = true }
    }

    Box(modifier = modifier) {
        CompositionLocalProvider(LocalUiFeedback provides feedback) {
            content()
        }


        if(showDialog) {
            WteConfirmDialog(
                title = "未预期的错误",
                message = "哦不，遇到了一个未预期的错误。您可以点击“确定”上传最近的日志，帮助我们排查并修复问题。" +
                        "日志可能包含部分本地信息，您可以先查看日志，再决定是否上传。如果不想上传，点击“取消”即可。",
                onConfirm = { showDialog = false },
                onDismiss = { showDialog = false }
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
