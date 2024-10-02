package com.nickmorus.pregnancyapp.screens.main.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nickmorus.pregnancyapp.data.dao.NoteDao
import com.nickmorus.pregnancyapp.data.entities.NotesEntity
import com.nickmorus.pregnancyapp.model.FeedModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
   private val noteDao: NoteDao
):ViewModel() {
    private val _feedModel = MutableStateFlow(FeedModelState())
    val feedModel : StateFlow<FeedModelState> = _feedModel
    val dataNotes = noteDao.getAllNotes()
    fun saveNote(note:NotesEntity){
        viewModelScope.launch {
            _feedModel.value = FeedModelState(loading = true)
            try {
                noteDao.insertNote(note)
                _feedModel.value = FeedModelState()
            }catch (e:Exception){
                _feedModel.value = FeedModelState(error = true)
            }
        }

    }
    fun deleteNote(note:NotesEntity){
        viewModelScope.launch {
            try {
                noteDao.deleteNote(note.noteDate)
            }catch (e:Exception){
                _feedModel.value = FeedModelState(error = true)
            }
        }
    }
}
