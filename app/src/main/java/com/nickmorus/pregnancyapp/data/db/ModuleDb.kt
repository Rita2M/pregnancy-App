package com.nickmorus.pregnancyapp.data.db

import android.content.Context
import androidx.room.Room
import com.nickmorus.pregnancyapp.data.dao.BodySizeDao
import com.nickmorus.pregnancyapp.data.dao.CycleDao
import com.nickmorus.pregnancyapp.data.dao.EmojiDayDao
import com.nickmorus.pregnancyapp.data.dao.MotionCounterDao
import com.nickmorus.pregnancyapp.data.dao.NoteDao
import com.nickmorus.pregnancyapp.data.dao.PregnancyWeekDataDao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ModuleDb {
    @Singleton
    @Provides
    fun providesDb(
        @ApplicationContext
        context: Context
    ): AppDb {
        DatabaseHelper.copyDatabaseFromAssets(context)
       return Room.databaseBuilder(context, AppDb::class.java, "app.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    @Provides
    fun providesCycleDao(
        appDb: AppDb
    ): CycleDao = appDb.cycleDao()
    @Provides
    fun providesMotionCounterDao(
        appDb: AppDb
    ): MotionCounterDao =appDb.motionCounterDao()
    @Provides
    fun providesPregnancyWeekDataDao(
        appDb: AppDb
    ): PregnancyWeekDataDao = appDb.pregnancyWeekDataDao()

    @Provides
    fun provideBodySizeDao(
        appDb: AppDb
    ): BodySizeDao = appDb.bodySizeDao()

    @Provides
    fun providesNoteDao(
        appDb: AppDb
    ): NoteDao = appDb.noteDao()
    @Provides
    fun providesEmojiDayDao(
        appDb: AppDb
    ): EmojiDayDao = appDb.emojiDayDao()



}
