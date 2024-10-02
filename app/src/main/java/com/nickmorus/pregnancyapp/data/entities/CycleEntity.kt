package com.nickmorus.pregnancyapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CycleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val startDate: String,
    val endDate: String?= null,
    val duration: Int? = null,
    val cycleLength: Int? = null,
    val pregnancy: Boolean = false
)
