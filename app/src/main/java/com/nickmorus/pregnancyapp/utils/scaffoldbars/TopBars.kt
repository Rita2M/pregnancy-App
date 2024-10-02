package com.nickmorus.pregnancyapp.utils.scaffoldbars

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nickmorus.pregnancyapp.R

@Composable
fun LibraryTopBar(
    title:String,
    onMenuClicked: () -> Unit,
    onBellClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.pink)),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(modifier = Modifier
            .weight(1f)
            .padding(top = 16.dp, start = 8.dp, end = 8.dp, bottom = 16.dp)){
            Icon(painter = painterResource(id = R.drawable.topbar_menu),
                  contentDescription = null,
                  modifier = Modifier
                      .align(Alignment.Center)
                      .clickable {
                          onMenuClicked()
                      },
                tint = Color.White
            )
        }
        Text(
            modifier = Modifier.weight(3f),
            color = Color.White,
            fontSize = 18.sp,
            text = title,
            textAlign = TextAlign.Center
        )
        Box(modifier = Modifier.weight(1f)){
            Icon(painter = painterResource(id = R.drawable.topbar_bell),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable {
                        onBellClicked()
                    },
                tint = Color.White
            )
        }
    }
}
@Preview
@Composable
fun LibraryTopBarPreview() {
    LibraryTopBar("Библиотека", {}, {})
}


@Composable
fun ArticleTopBar(
    title:String,
    onBackClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.pink)),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(modifier = Modifier
            .weight(1f)
            .padding(top = 16.dp, start = 8.dp, end = 8.dp, bottom = 16.dp)){
            Icon(painter = painterResource(id = R.drawable.back),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable {
                        onBackClicked()
                    },
                tint = Color.White
            )
        }
        Text(
            modifier = Modifier.weight(3f),
            color = Color.White,
            fontSize = 18.sp,
            text = title,
            textAlign = TextAlign.Center
        )
        Divider(modifier = Modifier.weight(1f))
    }
}
@Preview
@Composable
fun ArticleTopBarPreview() {
    ArticleTopBar("Статья", {}, )
}