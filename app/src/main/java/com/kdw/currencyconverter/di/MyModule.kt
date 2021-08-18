package com.kdw.currencyconverter.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

private const val BASE_URL = "https://api.getgeoapi.com/v2/currency/convert"

@Module
@InstallIn(ApplicationComponent::class)
class MyModule {

    @Provides
    fun gson(): Gson = GsonBuilder().setLenient().create()


}