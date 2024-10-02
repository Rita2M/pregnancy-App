package com.nickmorus.pregnancyapp.screens.article

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nickmorus.pregnancyapp.data.entities.ArticleContent
import com.nickmorus.pregnancyapp.data.repositories.ArticleContentRepository
import com.nickmorus.pregnancyapp.model.FeedModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
   private val articlesRepository: ArticleContentRepository
) : ViewModel() {
    private val _content = MutableStateFlow<ArticleContent?>(null)
    val content: StateFlow<ArticleContent?> = _content
private val _state = mutableStateOf(FeedModelState())
    val state: State<FeedModelState> = _state
    init {
        val contentId: String? = savedStateHandle["contentId"]
        contentId?.let {
            loadArticleContent(UUID.fromString(it))
        }
    }

   fun loadArticleContent(articleId:UUID){
        viewModelScope.launch {
            try{
                val content = articlesRepository.getContent(articleId)
                _content.value = content
                _state.value = FeedModelState()

            }catch (e:Exception){
                _state.value = FeedModelState(error = true)

            }
        }
    }




}
