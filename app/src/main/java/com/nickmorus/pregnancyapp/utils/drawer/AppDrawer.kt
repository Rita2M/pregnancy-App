package com.nickmorus.pregnancyapp.utils.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nickmorus.pregnancyapp.R
import com.nickmorus.pregnancyapp.ui.theme.Purple40



@Composable
fun AppDrawer(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    onInviteFriendClicked: () -> Unit,
    onAboutAppClicked: () -> Unit,
    onWriteUsClicked: () -> Unit,
    onTelegramClicked : () -> Unit,

    content: @Composable ()-> Unit
) {
    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = modifier.fillMaxWidth(0.7f)
                ) {
                    val boxWeight = 6f
                    Box(
                        modifier = Modifier
                            .weight(boxWeight)
                            .background(Purple40),
                        contentAlignment = Alignment.BottomEnd
                    ){

                    }
                    Box(
                        modifier = Modifier
                            .weight(4f)
                            .background(MaterialTheme.colorScheme.background)
                    ){
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Column(
                                modifier = Modifier
                                    .verticalScroll(rememberScrollState())
                                    .weight(10f)
                            ) {
                                DrawerItem("Пригласить друга", R.drawable.drawer_invite_friend, onInviteFriendClicked)
                                DrawerItem("О приложении", R.drawable.drawer_info, onAboutAppClicked)
                                DrawerItem("Напишите нам", R.drawable.drawer_write_us, onWriteUsClicked)
                                DrawerItem("Мы в Telegram", R.drawable.ic_telegram, onTelegramClicked)

                            }
                            Text(
                                modifier = Modifier.weight(1f),
                                text = "Версия 0.6"
                            )
                        }
                    }
                }
            }
    }){
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DrawerContentPreview() {
    AppDrawer(
        drawerState = DrawerState(initialValue = DrawerValue.Open),
        onInviteFriendClicked = {},
        onAboutAppClicked = {},
        onWriteUsClicked = {},
        onTelegramClicked = {},
        content = {}
    )
}
