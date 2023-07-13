package com.monke.yandextodo.presentationState.settingsFeature

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monke.yandextodo.data.repository.SettingsRepository
import com.monke.yandextodo.domain.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _appSettings = MutableStateFlow(
        AppSettings(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM))
    val appSettings: StateFlow<AppSettings> = _appSettings

    init {
        viewModelScope.launch {
            settingsRepository.getSettings().collect { settings ->
                if (settings != null) {
                    _appSettings.value = settings
                }
            }
        }
    }

    fun setAppThemeMode(themeMode: Int) {
        viewModelScope.launch {
            _appSettings.value.themeMode = themeMode
            settingsRepository.setSettings(_appSettings.value)
            AppCompatDelegate.setDefaultNightMode(themeMode)
        }
    }
}