package com.nickmorus.pregnancyapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity
data class MotionRecordEntity(
    @PrimaryKey val id: LocalDate,
    val movementTimes: List<LocalTime> = emptyList()

)
