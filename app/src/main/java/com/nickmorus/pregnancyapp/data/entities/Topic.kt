package com.nickmorus.pregnancyapp.data.entities
import com.google.firebase.database.PropertyName
import java.util.UUID

data class Topic(
    @PropertyName("Id")
    val id: UUID,
    @PropertyName("Title")
    val title: String = "",
    @PropertyName("Image")
    var image: String?
)
