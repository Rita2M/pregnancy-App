package com.nickmorus.pregnancyapp.data.repositories

import com.nickmorus.pregnancyapp.data.dao.BodySizeDao
import com.nickmorus.pregnancyapp.data.dao.PregnancyWeekDataDao
import com.nickmorus.pregnancyapp.data.entities.BodySizeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodayRepository @Inject constructor(
     private val bodySizeDao: BodySizeDao,
    pregnancyWeekDataDao: PregnancyWeekDataDao
) {

    val bodySize: Flow<List<BodySizeEntity?>> = bodySizeDao.getAllBodySize()

    suspend fun insertElementaryBodySize(bodySize: BodySizeEntity){
        bodySizeDao.insertBodySize(bodySize)
    }
    suspend fun insertNewBodySize(bodySize: BodySizeEntity){
        bodySizeDao.insertBodySize(bodySize)
    }
    suspend fun getAllBodySize() : List<BodySizeEntity>{
      return  bodySizeDao.getAll()

    }
    suspend fun getFirstBodySize():BodySizeEntity?{
       return bodySizeDao.getFirstBodySize()
    }
     fun getBodySizeByWeek(week:Int) :Flow<BodySizeEntity?>{
        return bodySizeDao.getBodySizeById(week)
    }
   suspend fun recordForThisWeek(week: Int):Int{
       return bodySizeDao.recordForThisWeek(week)
    }
}
