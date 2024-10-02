package com.nickmorus.pregnancyapp.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nickmorus.pregnancyapp.ui.theme.Purple40

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StartupCarousel(
    onButtonClick : ()-> Unit
) {
    val pagerState = rememberPagerState( pageCount = {4})
    HorizontalPager(
        state = pagerState
    ) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(top=16.dp),
                    textAlign = TextAlign.Center,
                    text = "Карусель картинок с рассказом о том как необходимо использовать приложение (как в банковском приложении, например)"
                )
                Divider(modifier = Modifier.weight(1f), color = Color.Transparent)
                if (it == 3) {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Purple40
                        ),
                        onClick = {
                            onButtonClick()
                        }
                    ) {
                        Text("Открыть приложение",color = Color.White)
                    }
                }
            }
    }
}
