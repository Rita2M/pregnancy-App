package com.nickmorus.pregnancyapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NotesEntity(
    @PrimaryKey
    val noteDate : String,
    val note: String
)
