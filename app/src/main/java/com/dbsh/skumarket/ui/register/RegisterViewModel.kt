package com.dbsh.skumarket.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dbsh.skumarket.api.SkuAuthApi
import com.dbsh.skumarket.api.model.SkuAuth
import com.dbsh.skumarket.api.model.SkuAuthResponse
import com.dbsh.skumarket.api.model.User
import com.dbsh.skumarket.api.model.UserInfoResponse
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.reflect.typeOf

class RegisterViewModel(private val api: SkuAuthApi) : ViewModel() {
    var registerState: MutableLiveData<String> = MutableLiveData()

    val result: BehaviorSubject<UserInfoResponse> = BehaviorSubject.create()
    val rtnStatus: BehaviorSubject<String> = BehaviorSubject.create()
    val message: BehaviorSubject<String> = BehaviorSubject.create()
    val isLoading: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    fun getSkuAuth(id: String, pw: String): Disposable {
        val skuAuthObservable = api.getSkuAuth(SkuAuth(id, pw, "password", "sku"))

        return skuAuthObservable
            .doOnSubscribe { isLoading.onNext(true)  }
            .doOnTerminate { isLoading.onNext(false) }
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe ({ auth ->
                auth.userInfo?.let { result.onNext(it) }
                auth.rtnStatus?.let { rtnStatus.onNext(it) }
            }) {
                message.onNext(it.message ?: "Unexpected error")
            }
    }

    fun signUp(email: String, pw: String, name: String, stuId: String) {
        val auth = Firebase.auth
        val db = Firebase.database
        auth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener {
            if (it.isSuccessful) {
                try {
                    val user = auth.currentUser
                    val userId = user?.uid

                    // RealtimeDatabase
                    db.getReference("User").child("users").child(userId.toString())
                        .setValue(User(name, stuId))
                    registerState.value = "S"
                } catch (e: Exception) {
                    e.printStackTrace()
                    registerState.value = "F"
                }
            } else if (it.exception?.message.isNullOrBlank()) {
                registerState.value = "F"
            }
        }
    }
}
