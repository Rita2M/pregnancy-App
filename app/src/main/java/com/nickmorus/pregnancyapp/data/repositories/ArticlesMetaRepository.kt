package com.nickmorus.pregnancyapp.data.repositories

import com.nickmorus.pregnancyapp.data.entities.ArticleMeta
import com.nickmorus.pregnancyapp.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticlesMetaRepository @Inject constructor(
    private val apiService: ApiService
) {

    fun getArticlesMetaById(topicId: UUID) :Flow<List<ArticleMeta>> {
        val data: Flow<List<ArticleMeta>> = flow {
            try {
                val response = apiService.getArticlesMetaByTopicId(topicId)
                if (response.isSuccessful) {
                    emit(response.body() ?: emptyList())
                } else {
                    throw Exception("Неуспешный запрос в getArticlesMeta")
                }
            } catch (e: Exception) {
                emit(emptyList())  // Возвращаем пустой список в случае ошибки
                throw Exception("Ошибка в getArticlesMeta: ${e.message}")
            }
        }
        return data
    }
}

//    fun getArticlesMetaByTopicId(topicId: UUID, callback: (List<ArticleMeta>) -> Unit)
//    {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://95.31.5.104")
//            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
//            .build()
//        val service = retrofit.create(ArticlesMetaService::class.java)
//
//        val call: Call<List<ArticleMeta>> = service.getArticlesMetaById(topicId)
//
//        call.enqueue(object : Callback<List<ArticleMeta>> {
//
//            override fun onResponse(call: Call<List<ArticleMeta>>, response: Response<List<ArticleMeta>>) {
//                if(response.isSuccessful){
//                    val topics: List<ArticleMeta> = response.body() as List<ArticleMeta>
//                    println(response.body())
//                    //callback(topics)
//                }
//            }
//
//            override fun onFailure(call: Call<List<ArticleMeta>>, t: Throwable) {
//                Log.d("RETROFIT","Api call failure ${t.message}")
//            }
//        })
//    }
