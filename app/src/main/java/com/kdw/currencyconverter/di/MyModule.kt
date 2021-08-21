package com.kdw.currencyconverter.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kdw.currencyconverter.BuildConfig
import com.kdw.currencyconverter.api.NewApiService
import com.kdw.currencyconverter.api.NewApiSource
import com.kdw.currencyconverter.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class MyModule {

    @Provides
    fun provideUrl() = Constants.BASE_URL

    @Provides
    fun provideGson() : Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(OkHttpClient.Builder().also { client ->
            if(BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                client.addInterceptor(logging)
                client.connectTimeout(120, TimeUnit.SECONDS)
                client.readTimeout(120, TimeUnit.SECONDS)
                client.protocols(Collections.singletonList(Protocol.HTTP_1_1))
            }
        }.build()
        )
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) : NewApiService = retrofit.create(NewApiService::class.java)

    @Provides
    @Singleton
    fun provideDataSource(apiService: NewApiService) = NewApiSource(apiService)
}