package com.nickmorus.pregnancyapp.data.entities

import java.time.LocalDate

data class Cycle(
    val id: Int?,
    val startDate: LocalDate,
    val endDate: LocalDate,
)
