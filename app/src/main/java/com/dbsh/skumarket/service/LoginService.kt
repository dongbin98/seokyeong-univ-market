package com.dbsh.skumarket.service

import com.dbsh.skumarket.retrofit.RequestLoginData
import com.dbsh.skumarket.retrofit.ResponseLogin
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {
    @Headers("Accept: application/json", "content-type: application/json")
    @POST("auth/login.sku")
    fun getLogin(@Body requestLoginData : RequestLoginData): Call<ResponseLogin>
}