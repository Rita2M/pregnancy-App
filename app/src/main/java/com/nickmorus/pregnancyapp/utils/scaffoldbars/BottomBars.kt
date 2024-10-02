package com.nickmorus.pregnancyapp.utils.scaffoldbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nickmorus.pregnancyapp.MainScreen
import com.nickmorus.pregnancyapp.R
import com.nickmorus.pregnancyapp.ui.theme.Purple40

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    BottomAppBar(
        Modifier
            .fillMaxWidth()
            .height(60.dp))
    {
        NavigationBar(
            modifier = modifier.fillMaxSize(),
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            BottomNavigationConstants.Items.forEach { navItem ->
                val itemsColor:Color =
                    if(currentRoute == navItem.route)
                        Purple40
                    else
                        Color.Black
                NavigationBarItem(
                    selected = currentRoute == navItem.route,
                    onClick = {
                        navController.navigate(navItem.route)
                    },
                    icon = {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Icon(imageVector = ImageVector.vectorResource(navItem.iconId),
                                contentDescription = navItem.title,
                                tint = itemsColor)
                            Text(text = navItem.title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 12.sp, softWrap = false,
                                color = itemsColor)
                        }
                    },
                    alwaysShowLabel = true
                )
            }
        }
    }
}

object BottomNavigationConstants{
    val Items = listOf(
        BottomNavItem("Сегодня", R.drawable.bottombar_today, MainScreen.Today.route ),
        BottomNavItem("Статьи", R.drawable.bottombar_library, MainScreen.TopicsAndArticles.route),
        BottomNavItem("Ещё", R.drawable.ic_pius, MainScreen.More.route),
        BottomNavItem("Календарь", R.drawable.bottombar_additional, MainScreen.Calendar.route)
    )
}

data class BottomNavItem(val title: String, val iconId:Int, val route: String)

@Preview
@Composable
fun BottomBarPreview() {
    BottomBar(
        navController = rememberNavController()
    )
}
