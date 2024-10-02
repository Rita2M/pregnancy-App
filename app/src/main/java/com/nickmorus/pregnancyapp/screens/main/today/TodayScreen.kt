package com.nickmorus.pregnancyapp.screens.main.today

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nickmorus.pregnancyapp.AppViewModel
import com.nickmorus.pregnancyapp.MainScreen
import com.nickmorus.pregnancyapp.data.entities.ArticleMeta
import com.nickmorus.pregnancyapp.data.entities.CycleEntity
import com.nickmorus.pregnancyapp.screens.ad.AdBanner
import com.nickmorus.pregnancyapp.screens.main.calendar.CalendarViewModel
import com.nickmorus.pregnancyapp.screens.main.library.articles.ArticleItem
import com.nickmorus.pregnancyapp.screens.main.library.articles.ArticlesViewModel
import com.nickmorus.pregnancyapp.screens.main.library.topics.TopicsViewModel
import com.nickmorus.pregnancyapp.ui.theme.PaddingMin
import com.nickmorus.pregnancyapp.utils.textDays
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID


@Composable
fun TodayScreen(
    modifier: Modifier = Modifier, navController: NavHostController,
    onClickBodyWeight: () -> Unit,
    viewModel: CalendarViewModel = hiltViewModel(),
    viewModelApp: AppViewModel = hiltViewModel(),
    viewModelToday: TodayViewModel,
    topicsViewModel: TopicsViewModel = hiltViewModel(),
    articlesViewModel: ArticlesViewModel = hiltViewModel(),
    onArticleClicked: (ArticleMeta) -> Unit


) {
    val isPregnant = viewModelApp.isPregnant.collectAsState()
    val firstDay = viewModelApp.firstDay.collectAsState()
    val openDialog = remember { mutableStateOf(false) }

    // val snackScope = rememberCoroutineScope()
    val todayData = LocalDate.now()


    Log.d("TodayScr", "${firstDay.value}")
    Log.d("TodayScr", "${isPregnant.value}")
    val cycles = viewModel.data.collectAsState()

    val topics by topicsViewModel.data.collectAsState(initial = emptyList())
    val articleMeta by articlesViewModel.articleMetas.collectAsState()
    val selectedTopicId = remember { mutableStateOf<UUID?>(null) }
    LaunchedEffect(topics) {
        if (topics.isNotEmpty()) {
            val secondTopicId = topics[1].id
            articlesViewModel.updateTopicId(secondTopicId)
            selectedTopicId.value = secondTopicId
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            // stateToday.value.loading || stateCalendar.value.loading -> LoadingScreen()
            isPregnant.value -> InformationAboutPregnancyScreen(
                today = todayData,
                onClickBodySize = onClickBodyWeight,
                viewModelToday = viewModelToday,
                onArticleContentByWeekClick = onArticleClicked
            )

            else -> QuestionAboutPregnancyScreen(
                navController = navController,
                onClickedAlert = { openDialog.value = true },
                cycles = cycles.value,
                articlesMeta = articleMeta,
                onArticleClicked = onArticleClicked

            )


        }
    }
    if (openDialog.value) {
        ShowDatePickerDialog(openDialog = openDialog,
            onConfirm = { selectedDate, duration, langht ->
                viewModel.insertCycle(selectedDate, duration, langht, true)
                viewModelApp.setPregnancyState(true)
                viewModelApp.setFirstDay(selectedDate.toString())

            }
        )
    }


}


@Composable
fun QuestionAboutPregnancyScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onClickedAlert: () -> Unit,
    cycles: List<CycleEntity>,
    articlesMeta: List<ArticleMeta>,
    onArticleClicked: (ArticleMeta) -> Unit,
) {
    Column {


        Column(
            modifier = modifier
                .padding(16.dp)
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface) // фон
                .padding(16.dp)
                .clickable {
                    onClickedAlert()

                }

        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), text = "Ждете малыша?" +
                        "нажмите сюда и введите дату последней менструации"
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) // Цвет линии
        )
        UseCalendarBox(
            modifier = Modifier
                .padding(16.dp),
            onClicked = {
                navController.navigate(MainScreen.Calendar.route)

            }, cycles = cycles
        )
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) // Цвет линии
        )
        ArticleAndAdBanner(articlesMeta = articlesMeta, onArticleClicked = onArticleClicked)

    }

}

@Composable
fun ArticleAndAdBanner(
    articlesMeta: List<ArticleMeta>,
    onArticleClicked: (ArticleMeta) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        if (articlesMeta.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(PaddingMin)
                    .weight(1f)
                    .background(Color.Transparent),
                verticalArrangement = Arrangement.Top
            ) {

                itemsIndexed(articlesMeta) { index, article ->
                    ArticleItem(
                        title = article.title,
                        imageUrl = article.image
                    ) {
                        onArticleClicked(article)

                    }


                    if ((index + 1) % 3 == 0) {
                        AdBanner()
                    }
                }
            }
        } else {

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}


    @Composable
    fun UseCalendarBox(
        modifier: Modifier = Modifier,
        onClicked: () -> Unit,
        viewModel: CalendarViewModel = hiltViewModel(),
        cycles: List<CycleEntity>
    ) {


        Surface(
            modifier = modifier
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(16.dp))
        ) {
            Box(
                modifier.clickable { onClicked() }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (cycles.isNotEmpty()) {
                        Log.d("TodayScreenAllCycles", " $cycles")
                        // Получаем информацию о первом цикле
                        val latestCycle = cycles.maxBy { it.startDate }
                        val (daysUntilMenstruation, ovulationDate) = viewModel.calculateMenstruationInfo(
                            latestCycle
                        )

                        Log.d("TodayScreen", "$daysUntilMenstruation, $ovulationDate")
                        when (daysUntilMenstruation) {
                            1, 2 -> Text(
                                text = "Скоро должны начаться, осталось: $daysUntilMenstruation ${
                                    textDays(
                                        daysUntilMenstruation
                                    )
                                }"
                            )

                            in Int.MIN_VALUE..0 -> Text(text = "Должны были начаться: отметьте новый цикл")
                            else -> Text(text = "Дней до следующей менструации: $daysUntilMenstruation")
                        }
                        Text(text = "Дата овуляции: ${ovulationDate?.format(DateTimeFormatter.ISO_LOCAL_DATE)}")
                    } else {
                        // Если данных нет, показываем предложение добавить цикл
                        Text(text = "Используйте наш календарь для отслеживания циклов")
                    }
                }
            }
        }

    }

    @Composable
    fun ToArticlesBox(
        modifier: Modifier = Modifier,
        onClicked: () -> Unit
    ) {
        Surface(
            modifier = modifier
                .fillMaxHeight()
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
        ) {
            Box(
                modifier = modifier
                    .clickable {
                        onClicked()
                    }
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "В разделе \"Статьи\" Вы можете ознакомиться со всей информацией которая может быть Вам полезна при планировании беременности",
                    fontSize = 20.sp
                )
            }
        }
    }

    @Composable
    fun LoadingScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ShowDatePickerDialog(
        openDialog: MutableState<Boolean>,
        onConfirm: (LocalDate, Int, Int) -> Unit
    ) {
        if (openDialog.value) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { openDialog.value = false },
                confirmButton = {
                    TextButton(onClick = {

                        val selectedDate = datePickerState.selectedDateMillis?.let {
                            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                        }
                        if (selectedDate != null) {
                            onConfirm(
                                selectedDate,
                                4,
                                28
                            )  // Здесь можно передать duration.intValue
                        }
                        openDialog.value = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { openDialog.value = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
