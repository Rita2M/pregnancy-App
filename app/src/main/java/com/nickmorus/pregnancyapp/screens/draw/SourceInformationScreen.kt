package com.nickmorus.pregnancyapp.screens.draw

import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nickmorus.pregnancyapp.R
import com.nickmorus.pregnancyapp.data.entities.Document
import com.nickmorus.pregnancyapp.toasts.ToastNotInformation

@Composable
fun SourceInformationScreen(
    navController: NavController,
    viewModel: DrawViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val data = viewModel.data.collectAsState(initial = emptyList()).value
    val state = viewModel.feedModel.collectAsState()
    when {
        state.value.error -> ToastNotInformation(context = context, message = "Информация отсутствует, повторите позднее")

        state.value.loading -> CircularProgressIndicator()


    }
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Назад",
                tint = Color.Black
            )

        }
        if (data.isNotEmpty()) {
            val document = data[1]

            Text(
                text = document.name,
                fontSize = 24.sp, // Крупный шрифт
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp) // Отступ снизу
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(16.dp)
            ) {
                item {
                    SourceInformationContent(doc = document)
                }
            }

        } else {

            CircularProgressIndicator()
        }

    }
}

@Composable
fun SourceInformationContent(doc: Document) {
    AndroidView(
        factory = { context ->
            TextView(context).apply {
                // Используем HTML совместимость для отображения контента
                text = HtmlCompat.fromHtml(doc.content, HtmlCompat.FROM_HTML_MODE_LEGACY)
                textSize = 16f // Размер текста
            }
        },
        modifier = Modifier.padding(bottom = 16.dp)
    )
}
