package com.nickmorus.pregnancyapp.screens.main.more


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
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
import com.nickmorus.pregnancyapp.R
import com.nickmorus.pregnancyapp.ui.theme.Purple40
import java.time.LocalTime

@Composable
fun MotionCounterScreen(
    viewModel: MoreViewModel = hiltViewModel(),
    onInformationClick :() -> Unit,
    onBackClick:()-> Unit,

) {
    val countMotion = viewModel.countMotion.collectAsState()
    val dateHistory = viewModel.dataAll.collectAsState(initial = emptyList())
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }
    val timeInput = remember { mutableStateOf("") }
    val openMotionData = remember { mutableIntStateOf(-1) }
    fun addDetected() {
        viewModel.saveMovementDetected()

    }


    fun onDeleteLastTime() {
        viewModel.deleteLastMovement(onError = {
            Toast.makeText(
                context,
                "Сегодня нет шевелений",
                Toast.LENGTH_SHORT
            ).show()
        })
    }


    fun saveNewTime() {
        // Преобразуем строку во время
        val parsedTime = try {
            LocalTime.parse(timeInput.value)
        } catch (e: Exception) {
            Toast.makeText(context, "Неверный формат времени", Toast.LENGTH_SHORT).show()
            null
        }

        if (parsedTime != null) {
            viewModel.saveMovementWithTime(parsedTime)
            Toast.makeText(context, "Время сохранено", Toast.LENGTH_SHORT).show()
            showDialog.value = false // Закрываем диалог
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
           verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.Start)
        ) {
            // Кнопка "Назад"
            IconButton(
                onClick = { onBackClick() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Назад",
                    tint = Color.Black
                )
            }
            Text(
                text = "Счетчик шевелений",
               // fontSize = 20.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // Счетчик в центре
        Text(
            text = "Шевеления: ${countMotion.value}",
            fontSize = 32.sp
        )

        // История шевелений
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Top
        ) {
            itemsIndexed(dateHistory.value) { index, record ->
               Column(modifier = Modifier
                   .padding(vertical = 8.dp)
                   .clickable { openMotionData.intValue = if (openMotionData.intValue == index) -1 else index }) {
                   Row {


                       Text(
                           text = "Дата: ${record.id}",
                           fontSize = 16.sp,
                           fontWeight = FontWeight.Bold
                       )
                       Text(
                           text = "Количество шевелений: ${record.movementTimes.size}",
                           fontSize = 14.sp
                       )
                   }
                   if (openMotionData.intValue == index) {
                       Column {
                           record.movementTimes.forEach { time ->
                               Text(text = "Время: $time")
                           }

                       }
                   }
                }
            }
        }


        Row(verticalAlignment = Alignment.Bottom) {
            RoundIconButton(
                onMovementDetected = { addDetected() },
                onInformationClick = { onInformationClick() },
                onDeleteClicked = { onDeleteLastTime() },
                onSaveNewTime = {showDialog.value = true})

        }
    }



        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text(text = "Добавить время") },
                text = {
                    Column {
                        Text(text = "Введите время в формате HH:mm")
                        TextField(
                            value = timeInput.value,
                            onValueChange = { timeInput.value = it },
                            placeholder = { Text("Например, 14:30") }
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = { saveNewTime() }) {
                        Text("Сохранить")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog.value = false }) {
                        Text("Отмена")
                    }
                }
            )
        }
    }



@Composable
fun RoundIconButton(onMovementDetected: () -> Unit, onInformationClick: () -> Unit, onDeleteClicked:()-> Unit, onSaveNewTime:()->Unit){
    Row(Modifier.fillMaxWidth()
        ) {
        IconButton(onClick = { onInformationClick()},
            modifier = Modifier.weight(1f)) {
            Icon(painter = painterResource(id = R.drawable.drawer_info), contentDescription = "Информация")

        }
        IconButton(onClick = {onDeleteClicked()},
                modifier = Modifier.weight(1f)) {
            Icon(painter = painterResource(id = R.drawable.ic_delete_24), contentDescription ="Удалить" )

            
        }
        IconButton(onClick = { onSaveNewTime() },
            modifier = Modifier.weight(1f)) {
            Icon(painter = painterResource(id = R.drawable.ic_pius), contentDescription = "добавить")
            
            
        }
        Spacer(modifier = Modifier.weight(3f))

        IconButton(
            onClick = { onMovementDetected() },
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape) // Делаем кнопку круглой
                .background(Purple40)

        ) {
            Icon(
                painter = painterResource(id = R.drawable.foot),
                contentDescription = "Шевеление",
                modifier = Modifier.size(56.dp) // Размер иконки внутри кнопки
            )
        }
    }

}
//@Composable
//fun OpenTextItem(record: MotionRecordEntity, onClickClosed:()-> Unit){
//    Column(
//        Modifier
//            .fillMaxWidth()
//            .clickable { onClickClosed() }) {
//        Text(text = "Время шевелений за выбранный день")
//        LazyColumn {
//            items(record.movementTimes){
//                Text(text = "$it")
//            }
//        }
//
//    }
//}
