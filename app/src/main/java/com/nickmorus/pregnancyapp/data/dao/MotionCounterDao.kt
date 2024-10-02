package com.nickmorus.pregnancyapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nickmorus.pregnancyapp.data.entities.MotionRecordEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime

@Dao
interface MotionCounterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMotionRecordDate(record: MotionRecordEntity)
    @Query("SELECT * FROM MotionRecordEntity")
    fun getAllMotionRecord(): Flow<List<MotionRecordEntity>>
    @Query("SELECT * FROM MotionRecordEntity WHERE id = :date")
    suspend fun getMotionRecordById(date: LocalDate): MotionRecordEntity?
    @Query("UPDATE MotionRecordEntity SET movementTimes = :time WHERE id = :date")
    suspend fun updateMotionRecord(date: LocalDate,time: List<LocalTime>)
    @Query("SELECT COUNT(*) FROM MotionRecordEntity WHERE id = :date")
    suspend fun getMovementCountByDate(date: LocalDate): Int

}
