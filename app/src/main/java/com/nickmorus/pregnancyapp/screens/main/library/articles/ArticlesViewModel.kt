package com.nickmorus.pregnancyapp.screens.main.library.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nickmorus.pregnancyapp.data.entities.ArticleMeta
import com.nickmorus.pregnancyapp.data.repositories.ArticlesMetaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val articlesRepository: ArticlesMetaRepository
) : ViewModel() {

    private val _topicId: MutableStateFlow<UUID?> = MutableStateFlow(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val articleMetas: StateFlow<List<ArticleMeta>> = _topicId
        .filterNotNull()
        .flatMapLatest { topicId ->
            articlesRepository.getArticlesMetaById(topicId)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )


    // Метод для обновления topicId
    fun updateTopicId(newTopicId: UUID) {
        _topicId.value = newTopicId
    }

}

//    init {
//        articlesRepository.getArticlesMeta{
//            articleMetas = it
//        }
//    }

//    companion object{
//        private const val TOPIC_ID_SAVED_STATE_KEY: String = "topicId"
//    }
//}
