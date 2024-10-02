package com.nickmorus.pregnancyapp.screens.ad

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AdBanner()
{
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp) // Высота баннера
            .background(Color.Gray) // Цвет фона баннера
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Рекламный баннер",
            color = Color.Black, // Цвет текста
            style = MaterialTheme.typography.bodyMedium // Стиль текста
        )
    }
}
