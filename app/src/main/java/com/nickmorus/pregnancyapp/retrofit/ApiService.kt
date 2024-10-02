package com.nickmorus.pregnancyapp.retrofit

import com.nickmorus.pregnancyapp.data.entities.ArticleContent
import com.nickmorus.pregnancyapp.data.entities.ArticleMeta
import com.nickmorus.pregnancyapp.data.entities.Document
import com.nickmorus.pregnancyapp.data.entities.Topic
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.UUID

interface ApiService {
    //topic
    @GET("/api/topics")
   suspend fun getTopics(): Response<List<Topic>>
    @GET("/api/topics/{id}")
   suspend fun getTopicById(@Path("id") id: UUID): Response<Topic>
    @GET("/api/articles/topic/{id}")
    suspend fun getArticlesMetaByTopicId(@Path("id")topicId:UUID): Response<List<ArticleMeta>>
    @GET("/api/articles/content/{id}")
    suspend fun getArticlesContentByContentId(@Path("id")contentId:UUID):Response<ArticleContent>
    @GET("/api/documents")
    suspend fun getDocuments(): Response<List<Document>>

}
