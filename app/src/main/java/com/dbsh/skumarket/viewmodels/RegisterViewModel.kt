package com.dbsh.skumarket.viewmodels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dbsh.skumarket.model.User
import com.dbsh.skumarket.model.RequestLoginData
import com.dbsh.skumarket.model.ResponseLogin
import com.dbsh.skumarket.repository.retrofit.RetrofitClient
import com.dbsh.skumarket.api.SkuAuthService
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    lateinit var skuAuthService: SkuAuthService
    var authState: MutableLiveData<String> = MutableLiveData()
    var authData: MutableLiveData<ResponseLogin> = MutableLiveData()
    var registerState: MutableLiveData<String> = MutableLiveData()

    fun getSkuAuth(id: String, pw: String) {
        var retrofitClient = RetrofitClient()
        skuAuthService = retrofitClient.getLoginService()
        skuAuthService.getSkuAuth(RequestLoginData(id, pw, "password", "sku")).enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                // 응답 수신 성공
                if(response.isSuccessful) {
                    // 로그인 성공
                    if(response.body()?.rtnStatus.equals("S")) {
                        authData.value = response.body()
                        authState.value = "S"
                    }
                    // 로그인 실패
                    else {
                        authState.value = "F"
                    }
                } else {
                    // 응답 수신 실패
                    println(response)
                    authState.value = "R"
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                // 통신 실패
                authState.value = "C"
            }
        })
    }

    fun signUp(email: String, pw: String, name: String, stuId: String) {
        val auth = Firebase.auth
        val db = Firebase.database
        auth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener {
            if(it.isSuccessful) {
                try {
                    val user = auth.currentUser
                    val userId = user?.uid

                    // Realtimebase
                    db.getReference("User").child("users").child(userId.toString())
                        .setValue(User(name, stuId))
                    registerState.value = "S"
                } catch (e: Exception) {
                    e.printStackTrace()
                    registerState.value = "F"
                }
            } else if(it.exception?.message.isNullOrBlank()) {
                registerState.value = "F"
            }
        }
    }
}