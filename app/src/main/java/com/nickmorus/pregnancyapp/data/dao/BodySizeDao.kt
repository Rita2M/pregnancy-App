package com.nickmorus.pregnancyapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nickmorus.pregnancyapp.data.entities.BodySizeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BodySizeDao {
    @Query("SELECT * FROM BodySizeEntity WHERE id = :id ")
  fun getBodySizeById(id: Int ):Flow<BodySizeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBodySize(bodySize: BodySizeEntity)

    @Query("SELECT * FROM BodySizeEntity WHERE id = :id ")
 fun getBody(id: Int ): Flow<BodySizeEntity?>
    @Query("SELECT * FROM BodySizeEntity")
     fun getAllBodySize(): Flow<List<BodySizeEntity?>>
    @Query("SELECT * FROM BodySizeEntity")
    suspend fun getAll(): List<BodySizeEntity>
    @Query("SELECT * FROM BodySizeEntity ORDER BY id ASC LIMIT 1")
    suspend fun getFirstBodySize(): BodySizeEntity?
    @Query("SELECT COUNT(*) FROM BodySizeEntity WHERE id = :id")
    suspend fun recordForThisWeek(id: Int): Int


}
