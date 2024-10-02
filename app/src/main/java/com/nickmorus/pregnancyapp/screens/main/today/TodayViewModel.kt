package com.nickmorus.pregnancyapp.screens.main.today

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nickmorus.pregnancyapp.data.dao.BodySizeDao
import com.nickmorus.pregnancyapp.data.dao.PregnancyWeekDataDao
import com.nickmorus.pregnancyapp.data.entities.BodySizeEntity
import com.nickmorus.pregnancyapp.data.entities.PregnancyWeekDataEntity
import com.nickmorus.pregnancyapp.data.repositories.TodayRepository
import com.nickmorus.pregnancyapp.model.FeedModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val dao: PregnancyWeekDataDao,
    private val bodySizeDao: BodySizeDao,
    private val repository: TodayRepository,
    private val savedStateHandle: SavedStateHandle


) : ViewModel() {

    val bodySize: Flow<List<BodySizeEntity?>> = repository.bodySize//параметры человека

    // Получаем первый элемент списка
    private val elementary = bodySize
        .map { it.firstOrNull() } // Преобразуем Flow<List<BodySizeEntity?>> в Flow<BodySizeEntity?>

    // Собираем данные в StateFlow для синхронизации с UI
    val elementaryBodySizeState: StateFlow<BodySizeEntity?> = elementary
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val _feedModel = MutableStateFlow(FeedModelState())
    val feedModel: StateFlow<FeedModelState> = _feedModel

    private val _week = MutableStateFlow<Int?>(null)
    val week: StateFlow<Int?> = _week
    private val _record = MutableStateFlow<Boolean>(false)
    val record: StateFlow<Boolean> = _record


    @OptIn(ExperimentalCoroutinesApi::class)
    val weekData: Flow<PregnancyWeekDataEntity?> = week.flatMapLatest { we ->
        we?.let { dao.getDataForWeek(it) } ?: flowOf(null)
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)
    // private val _weightGain = MutableStateFlow<Int?>(null)
    //  val weightGain: StateFlow<Int?> = _weightGain




    fun insertBodySize(height: Float, weight: Float, weightBefore: Float) {
        viewModelScope.launch {
            _feedModel.value = FeedModelState(loading = true)
            try {
                Log.d("viewModelToday", "insertBodySize")
                val bmi = calculateBMI(height, weight)
                val weightGainNew = weight - weightBefore
                val currentWeek = week.value

                Log.d("viewModelToday", "insertBodySize$currentWeek")
                Log.d("viewModelToday", "insertBodySize ${_week.value}")
                currentWeek?.let {
                    val bodySize = BodySizeEntity(
                        id = currentWeek,
                        weight = weight,
                        height = height,
                        bmi = bmi,
                        weightSizeBefore = weightBefore,
                        weightGain = weightGainNew
                    )
                    Log.d("viewModelToday", "$currentWeek")
                    repository.insertElementaryBodySize(bodySize)
                }
                _record.value = true
                _feedModel.value = FeedModelState()
            } catch (e: Exception) {
                _feedModel.value = FeedModelState(error = true)
            }

        }
    }

    fun addWeightAndWeightGain(newWeight: Float) {
        viewModelScope.launch {
            _feedModel.value = FeedModelState(loading = true)
            try {
                val firstBody = repository.getFirstBodySize()//bodySizeDao.getAll()
                val tt = elementaryBodySizeState.value
                if (firstBody?.weightSizeBefore != null) {
                    val weight = firstBody.weightSizeBefore
                    val weightGainNew = newWeight - weight
                    val currentWeek = week.value
                    currentWeek?.let {
                        repository.insertNewBodySize(
                            BodySizeEntity(
                                id = currentWeek.toInt(),
                                weight = newWeight,
                                weightGain = weightGainNew
                            )
                        )
                    }
                    _record.value = true
                    _feedModel.value = FeedModelState()
                } else {
                    _feedModel.value = FeedModelState(error = true)
                }
                _feedModel.value = FeedModelState()
            } catch (e: Exception) {
                _feedModel.value = FeedModelState(error = true)
            }


        }
    }

    fun getBodySizeByWeek(week: Int): Flow<BodySizeEntity?> {
        return repository.getBodySizeByWeek(week)

    }

    private fun calculateBMI(height: Float, weight: Float): Float {
        val heightInMeters = height / 100.0
        return (weight / (heightInMeters * heightInMeters)).toFloat()
    }

    fun updateWeek(value: Int) {
        Log.d("viewModelToday", "insertBodySize ${week.value}")
        viewModelScope.launch {
            _week.value = value
        }
        Log.d("viewModelToday", "updateWeek _week.value updated: ${_week.value}")
        Log.d("viewModelToday", "insertBodySize ${week.value}")
    }

    fun getBmiCategory(): String {
        val bmi = elementaryBodySizeState.value?.bmi
        return when {
            bmi == null -> " не рассчитан"
            bmi < 19 -> "Дефицит массы"
            bmi in 19.0..24.9 -> "Норма"
            bmi >= 25 -> "Избыточная масса тела"
            else -> "Неизвестная категория"
        }
    }

    // Метод для получения нужного значения из Triple в зависимости от ИМТ
    fun getWeightGainForBmi(
        bmi: Float?,
        week: Int,
        weightGainByWeek: Map<Int, Triple<Double, Double, Double>>
    ): Double? {
        val weekData = weightGainByWeek[week]
        return when {
            bmi == null -> null
            bmi < 19 -> weekData?.first  // Дефицит массы
            bmi in 19.0..24.9 -> weekData?.second  // Норма
            bmi >= 25 -> weekData?.third  // Избыточная масса тела
            else -> null


        }
    }
    fun recordForThisWeek(week: Int) {
        viewModelScope.launch {
            val response = repository.recordForThisWeek(week) >0
            _record.value = response

        }

    }
}
