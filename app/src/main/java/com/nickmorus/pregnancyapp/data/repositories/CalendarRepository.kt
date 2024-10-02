package com.nickmorus.pregnancyapp.data.repositories

import com.nickmorus.pregnancyapp.data.dao.CycleDao
import com.nickmorus.pregnancyapp.data.entities.CycleEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarRepository @Inject constructor(
    private val dao: CycleDao,

) {
    val data : Flow<List<CycleEntity>> = dao.getAllCycles()
    suspend fun insertCycle(cycle: CycleEntity){
       dao.insertCycle(cycle)
    }
    suspend fun delete(id: Int){
        dao.deleteCycle(id)
    }
    suspend fun getCycleForDate(selectedData:String): CycleEntity?{
        return dao.getCycleForDate(selectedData)

    }



}
