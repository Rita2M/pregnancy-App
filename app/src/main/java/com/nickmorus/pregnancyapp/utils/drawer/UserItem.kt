package com.nickmorus.pregnancyapp.utils.drawer
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.nickmorus.pregnancyapp.R

@Composable
fun UserItem(
    username: String,
    description: String,
    iconId: Int,
    onClicked: ()-> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    )
    {
        Column {
            Row(
                modifier = Modifier.alpha(0f).clickable {
                    onClicked()
                },
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.White, CircleShape),
                    painter = painterResource(id = iconId),
                    contentScale = ContentScale.Crop,
                    contentDescription = "user icon"
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(15f),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp),
                        text = username,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp),
                        text = description,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
                Icon(
                    modifier = Modifier
                        .weight(1f)
                        .width(40.dp)
                        .height(40.dp)
                        .padding(top = 4.dp, bottom = 4.dp),
                    painter = painterResource(id = R.drawable.user_item_arrow),
                    contentDescription = username,
                    tint = Color.White
                )
            }
            DropdownElement()
        }

    }
}

@Composable
fun DropdownElement() {
    var expanded by remember { mutableStateOf(false) }
    val buttonText = remember{ mutableStateOf("Планирование") }
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomEnd
    ) {
        IconButton(
            modifier = Modifier.width(150.dp),
            onClick = {
                expanded = !expanded
            },
        )
        {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End
            ) {
                Text(text = buttonText.value,
                    fontSize = 16.sp,
                    softWrap = false,
                    color = Color.White)
                Icon(
                    modifier = Modifier.height(10.dp),
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    tint = Color.White,
                    contentDescription = "down_arrow")
            }
        }

        DropdownMenu(
            expanded = expanded,
            offset = DpOffset(x = 200.dp, y = 0.dp),
            onDismissRequest = { expanded = false }
        ) {
            DropdownElementMenu("Планирование", onClicked = {
                expanded = !expanded
                buttonText.value = "Планирование"
            }
            )
            DropdownElementMenu("Беременность", onClicked = {
                expanded = !expanded
                buttonText.value = "Беременность"
                }
            )
            DropdownElementMenu("Ребёнок", onClicked = {
                expanded = !expanded
                buttonText.value = "Ребёнок"
            }
            )
        }
    }
}

@Composable
fun DropdownElementMenu(
    title: String,
    onClicked: () -> Unit
) {
    DropdownMenuItem(
        text = { Text(title) },
        onClick = {
            onClicked()
        }
    )
}







@Preview(backgroundColor = 0xF256F5)
@Composable
fun UserItemPreview() {
    UserItem("Имя пользователя", "Описание пользователя", R.drawable.drawer_account, {})
}
