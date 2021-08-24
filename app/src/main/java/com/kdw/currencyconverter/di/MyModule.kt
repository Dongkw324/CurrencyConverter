package com.kdw.currencyconverter.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kdw.currencyconverter.BuildConfig
import com.kdw.currencyconverter.api.NewApiService
import com.kdw.currencyconverter.api.NewApiSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

const val BASE_URL = "https://api.getgeoapi.com/api/v2/currency/"

// 외부 라이브러리를 사용하는 경우 ex) Retrofit, OKHttpClient, Room Database 개발자는 생성자를 만들고 삽입할 수 없다.
// 이 경우 Hilt 모듈 이용해서 의존성 생성 가능
// Hilt 모듈은 @Module Annotation이 지정된 클래스
// 이 모듈은 의존성 인스턴스를 제공하는 방법을 Hilt에 알려주는 역할을 수행
// 이 module에 @InstallIn(component) Annptation을 지정해서 어떤 Component에 install할 지를 지정
@Module
@InstallIn(SingletonComponent::class)
class MyModule {

    // @Provides를 사용해서 의존성을 생성
    // 클래스가 외부 라이브러리를 사용하는 경우 또는 Builder 패턴으로 객체 생성하는 경우 개발자가 생성자 삽입할 수 없음
    // @Provides Annotation을 이용해서 의존성 생성을 할 수 있다.
    @Provides
    fun provideGson() : Gson = GsonBuilder().setLenient().create()

    // 이미 생성된 객체를 다시 생성하지 않고 재사용하는 방법은 @Singleton Annotation을 사용하는 것이다.
    // Gson: json 구조를 띄는 직렬화된 데이터를 Object로 역직렬화, 또는 그 반대로 설정할 수 있게 하는 기법
    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL) //baseUrl 등록
        .client(OkHttpClient.Builder().also { client -> //okhttp는 Http를 간편하고 효율적으로 쓸 수 있게 도와주는 클라이언트
            if(BuildConfig.DEBUG) { // 입력값이나서버 응답을 로그로 확인, 헤더 값을 편하게 입력하고자 하는 목적
                val logging = HttpLoggingInterceptor() //Http 통신에 대한 로그 출력, 디버그 용이
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                client.addInterceptor(logging)
                client.connectTimeout(120, TimeUnit.SECONDS) //요청 시작 후 서버와의 TCP handshake가 완료되기까지 지속되는 시간
                client.readTimeout(120, TimeUnit.SECONDS) // 모든 바이트가 전송되는 속도를 감시
                client.protocols(Collections.singletonList(Protocol.HTTP_1_1))
            }
        }.build()
        )
        .addConverterFactory(ScalarsConverterFactory.create()) //Response를 String 형태로 받을 때 사용
        .addConverterFactory(GsonConverterFactory.create(gson)) // JSON을 변환해줄 Gson 변환기 등록
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) : NewApiService = retrofit.create(NewApiService::class.java)

    @Provides
    @Singleton
    fun provideDataSource(apiService: NewApiService) = NewApiSource(apiService)
}