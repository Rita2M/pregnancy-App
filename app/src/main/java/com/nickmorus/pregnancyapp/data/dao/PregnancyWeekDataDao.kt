package com.nickmorus.pregnancyapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.nickmorus.pregnancyapp.data.entities.PregnancyWeekDataEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PregnancyWeekDataDao {
    @Query("SELECT * FROM PregnancyData where week = :week")
    fun getDataForWeek(week: Int): Flow<PregnancyWeekDataEntity?>

}
