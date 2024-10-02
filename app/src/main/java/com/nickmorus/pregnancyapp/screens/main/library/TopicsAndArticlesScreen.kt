package com.nickmorus.pregnancyapp.screens.main.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.nickmorus.pregnancyapp.data.entities.ArticleMeta
import com.nickmorus.pregnancyapp.data.entities.Topic
import com.nickmorus.pregnancyapp.screens.main.library.articles.ArticleItem
import com.nickmorus.pregnancyapp.screens.main.library.articles.ArticlesViewModel
import com.nickmorus.pregnancyapp.screens.main.library.topics.LibraryTopicItem
import com.nickmorus.pregnancyapp.screens.main.library.topics.TopicsViewModel
import com.nickmorus.pregnancyapp.ui.theme.PaddingMin
import java.util.UUID


@Composable
fun TopicsAndArticlesScreen(
    topicsViewModel: TopicsViewModel = hiltViewModel(),
    articlesViewModel: ArticlesViewModel = hiltViewModel(),
    onTopicClicked: (Topic) -> Unit = {},
    onArticleClicked: (ArticleMeta) -> Unit
) {
    val topics by topicsViewModel.data.collectAsState(initial = emptyList())
    val articleMeta by articlesViewModel.articleMetas.collectAsState()
    val selectedTopicId = remember { mutableStateOf<UUID?>(null) }
   // val scrollState = rememberScrollState()
    val filteredTopics = topics.filter { it.title != "ПО НЕДЕЛЯМ" }
    LaunchedEffect(filteredTopics) {
        if (filteredTopics.isNotEmpty()) {
            val firstTopicId = filteredTopics.first().id
            articlesViewModel.updateTopicId(firstTopicId)
            selectedTopicId.value = firstTopicId
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)

    ) {
        // Articles section
        if (articleMeta.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(PaddingMin)
                    .weight(1f)
                    .background(Color.Transparent),
                verticalArrangement = Arrangement.Top
            ) {

                items(articleMeta) {
                    ArticleItem(
                        title = it.title,
                        imageUrl = it.image


                    ) {
                        // Обработка клика по статье
                        onArticleClicked(it)
                    }

                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator() // Индикатор загрузки тем
            }

        }

        // Topics section at the bottom

        Box(
            modifier = Modifier.fillMaxWidth()
                ,
            contentAlignment = Alignment.BottomStart
        ) {
            if (filteredTopics.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        //.horizontalScroll(scrollState)
                ) {
                    items(filteredTopics) {
                        LibraryTopicItem(
                            title = it.title,
                            isSelected = selectedTopicId.value == it.id
                        ) {
                            articlesViewModel.updateTopicId(it.id)
                            selectedTopicId.value = it.id
                            // При клике на тему, загрузить статьи для этой темы
                            onTopicClicked(it)


                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    TopicsAndArticlesScreen {}
}
