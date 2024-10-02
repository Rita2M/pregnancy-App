package com.nickmorus.pregnancyapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nickmorus.pregnancyapp.converters.LocalDateTimeListConverter
import com.nickmorus.pregnancyapp.data.dao.BodySizeDao
import com.nickmorus.pregnancyapp.data.dao.CycleDao
import com.nickmorus.pregnancyapp.data.dao.EmojiDayDao
import com.nickmorus.pregnancyapp.data.dao.MotionCounterDao
import com.nickmorus.pregnancyapp.data.dao.NoteDao
import com.nickmorus.pregnancyapp.data.dao.PregnancyWeekDataDao
import com.nickmorus.pregnancyapp.data.entities.BodySizeEntity
import com.nickmorus.pregnancyapp.data.entities.CycleEntity
import com.nickmorus.pregnancyapp.data.entities.EmojiDayEntity
import com.nickmorus.pregnancyapp.data.entities.MotionRecordEntity
import com.nickmorus.pregnancyapp.data.entities.NotesEntity
import com.nickmorus.pregnancyapp.data.entities.PregnancyWeekDataEntity

@Database(entities = [CycleEntity::class, MotionRecordEntity::class, PregnancyWeekDataEntity::class, BodySizeEntity::class, NotesEntity::class, EmojiDayEntity::class ], version = 13, exportSchema = false)
@TypeConverters(LocalDateTimeListConverter::class)
abstract class AppDb : RoomDatabase(){
    abstract fun cycleDao(): CycleDao
    abstract fun motionCounterDao(): MotionCounterDao
    abstract fun pregnancyWeekDataDao(): PregnancyWeekDataDao
    abstract fun bodySizeDao(): BodySizeDao
    abstract fun noteDao(): NoteDao
    abstract fun  emojiDayDao(): EmojiDayDao

}
