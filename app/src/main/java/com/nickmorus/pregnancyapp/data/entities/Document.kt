package com.nickmorus.pregnancyapp.data.entities

import java.util.UUID

data class Document(
    val id: UUID,
    val name : String,
    val content: String,
)
