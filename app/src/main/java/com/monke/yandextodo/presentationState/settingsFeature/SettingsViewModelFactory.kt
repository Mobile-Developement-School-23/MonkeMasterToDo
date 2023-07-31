package com.monke.yandextodo.presentationState.settingsFeature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monke.yandextodo.data.repository.SettingsRepository
import javax.inject.Inject

class SettingsViewModelFactory @Inject constructor(
    private val settingsRepository: SettingsRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(settingsRepository) as T
    }


}