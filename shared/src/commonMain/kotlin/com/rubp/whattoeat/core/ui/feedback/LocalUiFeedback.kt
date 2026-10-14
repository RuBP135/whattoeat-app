package com.rubp.whattoeat.core.ui.feedback

import androidx.compose.runtime.staticCompositionLocalOf

val LocalUiFeedback = staticCompositionLocalOf<UiFeedback> {
    error("未提供 UiFeedback")
}
