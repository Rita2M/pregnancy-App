package com.nickmorus.pregnancyapp.utils
fun textDays(day: Int): String{
    return when {
        day % 10 == 1 && day % 100 != 11 -> "день"
        day % 10 in 2..4 && (day % 100 !in 12..14) -> "дня"
        else -> "дней"
    }
}



fun textSecond(seconds: Int): String {
    return when {
        seconds % 10 == 1 && seconds % 100 != 11 -> "секунда"
        seconds % 10 in 2..4 && (seconds % 100 !in 12..14) -> "секунды"
        else -> "секунд"
    }
}
fun textWeeks(weeks: Int): String {
    return when {
        weeks % 10 == 1 && weeks % 100 != 11 -> "неделя"
        weeks % 10 in 2..4 && weeks % 100 !in 12..14 -> "недели"
        else -> "недель"
    }
}
