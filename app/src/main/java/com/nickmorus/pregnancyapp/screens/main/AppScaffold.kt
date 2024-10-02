package com.nickmorus.pregnancyapp.screens.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nickmorus.pregnancyapp.MainScreen
import com.nickmorus.pregnancyapp.R
import com.nickmorus.pregnancyapp.data.entities.ArticleMeta
import com.nickmorus.pregnancyapp.screens.main.calendar.CalendarScreen
import com.nickmorus.pregnancyapp.screens.main.library.TopicsAndArticlesScreen
import com.nickmorus.pregnancyapp.screens.main.more.MoreScreen
import com.nickmorus.pregnancyapp.screens.main.today.TodayScreen
import com.nickmorus.pregnancyapp.screens.main.today.TodayViewModel
import com.nickmorus.pregnancyapp.utils.scaffoldbars.BottomBar


@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    onArticleClicked: (ArticleMeta) -> Unit,//статья
    onCounterContractions: () -> Unit,
    onMotionCounter:()-> Unit,
    onClickBodyWeight:()->Unit,
    viewModel: TodayViewModel,
    onGraphClick:()->Unit,
    onClickedArticle:(ArticleMeta)-> Unit

){
    val navController = rememberNavController()
    val title = remember { mutableStateOf("Title") }
    Scaffold(
        modifier = modifier.fillMaxSize(),
       // snackbarHost = { AdvertisementBox() },
        bottomBar = {
            BottomBar(navController = navController)
        },
    ){ scaffoldPadding ->
        NavHost(
            modifier = Modifier.padding(scaffoldPadding),
            navController = navController,
            startDestination = MainScreen.Today.route,
            builder = {
                composable(MainScreen.Today.route){
                    title.value = stringResource(id = R.string.main_page_header)
                    TodayScreen(
                        navController = navController,
                        onClickBodyWeight = {onClickBodyWeight()},
                        viewModelToday = viewModel,
                        onArticleClicked =  onClickedArticle

                    )
                }
                composable(MainScreen.More.route){
                    title.value = stringResource(id = R.string.exercises_header)
                    MoreScreen(
                        onMotionCounter ={onMotionCounter()},
                        onCounterContractions = {onCounterContractions()},
                        onGraphClick = {onGraphClick()}
                    )
                }
                composable(MainScreen.TopicsAndArticles.route){
                    TopicsAndArticlesScreen(
                        onArticleClicked = {
                            onArticleClicked(it)
                        }
                        )

                }


                composable(MainScreen.Calendar.route){
                    title.value = stringResource(id = R.string.notes_header)
                   CalendarScreen()
                }

            }
        )
    }
}

//@Composable
//fun AdvertisementBox() {
//    Box(modifier = Modifier
//        .fillMaxWidth()
//        .height(60.dp)
//        .background(colorResource(id = R.color.light_grey)),
//        contentAlignment = Alignment.Center)
//    {
//        Text(text = "Реклама",
//            textAlign = TextAlign.Center)
//    }
//}
