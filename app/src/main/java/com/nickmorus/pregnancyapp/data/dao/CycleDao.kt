package com.nickmorus.pregnancyapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nickmorus.pregnancyapp.data.entities.CycleEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface CycleDao {
    @Query("SELECT * FROM CycleEntity")
    fun getAllCycles(): Flow<List<CycleEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCycle(cycle: CycleEntity)
    @Query("DELETE FROM CycleEntity WHERE id = :id")
    suspend fun deleteCycle(id: Int)
    @Query("""
        SELECT * FROM CycleEntity 
        WHERE startDate <= :selectedDate 
        AND (endDate IS NULL OR endDate >= :selectedDate) 
        LIMIT 1
    """)
    suspend fun getCycleForDate(selectedDate: String): CycleEntity?
}
