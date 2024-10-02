package com.nickmorus.pregnancyapp.data.repositories

import com.nickmorus.pregnancyapp.data.entities.ArticleContent
import com.nickmorus.pregnancyapp.error.ApiError
import com.nickmorus.pregnancyapp.retrofit.ApiService
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleContentRepository @Inject constructor(
    private val apiService: ApiService
){
    suspend fun getContent(contentId:UUID):ArticleContent{
        val response = apiService.getArticlesContentByContentId(contentId)
        if (!response.isSuccessful){
            throw ApiError(response.code(),response.message())
        }
        val body = response.body() ?:throw ApiError(response.code(),response.message())
        return body
    }
}
