package com.dbsh.skumarket.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbsh.skumarket.api.SkuAuthApi
import com.dbsh.skumarket.api.model.SkuAuth
import com.dbsh.skumarket.api.model.UserInfoResponse
import com.dbsh.skumarket.repository.FirebaseRepository
import com.dbsh.skumarket.util.Resource
import com.google.firebase.auth.AuthResult
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RegisterViewModel(private val api: SkuAuthApi) : ViewModel() {

    private var _userRegistrationStatus =  MutableLiveData<Resource<AuthResult>>()
    var userRegistrationStatus: LiveData<Resource<AuthResult>> = _userRegistrationStatus
    private val repository = FirebaseRepository()

    val result: BehaviorSubject<UserInfoResponse> = BehaviorSubject.create()
    val rtnStatus: BehaviorSubject<String> = BehaviorSubject.create()
    val message: BehaviorSubject<String> = BehaviorSubject.create()
    val isLoading: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    fun getSkuAuth(id: String, pw: String) {
        lateinit var check: Resource<Boolean>
        runBlocking {
            check = repository.checkRegister(id)
            println(check)
        }
        if(check is Resource.Success) {
            println("auth")
            val skuAuthObservable = api.getSkuAuth(SkuAuth(id, pw, "password", "sku"))

            skuAuthObservable
                .doOnSubscribe { isLoading.onNext(true)  }
                .doOnTerminate { isLoading.onNext(false) }
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe ({ auth ->
                    auth.userInfo?.let { result.onNext(it) }
                    auth.rtnStatus?.let { rtnStatus.onNext(it) }
                }) {
                    message.onNext(it.message ?: "Unexpected error")
                }
        } else {
            message.onNext("이미 가입된 학생입니다.")
        }
    }

    fun createUser(email: String, pw: String, name: String, stuId: String) {
        _userRegistrationStatus.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.Main) {
            val loginResult = repository.createUser(email, pw, name, stuId)
            _userRegistrationStatus.postValue(loginResult)
        }
    }
}
