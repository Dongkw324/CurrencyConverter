package com.kdw.currencyconverter.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Hilt를 사용하는 모든 앱은 @HiltAndroidApp Annotation 을 Application Class에 추가
// 이를 통해 의존성 주입의 시작점을 지정
// Application의 생명 주기를 따르며 컴파일 단계 시 DI에 필요한 구성요소들을 초기화
@HiltAndroidApp
class MyApplication : Application() {
}