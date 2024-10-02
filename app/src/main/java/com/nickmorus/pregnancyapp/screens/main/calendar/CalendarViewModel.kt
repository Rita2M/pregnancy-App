package com.nickmorus.pregnancyapp.screens.main.calendar


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nickmorus.pregnancyapp.data.entities.CycleEntity
import com.nickmorus.pregnancyapp.data.entities.EmojiDayEntity
import com.nickmorus.pregnancyapp.data.entities.NotesEntity
import com.nickmorus.pregnancyapp.data.repositories.CalendarRepository
import com.nickmorus.pregnancyapp.data.repositories.EmojiRepository
import com.nickmorus.pregnancyapp.data.repositories.NoteRepository
import com.nickmorus.pregnancyapp.model.FeedModelState
import com.nickmorus.pregnancyapp.utils.information.EmojiType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject


@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: CalendarRepository,
    private val noteRepository:NoteRepository,
    private val emojiRepository: EmojiRepository,

) : ViewModel() {
    private val _feedModel = MutableStateFlow(FeedModelState())
    val feedModel : StateFlow<FeedModelState> = _feedModel

    private val _selectedCycle = MutableStateFlow<CycleEntity?>(null) // Для хранения выбранного цикла для редактирования
    val selectedCycle : StateFlow<CycleEntity?> get() =  _selectedCycle
    private val _selectedNote = MutableStateFlow<NotesEntity?>(null)
    val selectedNote : StateFlow<NotesEntity?> get() =  _selectedNote

    // Начальные значения для дат
    var startDateInput by mutableStateOf<LocalDate?>(null)
    val data: StateFlow<List<CycleEntity>> = repository.data
        .onStart {
            _feedModel.value = FeedModelState(loading = true) // Начинаем загрузку
        }
        .onEach {
            _feedModel.value = FeedModelState(loading = false) // Данные загрузились
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
//    fun onDateSelected(date: LocalDate) {
//        startDateInput = date
//        selectedCycle = CycleEntity(startDate = startDateInput.toString())
//    }
    val dataEmoji = emojiRepository.dataEmoji


    fun insertCycle( selectedDate: LocalDate,
                     duration: Int,
                     cycleLength: Int,
                     pregnant: Boolean) {
        viewModelScope.launch {
            val endDate =
                selectedDate.plusDays(duration.toLong() - 1)
            val newCycle = CycleEntity(
                startDate = selectedDate.toString(),
                endDate = endDate.toString(),
                cycleLength = cycleLength,
                duration = duration,
                pregnancy = pregnant
            )

            repository.insertCycle(newCycle)
            Log.d("insertCycle", "$newCycle ")
            FeedModelState()


        }
    }
   fun deleteCycle(id:Int) {
       viewModelScope.launch {
           try {
               repository.delete(id)

           } catch (e: Exception) {
               _feedModel.value = FeedModelState(error = true)
           }
       }
   }
    fun calculateMenstruationInfo(cycle: CycleEntity): Pair<Int?, LocalDate?> {
        val startDate = LocalDate.parse(cycle.startDate)
        val cycleLength = cycle.cycleLength ?: return null to null

        // Рассчитываем следующую менструацию
        val nextMenstruationDate = startDate.plusDays(cycleLength.toLong())
        val daysUntilMenstruation = ChronoUnit.DAYS.between(LocalDate.now(), nextMenstruationDate).toInt()
         Log.d("CalendarViewModel", "$startDate, $cycleLength")
        // Рассчитываем дату овуляции (середина цикла)
        val ovulationDate = startDate.plusDays((cycleLength / 2).toLong())

        return daysUntilMenstruation to ovulationDate
    }
    fun getCycleForDate(selectedDate: String, isPregnancy: Boolean){

        viewModelScope.launch {
            try {
                _feedModel.value = FeedModelState(loading = true)
                val note = noteRepository.getNoteById(selectedDate)

                Log.d("viewModelCalendar" , "$note, $selectedDate")
                _selectedNote.value = note
                _feedModel.value = FeedModelState()


            }catch (e:Exception){
                _feedModel.value = FeedModelState(error = true)
            }
        }

    }
    fun saveEmojiForDate(date: String, emojiType: EmojiType) {
        viewModelScope.launch {
            emojiRepository.saveEmojiDay(EmojiDayEntity(date, emojiType))
        }
    }

}
