package com.nickmorus.pregnancyapp.data.repositories

import com.nickmorus.pregnancyapp.data.dao.EmojiDayDao
import com.nickmorus.pregnancyapp.data.entities.EmojiDayEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmojiRepository @Inject constructor(
    private  val dao: EmojiDayDao
) {
    val dataEmoji = dao.getAllCycles()
    suspend fun saveEmojiDay(emojiDayEntity: EmojiDayEntity ){
        dao.insertEmojiDay(emojiDayEntity)
    }
}
