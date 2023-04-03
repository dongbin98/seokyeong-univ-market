package com.dbsh.skumarket.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dbsh.skumarket.api.SkuAuthApi
import com.dbsh.skumarket.api.model.SkuAuth
import com.dbsh.skumarket.api.model.SkuAuthResponse
import com.dbsh.skumarket.api.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val api: SkuAuthApi) : ViewModel() {
    var authState: MutableLiveData<String> = MutableLiveData()
    var authData: MutableLiveData<SkuAuthResponse> = MutableLiveData()
    var registerState: MutableLiveData<String> = MutableLiveData()

    fun getSkuAuth(id: String, pw: String) {
        api.getSkuAuth(SkuAuth(id, pw, "password", "sku")).enqueue(object : Callback<SkuAuthResponse> {
            override fun onResponse(call: Call<SkuAuthResponse>, response: Response<SkuAuthResponse>) {
                // 응답 수신 성공
                if (response.isSuccessful) {
                    // 로그인 성공
                    if (response.body()?.rtnStatus.equals("S")) {
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

            override fun onFailure(call: Call<SkuAuthResponse>, t: Throwable) {
                // 통신 실패
                authState.value = "C"
            }
        })
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
