package com.nickmorus.pregnancyapp.screens.draw

import androidx.lifecycle.ViewModel
import com.nickmorus.pregnancyapp.data.entities.Document
import com.nickmorus.pregnancyapp.model.FeedModelState
import com.nickmorus.pregnancyapp.retrofit.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class DrawViewModel @Inject constructor(
    private val apiService : ApiService
): ViewModel() {
    private val _feedModel = MutableStateFlow(FeedModelState())
    val feedModel : StateFlow<FeedModelState> = _feedModel
   // val data : Flow<List<Document>> =
   val data: Flow<List<Document>> = flow {
       try {
           _feedModel.value = FeedModelState(loading = true)
           val response = apiService.getDocuments()
           if (response.isSuccessful) {
               _feedModel.value = FeedModelState()
               emit(response.body() ?: emptyList())
           } else {
               _feedModel.value = FeedModelState(error = true)
               throw Exception("Неуспешный запрос в getTopics")
           }
       } catch (e: Exception) {
           emit(emptyList())  // Возвращаем пустой список в случае ошибки
           throw Exception("Ошибка в getTopics: ${e.message}")
       }
   }

}
