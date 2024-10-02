package com.nickmorus.pregnancyapp.data.entities

import com.google.firebase.database.PropertyName
import java.util.UUID

data class ArticleContent(
    @PropertyName("Id")
    val id: UUID,
    @PropertyName("Content")
    val content: String = "",
    @PropertyName("Image")
    val image: String?,
)
