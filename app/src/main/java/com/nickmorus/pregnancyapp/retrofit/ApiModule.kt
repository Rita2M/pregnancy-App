package com.nickmorus.pregnancyapp.retrofit

import com.nickmorus.pregnancyapp.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class
)
@Module
class ApiModule {
    companion object{
        private const val BASE_URL =  BuildConfig.BASE_URL
    }
    @Singleton
    @Provides
    fun providesLoggingHttp(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY

    }
    @Singleton
    @Provides
    fun provideOkHttp(
        logging : HttpLoggingInterceptor,
    ) : OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(logging)
        .build()
    @Singleton
    @Provides
    fun provideRetrofit(
okHttpClient: OkHttpClient
    ):Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .build()

    @Singleton
    @Provides
    fun provideApiService(
        retrofit: Retrofit
    ): ApiService = retrofit.create(ApiService::class.java)


}
