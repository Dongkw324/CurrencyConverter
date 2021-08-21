package com.kdw.currencyconverter.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import android.view.inputmethod.InputMethodManager

@Suppress("DEPRECATION")
object Helper {

    //안드로이드 키보드 숨기기 메소드, 키보드 자동 실행 막기
    fun hideKeyBoard(activity: Activity) {

        //InputMethodManager 객체를 선언하고, getSystemService(INPUT_METHOD_SERVICE)로 입력에 관한 manager를 반환받음
        //키보드 제어하기 위해 InputMethodManager 객체를 사용한다.
        val manager: InputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        //현재 Focus 되는 View 를 참조
        //hideSoftInputFromWindow : 키보드를 숨기는 메소드
        manager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)

    }

    //네트워크 연결 상태 확인하는 메소드
    fun isNetWordConnected(context: Context?) : Boolean {
        var result = false

        if(context == null)
            return false

        //ConnectivityManager 객체 선언
        val networkManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        //안드로이드 API 29 버전 이상과 그 미만 버전과의 분기처리
        //안드로이드 API 29 이상부터는 activeNetworkInfo 메소드가 deprecated 되었기 때문
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val networkCapabilities = networkManager.getNetworkCapabilities(networkManager.activeNetwork)
            if(networkCapabilities != null) {
                if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    result = true
                } else if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                    result = true
                } else if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                    result = true
                }
            }
        } else {
            val activeNetwork = networkManager.activeNetworkInfo
            if(activeNetwork != null && activeNetwork.isConnected) {
                result = true
            }
        }

        return result
    }

    //최상단 상태바 투명하게 만들기
    fun makeStatusTransparent(activity: Activity) {
        val status = activity.window.decorView

        //API 30부터는 systemUiVisibility 와 SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 가 deprecated 되었으므로 분기 처리
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            status.windowInsetsController?.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
            val w = activity.window
            w.statusBarColor = Color.TRANSPARENT
        } else
            status.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        val w = activity.window
        w.statusBarColor = Color.TRANSPARENT
    }
}