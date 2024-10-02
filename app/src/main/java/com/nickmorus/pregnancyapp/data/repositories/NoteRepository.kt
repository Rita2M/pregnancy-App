package com.nickmorus.pregnancyapp.data.repositories

import com.nickmorus.pregnancyapp.data.dao.NoteDao
import com.nickmorus.pregnancyapp.data.entities.NotesEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val dao: NoteDao
) {
    val data = dao.getAllNotes()
    suspend fun saveNote(notesEntity: NotesEntity){
        dao.insertNote(notesEntity)
    }
    suspend fun deleteNote(notesEntity: NotesEntity){
        dao.deleteNote(notesEntity.noteDate)
    }
    suspend fun getNoteById(selectDate:String):NotesEntity?{
       return dao.getNoteByDate(selectDate)
    }
}
