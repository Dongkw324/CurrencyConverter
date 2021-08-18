package com.kdw.currencyconverter.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kdw.currencyconverter.api.NewApi
import com.kdw.currencyconverter.api.NewApiSource
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

private const val BASE_URL = "https://api.getgeoapi.com/v2/currency/convert"

@Module
@InstallIn(ApplicationComponent::class)
class MyModule {

    @Provides
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson) : Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient.Builder().also { client ->
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            client.addInterceptor(logging)
            client.connectTimeout(100, TimeUnit.SECONDS)
            client.readTimeout(100, TimeUnit.SECONDS)
            client.writeTimeout(100, TimeUnit.SECONDS)
            client.protocols(Collections.singletonList(Protocol.HTTP_1_1))
        }.build())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    @Singleton
    fun provideNewApi(retrofit: Retrofit): NewApi = retrofit.create(NewApi::class.java)

    @Provides
    @Singleton
    fun provideNewApiSource(newApi: NewApi) : NewApiSource = NewApiSource(newApi)
}