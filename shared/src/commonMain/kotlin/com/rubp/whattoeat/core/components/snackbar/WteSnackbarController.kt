package com.rubp.whattoeat.core.components.snackbar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WteSnackbarController(
    val hostState: SnackbarHostState,
    private val scope: CoroutineScope
) {
    private var job: Job? = null;


    fun showSnackbar(
        message: String
    ) {
        job?.cancel()

        job = scope.launch {
            hostState.showSnackbar(message)
        }
    }
}

@Composable
fun rememberWteSnackbarController(hostState: SnackbarHostState): WteSnackbarController {
    val scope = rememberCoroutineScope()
    return remember(hostState) { WteSnackbarController(hostState, scope) }
}