package com.nickmorus.pregnancyapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val appSettings: AppSettings
) : ViewModel() {

    // Подписка на данные из DataStore
    val isPregnant: StateFlow<Boolean> = appSettings.isPregnant.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val firstDay: StateFlow<String?> = appSettings.firstDay.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    // Функции для изменения значений
    fun setFirstDay(firstDay: String) {
        viewModelScope.launch {
            appSettings.setFirstDayPregnant(firstDay)
        }
    }

    fun setPregnancyState(isPregnant: Boolean) {
        viewModelScope.launch {
            appSettings.setPregnancy(isPregnant)
        }
    }
}
