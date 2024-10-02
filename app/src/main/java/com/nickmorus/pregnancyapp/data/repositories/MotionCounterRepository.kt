package com.nickmorus.pregnancyapp.data.repositories

import com.nickmorus.pregnancyapp.data.dao.MotionCounterDao
import com.nickmorus.pregnancyapp.data.entities.MotionRecordEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MotionCounterRepository @Inject constructor(
    private val dao : MotionCounterDao
) {
    val getAll: Flow<List<MotionRecordEntity>> = dao.getAllMotionRecord()
    suspend fun insertMotion(localDate: LocalDate, localTime: LocalTime){
        val existingRecord = dao.getMotionRecordById(localDate)
        val addTime = if(existingRecord != null){
            existingRecord.movementTimes + localTime
        } else{
            listOf(localTime)
        }

        val record = MotionRecordEntity(
            id = localDate,
                movementTimes = addTime
        )
        dao.insertMotionRecordDate(record)
    }
    suspend fun deleteLastMovement(localDate: LocalDate):Boolean {
        try {
            val currentRecord = dao.getMotionRecordById(localDate)?.movementTimes ?: emptyList()
            return if (currentRecord.isNotEmpty()) {
                val updatedTimes = currentRecord.dropLast(1)

                // Обновите запись в базе данных
                dao.updateMotionRecord(localDate, updatedTimes)
                true
            } else {
                false
            }
        }catch (e:Exception){
            return false
        }
    }

    suspend fun getMovementCountForToday(currentDate:LocalDate): Int {
        val currentRecord = dao.getMotionRecordById(currentDate)
        return currentRecord?.movementTimes?.size ?: 0
    }


}
