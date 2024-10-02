package com.nickmorus.pregnancyapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nickmorus.pregnancyapp.data.entities.NotesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NotesEntity)
    @Query("SELECT * FROM NotesEntity")
    fun getAllNotes():Flow<List<NotesEntity>>
    @Query("SELECT * FROM NotesEntity WHERE noteDate = :noteDate ")
    suspend fun getNoteByDate(noteDate: String): NotesEntity?
    @Query("DELETE FROM NotesEntity WHERE noteDate = :noteDate")
    suspend fun deleteNote(noteDate: String)

}
