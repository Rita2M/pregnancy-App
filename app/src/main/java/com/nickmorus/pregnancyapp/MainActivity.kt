package com.nickmorus.pregnancyapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nickmorus.pregnancyapp.screens.article.ContentScreen
import com.nickmorus.pregnancyapp.screens.draw.AboutApp
import com.nickmorus.pregnancyapp.screens.draw.SourceInformationScreen
import com.nickmorus.pregnancyapp.screens.draw.UserAgreementScreen
import com.nickmorus.pregnancyapp.screens.draw.openTelegramChannel
import com.nickmorus.pregnancyapp.screens.draw.sendMail
import com.nickmorus.pregnancyapp.screens.draw.shareAppPlayStoreUrl
import com.nickmorus.pregnancyapp.screens.main.AppScaffold
import com.nickmorus.pregnancyapp.screens.main.more.ContractionTimerScreen
import com.nickmorus.pregnancyapp.screens.main.more.InfoBottomSheet
import com.nickmorus.pregnancyapp.screens.main.more.MotionCounterScreen
import com.nickmorus.pregnancyapp.screens.main.today.BodyWeightScreen
import com.nickmorus.pregnancyapp.screens.main.today.TodayViewModel
import com.nickmorus.pregnancyapp.ui.theme.MyApplicationTheme
import com.nickmorus.pregnancyapp.utils.StartupCarousel
import com.nickmorus.pregnancyapp.utils.drawer.AppDrawer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appSettings: AppSettings
//    @Inject lateinit var articlesRepository: ArticlesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isInternetAvailable()) {
            Toast.makeText(this, "Пожалуйста, подключитесь к интернету ", Toast.LENGTH_LONG).show()
        }
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showCarousel by remember { mutableStateOf(appSettings.appStartedFirstTime) }
                    if (showCarousel) {
                        StartupCarousel {
                            showCarousel = false
                            appSettings.setAppStartedFirstTime(false)
                        }
                    } else {
                        MainScreen(
                            settings = appSettings
                        )
                    }
                }
            }
        }
    }
    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork?.let {
            connectivityManager.getNetworkCapabilities(it)
        }
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}


@Composable
fun MainScreen(
    settings: AppSettings,

    ) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var showAboutApp by remember { mutableStateOf(false) }
    var showInfo by remember{ mutableStateOf(false) }
    if (showAboutApp) {
        AboutApp(onDismiss = { showAboutApp = false },
            onSourceInformationClicked = { run {  navController.navigate(AppScreen.SourceInformation.route)
                showAboutApp = false }  },
            onUserAgreementClicked = { run {navController.navigate(AppScreen.UserAgreement.route)
                showAboutApp = false}}
        )
    }
    if(showInfo){
        InfoBottomSheet(onDismiss = { showInfo = false })
    }
    val viewModel: TodayViewModel = hiltViewModel()
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = AppScreen.Main.route
    ) {
        composable(route = AppScreen.Main.route) {
            AppDrawer(
                drawerState = drawerState,
                onAboutAppClicked = { showAboutApp = true },
                onWriteUsClicked = {context.sendMail("alekcandr-cs@ya.ru ","subject")},
                onInviteFriendClicked = {
                    shareAppPlayStoreUrl(context)
                },
                onTelegramClicked = {context.openTelegramChannel("https://t.me/app_calendar")}
            ) {

                AppScaffold(
                    viewModel = viewModel,
                    onArticleClicked = {
                        navController.navigate(
                            AppScreen.Article.createRoute(
                                contentId = it.contentId
                            )
                        )
                    },
                    onCounterContractions ={ navController.navigate(AppScreen.CounterContractions.route)},
                    onMotionCounter = {navController.navigate(AppScreen.MotionCounter.route)},
                    onClickBodyWeight = {navController.navigate(AppScreen.BodyWeight.route)},
                    onGraphClick = {navController.navigate(AppScreen.BodyWeight.route)},
                    onClickedArticle = {navController.navigate(AppScreen.Article.createRoute(
                        contentId = it.contentId
                    ))}
                )
            }
        }
        composable(route = AppScreen.UserAgreement.route) {
            UserAgreementScreen(navController) // Здесь реализуется экран пользовательского соглашения
        }
        composable(route = AppScreen.SourceInformation.route) {
            SourceInformationScreen(navController) // Здесь реализуется экран информации об источниках
        }
        composable(route = AppScreen.Article.route,
             arguments = AppScreen.Article.navArguments)
        {
            ContentScreen(
                onBackClick = {navController.navigateUp()}


            )
        }
        composable(AppScreen.CounterContractions.route){
            ContractionTimerScreen(onBackClick = {navController.navigateUp()})
        }
        composable(AppScreen.MotionCounter.route){
            MotionCounterScreen(onInformationClick = {showAboutApp = true}, onBackClick = {navController.navigateUp()})
        }
        composable(AppScreen.BodyWeight.route){
            BodyWeightScreen(viewModelToday = viewModel, onBackPressed = {navController.navigateUp()})
        }


    }
}
