package com.nickmorus.pregnancyapp

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import java.util.UUID

sealed class MainScreen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
){
    data object Today: MainScreen("today")
    data object More: MainScreen("more") //Exercises
    data object Calendar: MainScreen("calendar")
    data object TopicsAndArticles: MainScreen("topics_and_articles")
   // data object Topics: MainScreen("topics")
//    data object Articles: MainScreen(
//        route = "articles/{topicId}", //статьи
//        navArguments = listOf(navArgument("topicId") {
//            type = NavType.StringType
//        })){
//        fun createRoute(topicId: UUID) = "articles/$topicId"
//    }
}

sealed class AppScreen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
){
    data object Main : AppScreen("main")
    data object SourceInformation:MainScreen("source")
    data object UserAgreement : MainScreen("agreement")

    data object Article : AppScreen(
        route = "article/{contentId}",
        navArguments = listOf(navArgument("contentId") {
            type = NavType.StringType
        })
    ) {
        fun createRoute(contentId: UUID) = "article/$contentId"
    }
    data object CounterContractions:MainScreen("contractions")
    data object MotionCounter : MainScreen("motion")
    data object BodyWeight:MainScreen("body")

}
