package com.nickmorus.pregnancyapp.screens.main.library.topics

import android.app.Application
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nickmorus.pregnancyapp.PregnancyApp
import com.nickmorus.pregnancyapp.data.repositories.TopicsRepository
import com.nickmorus.pregnancyapp.data.entities.Topic
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TopicsViewModel @Inject constructor(
     private val topicsRepository: TopicsRepository
) : ViewModel() {
    var data: Flow<List<Topic>> = topicsRepository.data


    //var topics: List<Topic>? = null


}
