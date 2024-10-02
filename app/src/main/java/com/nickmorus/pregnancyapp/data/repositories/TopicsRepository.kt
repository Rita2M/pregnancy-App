package com.nickmorus.pregnancyapp.data.repositories

import com.nickmorus.pregnancyapp.data.entities.Topic
import com.nickmorus.pregnancyapp.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopicsRepository @Inject constructor(
    private val apiService: ApiService
) {

    val data: Flow<List<Topic>> = flow {
        try {
            val response = apiService.getTopics()
            if (response.isSuccessful) {
                emit(response.body() ?: emptyList())
            } else {
                throw Exception("Неуспешный запрос в getTopics")
            }
        } catch (e: Exception) {
            emit(emptyList())  // Возвращаем пустой список в случае ошибки

        }
    }
}
//interface TopicRepository{
//    val data :  Flow<List<Topic>>
//}

    // fun getAllTopics(callback: (List<Topic>) -> Unit)
//    {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://95.31.5.104")
//            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
//            .build()
//        val service = retrofit.create(TopicService::class.java)
//
//        val call: Call<List<Topic>> = service.getTopics()
//
//        call.enqueue(object : Callback<List<Topic>> {
//
//            override fun onResponse(call: Call<List<Topic>>, response: Response<List<Topic>>) {
//                if(response.isSuccessful){
//                    val topics: List<Topic> = response.body() as List<Topic>
//                    callback(topics)
//                }
//            }
//
//            override fun onFailure(call: Call<List<Topic>>, t: Throwable) {
//                Log.d("RETROFIT","Api call failure ${t.message}")
//            }
//        })
//    }
