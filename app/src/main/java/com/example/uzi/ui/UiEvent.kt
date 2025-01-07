package com.example.uzi.ui

sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent() // Для тостов
    data class ShowError(val errorMessage: String) : UiEvent() // Ошибка
}
