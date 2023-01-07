package com.dbsh.skumarket.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dbsh.skumarket.retrofit.RequestLoginData
import com.dbsh.skumarket.retrofit.ResponseLogin
import com.dbsh.skumarket.retrofit.RetrofitClient
import com.dbsh.skumarket.service.LoginService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    lateinit var loginService: LoginService
    var loginState : MutableLiveData<String> = MutableLiveData()
    var loginData : MutableLiveData<ResponseLogin> = MutableLiveData()

    fun getUserData(id : String, pw : String) {
        var retrofitClient = RetrofitClient()
        loginService = retrofitClient.getLoginService()
        loginService.getLogin(RequestLoginData(id, pw, "password", "sku")).enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                // 응답 수신 성공
                if(response.isSuccessful) {
                    // 로그인 성공
                    if(response.body()?.rtnStatus.equals("S")) {
                        loginData.value = response.body()
                        loginState.value = "로그인 성공"
                    }
                    // 로그인 실패
                    else {
                        loginState.value = "로그인 실패"
                    }
                } else {
                    // 응답 수신 실패
                    println(response)
                    loginState.value = "응답 실패"
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                // 통신 실패
                loginState.value = "통신 실패"
            }
        })
    }
}