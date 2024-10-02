package com.nickmorus.pregnancyapp.screens.main.library.articles

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.nickmorus.pregnancyapp.R
import com.nickmorus.pregnancyapp.data.entities.Topic
import com.nickmorus.pregnancyapp.utils.Base64Image
import java.util.UUID


@Composable
fun ArticleItem(
    modifier: Modifier = Modifier,
    title: String,
    imageUrl: String?,
    isOpened: Boolean = false,
    onClicked: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .clickable { onClicked() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 16.dp),

        shape = RoundedCornerShape(16.dp),


    ) {
        Column {
                Base64Image(base64String = imageUrl, modifier =Modifier.fillMaxWidth()
                    .padding(16.dp)
                    .height(180.dp) // Высота изображения
                    .clip(RoundedCornerShape(16.dp)))

            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isOpened) Color.Gray else Color.Black
                )

            }
        }
    }


@Preview(showBackground = true)
@Composable
fun LibraryScreenItemsPreview() {
    val content = listOf(
        Topic(UUID.randomUUID(), image = R.drawable.placeholder.toString()),
        Topic(UUID.randomUUID(), "второй", R.drawable.placeholder.toString())

    )
    LazyColumn {
        items(content) {
            ArticleItem(
                title = it.title,
                imageUrl = it.image,
                onClicked = {
                }
            )
        }
    }
}

@Preview
@Composable
fun LibraryScreenItemPreview() {
    ArticleItem(
        title = "Вступление",
        isOpened = false,
        imageUrl = "",
        onClicked = {}
    )
}
@Composable
fun ImageLoader(imageUrl: String?) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .build()
        )

        when (painter.state) {
            is AsyncImagePainter.State.Loading -> {
                CircularProgressIndicator()
            }
            is AsyncImagePainter.State.Error -> {
                Text(text = "Ошибка загрузки изображения")
            }
            else -> {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp) // Высота изображения
                        .clip(RoundedCornerShape(16.dp))
                )
            }
        }
    }
}
