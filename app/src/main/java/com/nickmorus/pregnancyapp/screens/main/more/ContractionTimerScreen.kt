package com.nickmorus.pregnancyapp.screens.main.more

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nickmorus.pregnancyapp.R
import com.nickmorus.pregnancyapp.ui.theme.Purple40
import com.nickmorus.pregnancyapp.ui.theme.PurpleGrey40
import com.nickmorus.pregnancyapp.utils.textSecond
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@SuppressLint("DefaultLocale")
@Composable
fun ContractionTimerScreen(onBackClick :()-> Unit){
    val timeLeft = remember { mutableIntStateOf(0) }
    val isRunning = remember { mutableStateOf(false) } // Таймер работает или нет
    val isContraction = remember { mutableStateOf(false) }
    val startTime = remember { mutableStateOf("") }
    val history = remember { mutableStateListOf<Triple<String,String,Int>>() }
    val buttonColor = if (isRunning.value) Purple40 else PurpleGrey40
    val contentColor = Color.White


    // Функция для обновления времени и состояния
    fun toggleState() {
        if (isRunning.value) {
            // Таймер был запущен, схватка закончилась
            val endTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            val duration = timeLeft.intValue // Длительность схватки в секундах
            history.add(Triple(startTime.value,endTime, duration) )
            timeLeft.intValue = 0 // Сбрасываем таймер
        } else {
            // Таймер не запущен, начинается новая схватка
            startTime.value = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            timeLeft.intValue = 0 // Сбрасываем таймер для новой схватки
        }
        isRunning.value = !isRunning.value // Переключаем состояние таймера
        isContraction.value = !isContraction.value // Переключаем между "Схватка" и "Отдых"
    }
    // Таймер
    LaunchedEffect(isRunning.value) {
        while (isRunning.value) {
            delay(1000L)
            timeLeft.intValue += 1 // Увеличиваем время на 1 секунду
        }
    }

    Column(modifier = Modifier.fillMaxSize(),   verticalArrangement = Arrangement.SpaceBetween) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Start),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Кнопка "Назад"
            Button(
                onClick = { onBackClick() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                // modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Назад",
                    tint = Color.Black
                )
            }
            Text(text = "Счетчик схваток")
        }

        // Внешний вид таймера и состояния
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Метка "Схватка" или "Отдых"
            Text(
                text = if (isContraction.value) "Схватка" else "Отдых",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            // Отсчет времени
            Text(
                text = String.format("%02d:%02d", timeLeft.intValue / 60, timeLeft.intValue % 60),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }


        // История схваток и отдыхов
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Начало", fontWeight = FontWeight.Bold)
            Text(text = "Конец", fontWeight = FontWeight.Bold)
            Text(text = "Длительность", fontWeight = FontWeight.Bold)
        }

        // История схваток и отдыхов
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            items(history) { item ->

                Row(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = item.first)  // Время начала
                    Text(text = item.second) // Время окончания
                    Text(text = "${item.third} ${textSecond(item.third)}") // Длительность
                }
            }
        }



            // Кнопка для переключения состояния
            Button(
                onClick = { toggleState() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = contentColor

                )
            )
            {
                Text(text = if (isRunning.value) "Остановить" else "Начать")
            }
        }


    }
