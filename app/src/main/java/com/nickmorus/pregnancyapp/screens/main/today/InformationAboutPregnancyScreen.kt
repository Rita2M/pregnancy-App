package com.nickmorus.pregnancyapp.screens.main.today

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nickmorus.pregnancyapp.AppViewModel
import com.nickmorus.pregnancyapp.data.entities.ArticleMeta
import com.nickmorus.pregnancyapp.screens.main.library.articles.ArticlesViewModel
import com.nickmorus.pregnancyapp.screens.main.library.topics.TopicsViewModel
import com.nickmorus.pregnancyapp.ui.theme.PaddingMedium
import com.nickmorus.pregnancyapp.ui.theme.Purple40
import com.nickmorus.pregnancyapp.ui.theme.Purple80
import com.nickmorus.pregnancyapp.utils.information.weightGainByWeek
import com.nickmorus.pregnancyapp.utils.textDays
import com.nickmorus.pregnancyapp.utils.textWeeks
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.UUID

@Composable
fun InformationAboutPregnancyScreen(

    today: LocalDate,
    onClickBodySize: () -> Unit,
    viewModelApp: AppViewModel = hiltViewModel(),
    viewModelToday: TodayViewModel,
    topicsViewModel: TopicsViewModel = hiltViewModel(),
    articlesViewModel: ArticlesViewModel = hiltViewModel(),
    onArticleContentByWeekClick: (ArticleMeta) -> Unit
) {
    val firstDay = viewModelApp.firstDay.collectAsState()
    if (firstDay.value.isNullOrEmpty()) {
        CircularProgressIndicator()
    } else {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val firstDayLocalData = LocalDate.parse(firstDay.value, dateFormatter)
        val currentWeek = remember(firstDay, today) {
            getCurrentPregnancyWeek(firstDayLocalData, today)
        }
        val weeksPassed = remember {
            ChronoUnit.WEEKS.between(firstDayLocalData, today).toInt()
        }
        val dayPassed =
            ChronoUnit.DAYS.between(firstDayLocalData.plusWeeks(weeksPassed.toLong()), today)
                .toInt()

        val context = LocalContext.current
        LaunchedEffect(weeksPassed) {
            viewModelToday.updateWeek(weeksPassed)
            Log.d("InformAboutScreen", "updateWeekPassed$weeksPassed")


        }
        val topics by topicsViewModel.data.collectAsState(initial = emptyList())
        val articleMeta by articlesViewModel.articleMetas.collectAsState()
        val selectedTopicId = remember { mutableStateOf<UUID?>(null) }
        LaunchedEffect(topics) {
            if (topics.isNotEmpty()) {
                val firstTopicId = topics.first().id
                articlesViewModel.updateTopicId(firstTopicId)
                selectedTopicId.value = firstTopicId
            }
        }


        // Прогресс в процентах
        val progress = (weeksPassed.toFloat() / 41).coerceIn(0f, 1f)

        val dueDate = firstDayLocalData.plusWeeks(40) // Предполагаемая дата родов через 40 недель
        val daysUntilDueDate = ChronoUnit.DAYS.between(today, dueDate)
            .toInt() // Количество дней до предполагаемой даты родов

        val weekData =
            viewModelToday.weekData.collectAsState(null) //данные по неделям средние из базы данных

        val imageResId = getWeekImageResId(context, weeksPassed)
        val state = viewModelToday.feedModel.collectAsState()

        val weekRegex = Regex("^\\d{1,2}")
        val articleForWeek = articleMeta.firstOrNull { article ->
        val matchResult = weekRegex.find(article.title)
        matchResult?.value?.toIntOrNull() == weeksPassed
    }
    when {
        state.value.error -> Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_SHORT).show()
        state.value.loading -> CircularProgressIndicator(Modifier.fillMaxSize())

    }
        val data = viewModelToday.bodySize.collectAsState(initial = null)
        //  val weightGain = viewModel.weightGain.collectAsState()
        val firstBodySize = viewModelToday.elementaryBodySizeState.collectAsState()
        val weightGainAverage = viewModelToday.getWeightGainForBmi(
            firstBodySize.value?.bmi, weeksPassed,
            weightGainByWeek
        )
        val currentWeekBodySize =
            viewModelToday.getBodySizeByWeek(weeksPassed).collectAsState(initial = null)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingMedium)
                .verticalScroll(rememberScrollState())

        ) {
            // Верхний блок с информацией о неделе
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, color = Color.Gray, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Purple80)
                        .padding(16.dp)
                        .clickable { articleForWeek?.let { onArticleContentByWeekClick(articleForWeek) }  }

                ) {

                    Text(
                        text = "Текущая неделя беременности",
                        fontWeight = FontWeight.Bold,

                        )
                    Text(
                        text = " $currentWeek",

                        )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(8.dp)

                ) {

                    Text(text = "Мой срок:", fontWeight = FontWeight.Bold)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (dayPassed != 0) {
                            Text(
                                text = "${weeksPassed}${textWeeks(weeksPassed)} и $dayPassed ${
                                    textDays(
                                        dayPassed
                                    )
                                }", color = Color.LightGray, fontSize = 12.sp,

                            )

                        } else {
                            Text(
                                text = "${weeksPassed}${textWeeks(weeksPassed)}",
                                color = Color.LightGray,
                                fontSize = 12.sp,
                            )
                        }
                        Spacer(modifier = Modifier.weight(0.5f))

                        Text(
                            text = getPregnancyDetails(firstDayLocalData, today),
                            color = Color.LightGray,
                            fontSize = 12.sp,

                            )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .background(Color.LightGray) // Фон линии
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progress)
                                .background(Purple40) // Цвет прогресса
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(text = "Предполагаемая дата родов:", fontWeight = FontWeight.Bold)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                        ,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "ещё $daysUntilDueDate ${textDays(daysUntilDueDate)}",
                            color = Color.LightGray,

                            )
                        Text(
                            text = "$dueDate",
                            color = Color.LightGray
                        )
                    }
                    Text(text = "Мои показатели:",
                        fontWeight = FontWeight.Bold)
                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "ЧСС: ${weekData.value?.motherHR}", color = Color.LightGray)
                        Text(
                            text = "Критическое значение: ${weekData.value?.criticalValue}",
                            color = Color.LightGray
                        )
                    }
                    Log.d("InformScreen", "${data.value}")
                    if (data.value.isNullOrEmpty()) {
                        Button(
                            onClick = { onClickBodySize() }, colors = ButtonDefaults.buttonColors(
                                Purple40
                            )
                        ) {
                            Text(text = "Укажите рост и вес для расчета", color = Color.White)

                        }

                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "ИМТ: ${viewModelToday.getBmiCategory()}",
                                color = Color.LightGray
                            )
                            Text(
                                text = "Средняя прибавка (на текущий период): $weightGainAverage кг.",
                                color = Color.LightGray
                            )

                            // Проверяем наличие данных за текущую неделю
                            if (currentWeekBodySize.value == null) {
                                Text(text = "Фактическая прибавка:",color = Color.LightGray)
                                // Если данных нет, отображаем кнопку для добавления
                                Button(onClick = { onClickBodySize() }) {
                                    Text(text = "Добавить вес за эту неделю", color = Color.White)
                                }
                            } else {
                                // Если данные есть, отображаем информацию о прибавке веса
                                val weightGain = currentWeekBodySize.value?.weightGain
                                weightGain?.let {
                                    BodyWeightBox(weightGain, onClickBodySize)
                                }
                            }
                        }
                    }
                    when {
                        data.value.isNullOrEmpty() ->
                            currentWeekBodySize.value == null
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))

            // Блок с информацией о малыше
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, color = Color.Gray, RoundedCornerShape(16.dp))
                    .padding(16.dp)
                ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Мой малыш как:", fontWeight = FontWeight.Bold)
                    Text(
                        text = weekData.value?.description ?: "Информация отсутствует",
                        fontWeight = FontWeight.Bold
                    )
                }
                if (imageResId != 0) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = "Картинка плода",
                        modifier = Modifier
                            .size(64.dp) // Размер картинки
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape)
                            .align(Alignment.CenterVertically) // Выравнивание по центру по вертикали
                    )
                }
            }
//            ArticleMetaByWeek(week = weeksPassed, articleMeta = articleMeta,
//                onArticleClicked = onArticleClicked)

        }
    }
}

fun getCurrentPregnancyWeek(firstDay: LocalDate, today: LocalDate): String {

    // Рассчитываем количество недель, прошедших с первого дня последних месячных
    val weeksPassed = ChronoUnit.WEEKS.between(firstDay, today)

    // Определяем начало и конец текущей недели
    val startOfWeek = firstDay.plusWeeks(weeksPassed)
    val endOfWeek = startOfWeek.plusDays(6)

    val formatter = DateTimeFormatter.ofPattern("EEE, dd MMMM")

    return "${startOfWeek.format(formatter)} - ${endOfWeek.format(formatter)}"
}


fun getPregnancyDetails(firstDay: LocalDate, today: LocalDate): String {
    // Рассчитываем количество дней беременности
    val daysPregnant = ChronoUnit.DAYS.between(firstDay, today).toInt()

    // Определяем триместр на основе количества дней
    val trimester = when {
        daysPregnant in 1..91 -> "1-й триместр"
        daysPregnant in 92..189 -> "2-й триместр"
        daysPregnant >= 190 -> "3-й триместр"
        else -> "Ошибка расчёта"
    }

    return "$daysPregnant ${textDays(daysPregnant)}, $trimester"
}

@SuppressLint("DiscouragedApi")
@Composable
fun getWeekImageResId(context: Context, week: Int): Int {
    val imageName = "week_$week"
    return context.resources.getIdentifier(imageName, "drawable", context.packageName)
}

@Composable
fun BodyWeightBox(
    weightGain: Float,
    onClickBodySize: () -> Unit
) {
    Column {

        Text(text = "Фактическая прибавка : $weightGain кг.",
            color = Color.LightGray)

        Button(
            onClick = { onClickBodySize() }, colors = ButtonDefaults.buttonColors(
                Purple40
            )
        ) {
            Text(text = "График прибавки веса", color = Color.White)

        }
    }
}
//@Composable
//fun ArticleMetaByWeek(
//    week: Int,
//    articleMeta: List<ArticleMeta>,
//    onArticleClicked: (ArticleMeta) -> Unit
//
//){
//    val weekRegex = Regex("^\\d{1,2}")
//    val articleForWeek = articleMeta.firstOrNull { article ->
//
//        val matchResult = weekRegex.find(article.title)
//
//        matchResult?.value?.toIntOrNull() == week
//    }
//    articleForWeek?.let { article ->
//        ArticleItem(
//            title = article.title,
//            imageUrl = article.image
//        ) {onArticleClicked(article)
//        }
//    } ?: run {
//        Text("Статья для $week недели не найдена")
//    }
//
//
//}
