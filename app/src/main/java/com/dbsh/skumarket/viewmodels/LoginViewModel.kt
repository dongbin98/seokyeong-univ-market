package com.dbsh.skumarket.viewmodels

import android.widget.Toast
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
                // 통신 성공
                if(response.isSuccessful) {
                    // 로그인 성공
                    if(response.body()?.equals("S") == true) {
                        loginData.value = response.body()
                        loginState.value = "s"
                    }
                    // 로그인 실패
                    else {
                        loginState.value = "f"

                    }
                } else {
                    // 통신 실패
                    loginState.value = "f"
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                loginState.value = "n"
            }
        })
    }
}