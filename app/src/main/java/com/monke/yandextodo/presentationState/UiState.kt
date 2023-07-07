package com.monke.yandextodo.presentationState

sealed interface UiState {

    object Loading: UiState

    object Success: UiState

    object NeedSync: UiState

    data class Error(
        val message: String
    ): UiState

}