package com.nickmorus.pregnancyapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PregnancyData")
data class PregnancyWeekDataEntity(
    @PrimaryKey val week: Int,
    val size: String?,
    val weight: String?,
    val fetalHR: String?,
    val hcg: String?,
    val description: String?,
    val motherHR: String?,
    val criticalValue: String?
)
