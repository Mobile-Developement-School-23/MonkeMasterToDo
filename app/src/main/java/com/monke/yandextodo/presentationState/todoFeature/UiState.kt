package com.monke.yandextodo.presentationState.todoFeature

sealed interface UiState {

    object Loading: UiState

    object Success: UiState

    object NeedSync: UiState

    data class Error(
        val message: String
    ): UiState

}