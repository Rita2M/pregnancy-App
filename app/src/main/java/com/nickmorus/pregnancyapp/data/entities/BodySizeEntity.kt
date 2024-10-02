package com.nickmorus.pregnancyapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BodySizeEntity(
    @PrimaryKey val id : Int,
    val weightSizeBefore: Float? = null,
    val weight: Float ,
    val height: Float? = null ,
    val bmi: Float?= null,
    val weightGain: Float? = null,


)
