package com.nickmorus.pregnancyapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nickmorus.pregnancyapp.data.entities.EmojiDayEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmojiDayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmojiDay(emojiDay: EmojiDayEntity)
    @Query("SELECT * FROM EmojiDayEntity")
    fun getAllCycles(): Flow<List<EmojiDayEntity>>
}
