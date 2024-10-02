package com.nickmorus.pregnancyapp.screens.article

import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nickmorus.pregnancyapp.R
import com.nickmorus.pregnancyapp.ui.theme.Purple40
import com.nickmorus.pregnancyapp.ui.theme.Purple80


@Composable
fun ContentScreen(
    viewModel: ArticleViewModel = hiltViewModel(),
    onBackClick: () -> Unit,

) {
    val content by viewModel.content.collectAsState()



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount > 0) { // Swipe right
                        onBackClick()
                    }
                }
            }
    ) {
        content?.let { articleContent ->
            val updatedHtmlContent = updateImageTags(articleContent.content, articleContent.image)
            ImageContentScreen(content = updatedHtmlContent, onBackClick = onBackClick)
        } ?: run {
            // Заглушка при загрузке данных
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun ImageContentScreen(
    content: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    var textSize by remember { mutableStateOf(16.sp) }
    val scrollState = rememberScrollState()
    val buttonSize = 48.dp

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Добавляем кнопку назад и другие элементы интерфейса
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .background(Color.Transparent)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            // Кнопка увеличения текста
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .background(Color.White, shape = CircleShape)
                    .size(buttonSize)
            ) {
                GradientButton(onClick = {textSize = (textSize.value + 2).sp})

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Отображение HTML-контента с обновленным изображением
        HtmlContentScreen(htmlContent = content, textSize = textSize)
    }
}

@Composable
fun HtmlContentScreen(htmlContent: String, textSize: TextUnit) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AndroidView(
            factory = { context ->
                TextView(context).apply {
                    text = HtmlCompat.fromHtml(htmlContent, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    setTextSize(textSize.value)
                }
            },
            update = {update ->
                update.textSize = textSize.value
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

// Функция для замены <img> тегов в HTML контенте
fun updateImageTags(htmlContent: String, imageUrl: String?): String {
    if (imageUrl == null) return htmlContent

    // Используем регулярное выражение для нахождения <img> тегов и замены их src атрибута
    val imgTagRegex = """<img[^>]+src=["']([^"']+)["'][^>]*>""".toRegex()
    return imgTagRegex.replace(htmlContent) { matchResult ->
        // Заменяем src на нужный URL изображения
        matchResult.value.replace(Regex("""src=["'][^"']+["']"""), """src="$imageUrl"""")
    }
}
@Composable
fun GradientButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp)) // Закругляем углы
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Purple80,
                        Purple40
                    ) // Градиент от голубого к фиолетовому
                )
            )
            .border(2.dp, Color.White, RoundedCornerShape(16.dp)),
        colors = ButtonDefaults.run {
            buttonColors(
               Color.Transparent // Делаем фон кнопки прозрачным, чтобы градиент был виден
            )
        },
        contentPadding = PaddingValues(16.dp)
    ) {
        Icon(painter = painterResource(id = R.drawable.bottombar_library), contentDescription = "plus")
    }
}
@Preview
@Composable
fun PreviewScreen(){
    ContentScreen(viewModel = viewModel(), onBackClick = {})
}
