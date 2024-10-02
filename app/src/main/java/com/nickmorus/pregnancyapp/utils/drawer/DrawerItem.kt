package com.nickmorus.pregnancyapp.utils.drawer
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nickmorus.pregnancyapp.R

@Composable
fun DrawerItem(
    title:String,
    iconId: Int,
    onClicked: ()-> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 8.dp)
            .clickable {
                onClicked()
            },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
                    .padding(top = 4.dp, bottom = 4.dp),
                painter = painterResource(id = iconId),
                contentDescription = title,

            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(15f),
                text = title,
                fontSize = 16.sp,

            )
            Icon(
                modifier = Modifier
                    .weight(1f)
                    .width(40.dp)
                    .height(40.dp)
                    .padding(top = 4.dp, bottom = 4.dp),
                painter = painterResource(id = R.drawable.drawer_arrow),
                contentDescription = title,

            )
        }
        Divider(modifier = Modifier.padding(start = 38.dp))
    }
}

@Preview
@Composable
fun DrawerItemPreview() {
    DrawerItem("Пригласить друга", R.drawable.drawer_account, {})
}
