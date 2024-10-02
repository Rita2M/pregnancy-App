package com.nickmorus.pregnancyapp.screens.main.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nickmorus.pregnancyapp.data.repositories.MotionCounterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val repository: MotionCounterRepository,

): ViewModel(){
    private val _countMotion = MutableStateFlow(0)
    val countMotion: StateFlow<Int> = _countMotion.asStateFlow()
    private val currentDate = LocalDate.now()
    init {
        getCount()
    }

   private fun getCount() {
       viewModelScope.launch {
           val count = repository.getMovementCountForToday(currentDate)
           _countMotion.value = count
       }
   }

    val dataAll = repository.getAll
    fun saveMovementDetected() {
       viewModelScope.launch {
            val currentTime = LocalTime.now()
            repository.insertMotion(currentDate, currentTime)
           _countMotion.value += 1

        }
    }
    fun saveMovementWithTime(time: LocalTime){
        viewModelScope.launch {
            repository.insertMotion(currentDate, time)
            _countMotion.value += 1
        }
    }


    fun deleteLastMovement(onError: () -> Unit) {
        viewModelScope.launch {
            val success = repository.deleteLastMovement(currentDate)
            if (_countMotion.value > 0) _countMotion.value -=1

            if (!success) {
                onError()
            }
        }
    }

}
