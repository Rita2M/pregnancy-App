package com.nickmorus.pregnancyapp.screens.main.more

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nickmorus.pregnancyapp.ui.theme.Purple40

@Composable
fun MoreScreen(
    onMotionCounter: () -> Unit,
    onCounterContractions: () -> Unit,
    onGraphClick:() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Используем padding из библиотеки Compose
        horizontalAlignment = Alignment.CenterHorizontally, // Центрируем кнопки по горизонтали
        verticalArrangement = Arrangement.Center // Центрируем кнопки по вертикали
    ) {
        Spacer(modifier = Modifier.height(16.dp)) // Добавляем отступ между кнопками

        Button(
            onClick = onCounterContractions,
            colors = ButtonDefaults.buttonColors(containerColor = Purple40),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(56.dp), // Увеличиваем высоту кнопки
            shape = RoundedCornerShape(12.dp) // Округляем углы кнопки
        ) {
            Text(
                text = "Счетчик схваток",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp)) // Добавляем отступ между кнопками

        Button(
            onClick = onMotionCounter,
            colors = ButtonDefaults.buttonColors(containerColor = Purple40),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Счетчик шевелений",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onGraphClick,
            colors = ButtonDefaults.buttonColors(containerColor = Purple40),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "График прибавки веса",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }
    }

    }
