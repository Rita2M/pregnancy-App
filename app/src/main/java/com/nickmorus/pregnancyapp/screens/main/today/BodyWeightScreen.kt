package com.nickmorus.pregnancyapp.screens.main.today

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.nickmorus.pregnancyapp.R
import com.nickmorus.pregnancyapp.data.entities.BodySizeEntity
import com.nickmorus.pregnancyapp.toasts.ToastNotInformation
import com.nickmorus.pregnancyapp.ui.theme.Lavender
import com.nickmorus.pregnancyapp.ui.theme.PaddingMedium
import com.nickmorus.pregnancyapp.ui.theme.Purple40
import com.nickmorus.pregnancyapp.utils.information.weightGainByWeek


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("DefaultLocale")
@Composable
fun BodyWeightScreen(
    viewModelToday: TodayViewModel,
    onBackPressed: () -> Unit

) {
    val data = viewModelToday.bodySize.collectAsState(initial = emptyList())
    val heightSize = remember { mutableStateOf("") }
    val weightSize = remember { mutableStateOf("") }
    val weightSizeBefore = remember { mutableStateOf("") }
    val state = viewModelToday.feedModel.collectAsState()
    val context = LocalContext.current
    val firsBodySize = viewModelToday.elementaryBodySizeState.collectAsState()
    val isButtonEnabled =
        heightSize.value.isNotBlank() && weightSize.value.isNotBlank() && weightSizeBefore.value.isNotBlank()
    val showAlertDialogAddBodySize = remember {
        mutableStateOf(false)
    }
    val bmiLong = firsBodySize.value?.bmi ?: 0.0
    val formattedBmi = String.format("%.1f", bmiLong)
    val todayWeek = viewModelToday.week.collectAsState()
    val week = todayWeek.value

    week?.let {
        LaunchedEffect(todayWeek) {
            viewModelToday.recordForThisWeek(week)
        }
    }
    val record = viewModelToday.record.collectAsState()


    if (showAlertDialogAddBodySize.value) {
        AlertDialogAddWeigh(onDismiss = { showAlertDialogAddBodySize.value = false },
            onConfirm = { weight ->
                viewModelToday.addWeightAndWeightGain(weight.toFloat())
                showAlertDialogAddBodySize.value = false

            })
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("График набора веса") },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(Icons.Default.Close, contentDescription = "Назад")
                    }
                }
            )
        },
        floatingActionButton = {
            if (data.value.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Отображаем соответствующий текст
                    if (record.value) {
                        Text(text =  " Вес за текущую неделю добавлен")
                    } else {
                        Text(text = "Добавьте вес за текущую неделю")
                    }

                    // Кнопка добавления веса, отключается если запись уже сделана
                    FloatingActionButton(
                        onClick = {
                            if (!record.value) {
                                showAlertDialogAddBodySize.value = true
                            }
                        },
                        modifier = Modifier
                            .padding(6.dp),
                        containerColor = if (record.value) Color.Gray else Purple40,

                        ) {
                        Icon(Icons.Default.Add, contentDescription = "Добавить параметры")
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(PaddingMedium)
        ) {
            when {
                state.value.error -> ToastNotInformation(
                    context = context,
                    message = "Произошла ошибка"
                )

                state.value.loading -> CircularProgressIndicator()
            }

            if (data.value.isEmpty()) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Lavender)
                        .border(1.dp, color = Color.Gray, RoundedCornerShape(16.dp))
                        .padding(8.dp)


                ) {
                    Text(text = "Введите рост:")

                    OutlinedTextField(
                        value = heightSize.value,
                        onValueChange = { heightSize.value = it },
                        label = { Text("Рост") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                            .padding(8.dp)

                    )
                    Text(text = "Введите вес до беременности (в кг)")
                    OutlinedTextField(
                        value = weightSizeBefore.value,
                        onValueChange = { weightSizeBefore.value = it },
                        label = { Text(text = "Вес (кг)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                            .padding(8.dp)
                    )
                    Text(text = "Введите текущий вес (в кг)")
                    OutlinedTextField(
                        value = weightSize.value,
                        onValueChange = { weightSize.value = it },
                        label = { Text(text = "Вес (кг)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                            .padding(8.dp)
                    )

                    Button(
                        onClick = {
                            viewModelToday.insertBodySize(
                                heightSize.value.toFloat(), // вносим и вес сейчас и вес до бер
                                weightSize.value.toFloat(),
                                weightSizeBefore.value.toFloat()

                            )
                            Log.d("bodyScreen"," {${heightSize.value},${weightSize.value.toFloat()},${weightSizeBefore.value.toFloat()}")
                        },
                        enabled = isButtonEnabled,
                        colors = ButtonDefaults.buttonColors(containerColor = if (isButtonEnabled) Purple40 else Color.Gray)
                    ) {
                        Text(text = "Добавить параметры")
                    }
                }
            } else {
                Text(text = "Рост: ${firsBodySize.value?.height}")
                Text(text = "Вес до беременности: ${firsBodySize.value?.weightSizeBefore}")
                Text(text = "ИМТ: $formattedBmi")
                WeightGainLineChart(week = week!!, bmi = bmiLong.toDouble(), data.value)
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(data.value) { bodySize ->
                        bodySize?.let {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                shape = RoundedCornerShape(12.dp), // Углы
                                elevation =  CardDefaults.elevatedCardElevation(4.dp)// Тень
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = "Неделя: ${bodySize.id}",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text(
                                            text = "Вес: ${bodySize.weight} кг",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = "Прибавка веса: ${bodySize.weightGain} кг",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun AlertDialogAddWeigh(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val weightSize = remember { mutableStateOf("") }
    val isConfirmButtonEnabled = weightSize.value.isNotBlank()
    AlertDialog(onDismissRequest = { onDismiss() },
        title = { Text("Укажите вес сегодня") },
        text = {
            Column {
                TextField(
                    value = weightSize.value, onValueChange = { weightSize.value = it },
                    label = { Text("Вес (кг)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(weightSize.value) },
                enabled = isConfirmButtonEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isConfirmButtonEnabled) Purple40 else Color.Gray
                )
            ) {
                Text(text = "Добавить")

            }

        },
        dismissButton = {
            Button(
                onClick = onDismiss // Просто закрываем диалог
            ) {
                Text("Отмена")
            }
        })
}

@Composable
fun WeightGainLineChart(week: Int, bmi: Double, dataBodySize: List<BodySizeEntity?>) {
    val lineData = mutableListOf<Entry>().apply {
        add(Entry(0f, 0f))  // Добавляем фиктивную точку
        addAll((1..week).map { weekNumber ->
            val value = when {
                bmi < 19 -> weightGainByWeek[weekNumber]?.first ?: 0.0
                bmi in 19.0..25.0 -> weightGainByWeek[weekNumber]?.second ?: 0.0
                else -> weightGainByWeek[weekNumber]?.third ?: 0.0
            }
            Entry(weekNumber.toFloat(), value.toFloat())
        })
    }.filter { entry ->
        val weekNum = entry.x.toInt()
        if (week <= 10) {
            true
        } else {
            weekNum % 3 == 0
        }

    }


    val listData1 = mutableListOf<Entry>().apply {
        val data = dataBodySize.size
        val ff = week / data //если при делении получается число больше 2 то записываем каждую точку, если меньше 2 то записываем каждую вторую точку
        add(Entry(0f, 0f))
        addAll(dataBodySize.mapNotNull { siziIt ->
            siziIt?.let {
                val weekId = siziIt.id
                val weight = siziIt.weightGain
                Entry(weekId.toFloat(), weight!!.toFloat())
            }
        })
    }

    // Используем AndroidView для вставки LineChart из MPAndroidChart
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        factory = { context ->

            LineChart(context).apply {
                description.isEnabled = false  // Отключаем описание
                setTouchEnabled(true)  // Включаем возможность взаимодействия
                isDragEnabled = true  // Включаем перетаскивание
                setScaleEnabled(true)  // Включаем масштабирование
                val firstDataSet = LineDataSet(lineData, "Средняя прибавка").apply {
                    color = ContextCompat.getColor(context, R.color.teal_200)
                    lineWidth = 2f
                    setCircleColor(ContextCompat.getColor(context, R.color.teal_200))
                    circleRadius = 3f
                }

                // Вторая линия - данные BodySize
                val secondDataSet = LineDataSet(listData1, "Фактическая прибавка").apply {
                    color = ContextCompat.getColor(context, R.color.purple_200)
                    lineWidth = 2f
                    setCircleColor(ContextCompat.getColor(context, R.color.purple_500))
                    circleRadius = 3f
                }

                // Добавляем обе линии в график
                data = LineData(firstDataSet, secondDataSet)
                invalidate()  // Обновляем график
            }

        }
    )
}

@Preview
@Composable
fun WeightGainChartPreview() {
    WeightGainLineChart(
        38, bmi = 20.0, dataBodySize = listOf(
            BodySizeEntity(11, 55f, 59f, 166f, 20f, 4f),
            BodySizeEntity(20, 55f, 59f, 166f, 20f, 6f)
        )
    )
}
