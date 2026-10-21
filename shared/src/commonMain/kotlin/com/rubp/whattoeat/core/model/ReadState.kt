package com.rubp.whattoeat.core.model

sealed interface ReadState<out T> {
    data object Loading: ReadState<Nothing>
    data class Success<T>(val data: T): ReadState<T>
    data class Error(val exception: Exception): ReadState<Nothing>
}