package com.dbsh.skumarket.rx

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

class AutoClearedDisposable(
    private val lifecycleOwner: AppCompatActivity, // 생명주기 참조 액티비티
    private val alwaysClearOnStop: Boolean = true, // onStop 호출 시 디스포저블 객체 해제 여부
    private val compositeDisposable: CompositeDisposable = CompositeDisposable(),
) : LifecycleEventObserver {

    fun add(disposable: Disposable) {
        // check()을 통해 Lifecycle.State.isAtLeast() 반환 값이 참인지 체크 거짓이면 IllegalStateException
        // isAtLeast() : 현재 상태가 특정 상태 이후의 상태인지 여부 반환
        check(lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED))

        // 검증 절차 통과시에만 디스포저블 추가
        compositeDisposable.add(disposable)
    }

    // @OnLifecycleEvent is deprecated : LifecycleObserver >> LifecycleEventObserver
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_STOP) {
            if (alwaysClearOnStop && !lifecycleOwner.isFinishing)
                return
            compositeDisposable.clear()
        } else if (event == Lifecycle.Event.ON_DESTROY) {
            compositeDisposable.clear()
            lifecycleOwner.lifecycle.removeObserver(this)
        }
    }

}