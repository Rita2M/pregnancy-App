package com.nickmorus.pregnancyapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nickmorus.pregnancyapp.utils.information.EmojiType

@Entity
data class EmojiDayEntity(
    @PrimaryKey val date: String,
    val emojiType: EmojiType
)
