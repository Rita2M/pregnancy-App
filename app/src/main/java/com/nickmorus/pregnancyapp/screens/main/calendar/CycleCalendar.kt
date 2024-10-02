@file:Suppress("DEPRECATION")

package com.nickmorus.pregnancyapp.screens.main.calendar

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import com.nickmorus.pregnancyapp.AppViewModel
import com.nickmorus.pregnancyapp.R
import com.nickmorus.pregnancyapp.data.entities.CycleEntity
import com.nickmorus.pregnancyapp.data.entities.EmojiDayEntity
import com.nickmorus.pregnancyapp.data.entities.NotesEntity
import com.nickmorus.pregnancyapp.ui.theme.Lavender
import com.nickmorus.pregnancyapp.ui.theme.Purple40
import com.nickmorus.pregnancyapp.utils.information.EmojiType
import com.nickmorus.pregnancyapp.utils.textDays
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

@Composable
fun CalendarScreen(
    calendarViewModel: CalendarViewModel = hiltViewModel(),
    viewModelApp: AppViewModel = hiltViewModel(),
    notesViewModel: NoteViewModel = hiltViewModel()
) {
    val dataEmoji = calendarViewModel.dataEmoji.collectAsState(initial = emptyList())
    val cycles = calendarViewModel.data.collectAsState().value
    val calendarState = rememberSelectableCalendarState()
    val showDialog = remember { mutableStateOf(false) }
    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }
    val showAlertDialogDeleteCycle = remember {
        mutableStateOf(false)
    }
    val itemIdToDelete = remember {
        mutableStateOf<Int?>(null)
    }
    val today = LocalDate.now()
    val showMenstrualInput = remember { mutableStateOf(false) }
    val showNoteInput = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val feedModel = calendarViewModel.feedModel.collectAsState()
    val isPregnant = viewModelApp.isPregnant.collectAsState()
    val notes = notesViewModel.dataNotes.collectAsState(initial = emptyList())
    val note = calendarViewModel.selectedNote.collectAsState()
    val cycle = calendarViewModel.selectedCycle.collectAsState()
    val showEmojiPicker = remember { mutableStateOf(false) }


    if (showAlertDialogDeleteCycle.value && itemIdToDelete.value != null) {
        AlertCycleDeleteDialog(
            id = itemIdToDelete.value!!,
            onDismiss = { showAlertDialogDeleteCycle.value = false },
            onConfirm = { id ->
                calendarViewModel.deleteCycle(id) // Удаление элемента
                showAlertDialogDeleteCycle.value = false
            }
        )
    }
    when {
        feedModel.value.error -> Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
    }




    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (selectedDate.value == null || showEmojiPicker.value || showDialog.value) {
            RecentCyclesView(
                cycles = cycles, onClickCycle = { cycle ->
                    showAlertDialogDeleteCycle.value = true
                    itemIdToDelete.value = cycle.id
                }, modifier = Modifier
                    .weight(0.4f)
                    .padding(8.dp),
                isPregnant = isPregnant.value
            )
        }
        // Вызываем функцию для получения цикла по выбранной дате
        LaunchedEffect(selectedDate.value) {
            selectedDate.value?.let {
                calendarViewModel.getCycleForDate(it.toString(), isPregnant.value)
            }
        }
        if (showNoteInput.value) {
            NoteInputScreen(
                selectedDate = selectedDate.value ?: LocalDate.now(),
                onSave = { note ->
                    notesViewModel.saveNote(
                        NotesEntity(selectedDate.value.toString(), note)
                    )
                    showNoteInput.value = false
                    selectedDate.value = null // Возвращаемся к основному экрану
                },
                onCancel = {
                    showNoteInput.value = false
                    selectedDate.value = null // Возвращаемся к основному экрану
                },
                modifier = Modifier.weight(0.4f),
                textNote = note.value?.note ?: "",


                )
        }

        // Отображаем экран для ввода информации о менструации и заметки
        if (showMenstrualInput.value) {
            val onSave: (LocalDate, Int, Int) -> Unit = { dateSelect, duration, cycleLength ->
                calendarViewModel.insertCycle(
                    dateSelect,
                    duration,
                    cycleLength,
                    false
                )
                showMenstrualInput.value = false
                selectedDate.value = null
            }
            MenstrualInputScreen(
                selectedDate = selectedDate.value ?: LocalDate.now(),
                onSave = onSave,
                onCancel = {
                    showMenstrualInput.value = false
                    selectedDate.value = null // Возвращаемся к основному экрану
                },
                modifier = Modifier.weight(0.4f),

                )

        }
        if (showEmojiPicker.value) {
            EmojiPicker(
                onEmojiSelected = { emojiType ->
                    calendarViewModel.saveEmojiForDate(selectedDate.value.toString(), emojiType)
                }, onDismissRequest = {
                    showEmojiPicker.value = false
                    selectedDate.value = null
                }
            )
        }




        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()) // Добавляем прокрутку, если нужно
                .weight(0.6f) // Вес для использования всего оставшегося пространства
        ) {

            SelectableCalendar(
                firstDayOfWeek = DayOfWeek.MONDAY,
                calendarState = calendarState,
                today = LocalDate.now(),
                dayContent = { dayState ->
                    DayContent(
                        dayState = dayState,
                        // locale = locale,

                        onDayClick = { clickedDate ->
                            selectedDate.value = clickedDate
                            when {
                                isPregnant.value -> {
                                    showNoteInput.value = true
                                }

                                 clickedDate > today -> {
                                    showNoteInput.value = true
                                     Toast.makeText( context,
                                        "Напишите заметку, ввод цикла на эту дату недоступен",
                                        Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    showDialog.value = true
                                }
                            }


                        },
                        onLongPress = { clickedDate ->
                            selectedDate.value = clickedDate
                            showEmojiPicker.value = true
                            showMenstrualInput.value = false
                            showNoteInput.value = false
                        },
                        cycles = cycles,
                        today = today,
                        isPregnant = isPregnant.value,
                        notes = notes.value,
                        dataEmoji = dataEmoji.value
                    )
                }
            )
        }
    }
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showDialog.value = false
                selectedDate.value = null
            },
            title = {
                Text(text = "Выберите действие")
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp) // Отступ между кнопками
                ) {
                    Button(
                        onClick = {
                            showDialog.value = false
                            showMenstrualInput.value = true
                        },
                        modifier = Modifier.fillMaxWidth() // Кнопка на всю ширину
                    ) {
                        Text("Отметить менструацию",color = Color.White)
                    }

                    Button(
                        onClick = {
                            showDialog.value = false
                            showNoteInput.value = true
                        },
                        modifier = Modifier.fillMaxWidth() // Кнопка на всю ширину
                    ) {
                        Text("Заметка",color = Color.White)
                    }
                }
            },
            confirmButton = {

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Button(
                        onClick = {
                            selectedDate.value = null
                            showDialog.value = false

                        }
                    ) {
                        Text("Отмена",color = Color.White)
                    }
                }
            },
        )
    }
}


@Composable
fun DayContent(
    dayState: DayState<DynamicSelectionState>,
    onDayClick: (LocalDate) -> Unit,
    onLongPress: (LocalDate) -> Unit,
    notes: List<NotesEntity>,
    today: LocalDate,
    cycles: List<CycleEntity>,
    isPregnant: Boolean,
    dataEmoji: List<EmojiDayEntity>
) {
    val context = LocalContext.current
    val vibrator = remember { context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }

    val isToday = dayState.date == today
    val isNoteDay = remember(dayState.date, notes) {
        notes.any {
            val date = LocalDate.parse(it.noteDate)
            dayState.date == date
        }

    }
    val emojiForDay = remember(dayState.date, dataEmoji) {
        dataEmoji.find { LocalDate.parse(it.date) == dayState.date }
    }
    val isMenstrualDay = remember(dayState.date, cycles) {
//        if (isPregnant) {
//            cycles.any { cycle ->
//                if (cycle.pregnancy) {
//                    val startDate = LocalDate.parse(cycle.startDate)
//                    val endDate = LocalDate.parse(cycle.endDate)
//                    dayState.date in startDate..endDate
//
//                } else false
//            }
//
//        } else {
        cycles.any { cycle ->
            val startDate = LocalDate.parse(cycle.startDate)
            val endDate = LocalDate.parse(cycle.endDate)
            dayState.date in startDate..endDate
        }

    }


    Column(
        modifier = Modifier
            .size(50.dp)
            .clickable { onDayClick(dayState.date) }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onDayClick(dayState.date) },
                    onLongPress = {
                        vibrator.vibrate(
                            VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
                        )
                        onLongPress(dayState.date)
                    }
                )
            }
            .background(
                color = if (isToday) Color.LightGray else Color.Transparent,
                shape = CircleShape
            ),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Text(text = dayState.date.dayOfMonth.toString())

        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isMenstrualDay) {

                Icon(
                    tint = Color.Red,
                    painter = painterResource(id = R.drawable.ic_water_drop_24),
                    contentDescription = "Menstrual Day",
                    modifier = Modifier.size(14.dp)
                )
            }
            if (isNoteDay) {
                Icon(
                    tint = Color.Black,
                    painter = painterResource(id = R.drawable.ic_baseline_attach_file_24),
                    contentDescription = "Note Day",
                    modifier = Modifier.size(14.dp)
                )
            }
            emojiForDay?.let {
                Text(
                    text = it.emojiType.emoji,
                    fontSize = 14.sp,
                    lineHeight = 12.sp,
                )
            }
        }
    }
}

@Composable
fun RecentCyclesView(
    cycles: List<CycleEntity>,
    onClickCycle: (CycleEntity) -> Unit, modifier: Modifier,
    isPregnant: Boolean
) {
    val recentCycles = cycles.sortedByDescending { LocalDate.parse(it.startDate) }.take(3)
    if (!isPregnant) {
        Column(modifier = modifier) {
            Text(text = "Последние циклы", style = MaterialTheme.typography.titleMedium)

            recentCycles.forEach { cycle ->
                val startDate = LocalDate.parse(cycle.startDate)
                val endDate = cycle.endDate?.let { LocalDate.parse(it) }
                    ?: startDate.plusDays(cycle.duration!!.toLong())
                val duration = ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1

                TextButton(
                    onClick = { onClickCycle(cycle) },
                    modifier = Modifier
                        .align(Alignment.Start),
                ) {

                    Text(
                        text = "${startDate.format(DateTimeFormatter.ofPattern("dd MMM"))} - ${
                            endDate.format(
                                DateTimeFormatter.ofPattern("dd MMM")
                            )
                        } (${duration} ${textDays(duration)})", color = Color.Black
                    )
                }
            }
        }
    } else {
        Column(modifier = modifier) {
            Text(
                text = "Последний цикл перед беременностью",
                style = MaterialTheme.typography.titleMedium
            )
            val pregnancyCycle = cycles.find { it.pregnancy }
            if (pregnancyCycle != null) {
                val startDate = LocalDate.parse(pregnancyCycle.startDate)
                val endDate = pregnancyCycle.endDate?.let { LocalDate.parse(it) }
                    ?: startDate.plusDays(pregnancyCycle.duration!!.toLong())
                val duration = ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1

                Text(
                    text = "${startDate.format(DateTimeFormatter.ofPattern("dd MMM"))} - ${
                        endDate.format(
                            DateTimeFormatter.ofPattern("dd MMM")
                        )
                    } (${duration} ${textDays(duration)})", color = Color.Black
                )
            } else {
                // Обрабатываем случай, когда нет беременного цикла
                Text(text = "Цикл беременности не найден")
            }

        }
    }
}

@Composable
fun AlertCycleDeleteDialog(
    id: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Подтверждение") },
        text = { Text("Вы уверены, что хотите удалить этот элемент?") },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(id) // Удаление элемента
                }
            ) {
                Text("Да")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss // Просто закрываем диалог
            ) {
                Text("Нет")
            }
        }
    )
}

@Composable
fun MenstrualInputScreen(
    selectedDate: LocalDate,
    onSave: (LocalDate, Int, Int) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier
) {
    val duration = remember { mutableStateOf("4") }
    val cycleLength = remember { mutableStateOf("") }
    val isSaveEnabled = duration.value.toIntOrNull() != null && duration.value.isNotEmpty() &&
            cycleLength.value.toIntOrNull() != null && cycleLength.value.isNotEmpty()
    Column(modifier = modifier) {
        Text(
            text = "Дата начала менструации $selectedDate",
            style = MaterialTheme.typography.bodyMedium
        )


        // Поле ввода для длительности менструации
        TextField(
            value = duration.value,
            onValueChange = { duration.value = it },
            label = { Text("Длительность (дней)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = duration.value.toIntOrNull() == null
        )
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = cycleLength.value,
            onValueChange = { cycleLength.value = it },
            label = { Text("Продолжительность цикла") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = cycleLength.value.toIntOrNull() == null
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Кнопки для сохранения или отмены
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { onCancel() },
                colors = ButtonDefaults.buttonColors(containerColor = Purple40)
            ) {
                Text("Отмена")
            }
            Button(
                onClick = {
                    onSave(
                        selectedDate,
                        duration.value.toInt(),
                        cycleLength.value.toInt(),

                        )

                },
                enabled = isSaveEnabled,
                colors = ButtonDefaults.buttonColors(containerColor = Purple40)
            ) {
                Text("Сохранить")
            }
        }
    }
}

@Composable
fun NoteInputScreen(
    selectedDate: LocalDate,
    onSave: (String) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier,
    textNote: String,


    ) {
    val noteText = remember { mutableStateOf("") }
    Log.d("CalendarNoteText", "noteText:$noteText,textNote: $textNote")
    LaunchedEffect(textNote) {
        noteText.value = textNote
    }

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "Заметка на $selectedDate", style = MaterialTheme.typography.bodyMedium)

        TextField(
            value = noteText.value,
            onValueChange = { noteText.value = it },
            label = { Text("Заметка") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            singleLine = false,
        )
        Spacer(modifier = Modifier.height(4.dp))

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { onCancel() }) {
                Text("Отмена")
            }
            Button(onClick = {
                onSave(noteText.value)
            }) {
                Text("Сохранить")
            }
        }
    }

}

@Composable
fun EmojiPicker(
    onEmojiSelected: (EmojiType) -> Unit,
    onDismissRequest: () -> Unit
) {
    Popup(

        alignment = Alignment.BottomStart,
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Lavender)
                .width(300.dp)

        ) {
            LazyRow(
                modifier = Modifier.padding(8.dp)
            ) {
                items(EmojiType.entries) { emojiType ->
                    IconButton(onClick = {
                        onEmojiSelected(emojiType)
                        onDismissRequest()
                    }) {
                        Text(text = emojiType.emoji, fontSize = 24.sp)
                    }
                }
            }
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}
