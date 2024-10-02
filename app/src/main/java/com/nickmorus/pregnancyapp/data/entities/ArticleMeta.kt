package com.nickmorus.pregnancyapp.data.entities


import java.util.UUID

data class ArticleMeta(
    val id: UUID,
    val topicId: UUID,
    val contentId: UUID,
    val title: String,
    val accessId:UUID?,
    val image: String?,
    val isForPregnant: Boolean,
    val isPremium: Boolean,

)
