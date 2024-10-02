package com.nickmorus.pregnancyapp.screens.main.library.topics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nickmorus.pregnancyapp.data.entities.Topic
import com.nickmorus.pregnancyapp.ui.theme.FontSizeTopics
import com.nickmorus.pregnancyapp.ui.theme.PaddingSuperMin
import com.nickmorus.pregnancyapp.ui.theme.Purple40
import java.util.UUID
import kotlin.io.encoding.ExperimentalEncodingApi

//@OptIn(ExperimentalEncodingApi::class)
//@Composable
//fun TopicsScreen(
//    viewModel: TopicsViewModel,
//    onTopicClicked: (Topic)->Unit
//) {
//    val topics by viewModel.data.collectAsState(initial = emptyList())
//    Box( modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.BottomCenter ) {
//
//        if (topics.isNotEmpty()) {
//            LazyRow(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(PaddingMin)
//            )
//            {
//                items(topics) {
//                    LibraryTopicItem(
//                        title = it.title,
//                    ) {
//                        onTopicClicked(it)
//                    }
//                }
//            }
//        } else {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.BottomCenter
//            ) {
//                CircularProgressIndicator() // привязка прогресса к индикатору
//            }
//        }
//    }
//}

@OptIn(ExperimentalEncodingApi::class)
@Composable
fun LibraryTopicItem(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    title: String,
    onClicked: () -> Unit
)
{
    Surface(modifier = Modifier
        .padding(6.dp)

        .clickable { onClicked() },
        color = MaterialTheme.colorScheme.background

    )
    {
            Row(
                modifier = Modifier


//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.End,
            ) {


                Text(
                    modifier = Modifier
                        .padding(PaddingSuperMin)
                        .height(IntrinsicSize.Min)
                        .background(Color.Transparent),

                    text = title,
                    textAlign = TextAlign.Center,

                    color = if (isSelected) Purple40 else Color.Black,
                    fontSize = FontSizeTopics)

            }
        }
    }


@Preview(showBackground = true)
@Composable
fun LibraryScreenItemsPreview() {
    val content = listOf(
        Topic(UUID.randomUUID(), "Вступление","fff"),
        Topic(UUID.randomUUID(), "Охуевание", "ggg"),
        Topic(UUID.randomUUID(), "Охуевание", "ff"),
        Topic(UUID.randomUUID(), "Охуевание", "rr"),
        Topic(UUID.randomUUID(), "Охуевание", "tt")

    )
    LazyRow{
        items(content){
            LibraryTopicItem(
                title = it.title,
                onClicked = {
                },
                isSelected = true
            )
        }
    }
}

@Preview
@Composable
fun LibraryScreenItemPreview() {
    LibraryTopicItem(
        title = "Вступление",
        onClicked = {},
        isSelected = false
    )
}
