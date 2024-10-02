package com.nickmorus.pregnancyapp.converters

import androidx.room.TypeConverter
import com.nickmorus.pregnancyapp.utils.information.EmojiType
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object LocalDateTimeListConverter {
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")


    // Конвертер для LocalDate
    @TypeConverter
    fun fromLocalDate(value: Long?): LocalDate? {
        return value?.let { Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate() }
    }

    @TypeConverter
    fun localDateToTimestamp(date: LocalDate?): Long? {
        return date?.atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
    }

    // Конвертер для LocalDateTime
    @TypeConverter
    fun fromLocalTime(time: String?): LocalTime? {
        return time?.let { LocalTime.parse(it, timeFormatter) }
    }

    @TypeConverter
    fun localTimeToTimestamp(time: LocalTime?):String? {
        return time?.format(timeFormatter)
    }

    @TypeConverter
    fun fromLocalDateTimeList(time: List<LocalTime>?): String? {
        return time?.joinToString(",") {
            it.format(timeFormatter)
        }
    }

    @TypeConverter
    fun toLocalTimeList(data: String?): List<LocalTime>? {
        return if (data.isNullOrEmpty()) {
            emptyList()  // Возвращаем пустой список, если строка пуста
        } else {
            data.split(",").map { LocalTime.parse(it, timeFormatter) }
        }
    }
    @TypeConverter
    fun fromEmojiType(emojiType: EmojiType): String {
        return emojiType.name
    }

    @TypeConverter
    fun toEmojiType(emojiName: String): EmojiType {
        return EmojiType.valueOf(emojiName)
    }
    @TypeConverter
    fun fromWeightList(weight: List<Int>): String {
        return weight.joinToString(separator = ",")
    }

    @TypeConverter
    fun toWeightList(weightString: String): List<Int> {
        return weightString.split(",").map { it.toInt() }
    }
    }
