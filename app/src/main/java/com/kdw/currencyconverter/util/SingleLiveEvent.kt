package com.kdw.currencyconverter.util

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent <T>: MutableLiveData<T>() {

    /*
    멀티스레딩 환경에서 동시성을 보장하는 AtomicBoolean
    false로 초기화
     */
    private val mPending = AtomicBoolean(false)

    /*
    View(Activity, Fragment 등)이 활성화 상태가 되거나 setValue값이 바뀌었을 때 호출되는 observe 함수
    mPending.compareAndSet(true, false): mPending 변수가 true라면 if문 안의 로직을 처리하고 false로 바꾼다

    아래의 setValue 함수를 통해서만 mPending값이 true로 바뀐다
    Configuration Changed가 일어나도 mPending 값은 false 이므로 observe가 데이터를 관찰하지 않는다.
     */
    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }

        // Observe the internal MutableLiveData
        super.observe(owner, Observer<T> { t ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }
    /*
    LiveData로서 가지고 있는 데이터의 값을 변경하는 경우
    mPending(AtomicBoolean)의 변수는 true로 바뀌어 observer 내의 if문 처리 가능하게 함
     */

    @MainThread
    override fun setValue(t: T?) {
        mPending.set(true)
        super.setValue(t)
    }

    // 데이터의 속성을 지정하지 않아도 call 만으로 setValue 호출 가능
    @MainThread
    fun call() {
        value = null
    }

    companion object {

        private val TAG = "SingleLiveEvent"
    }
}