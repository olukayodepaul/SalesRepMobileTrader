package com.mobbile.paul.ui.entryhistory


sealed class PostEntryUiState<out T> {
    object Loading : PostEntryUiState<Nothing>()
    data class Error(val message: String, val statusCode: Int) : PostEntryUiState<Nothing>()
    data class Success<out T>(val data: T) : PostEntryUiState<T>()
    object Empty : PostEntryUiState<Nothing>()
}
