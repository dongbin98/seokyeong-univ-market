package com.dbsh.skumarket.api

import com.dbsh.skumarket.model.RequestLoginData
import com.dbsh.skumarket.model.ResponseLogin
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SkuAuthService {
    @Headers("Accept: application/json", "content-type: application/json")
    @POST("auth2/login.sku")
    fun getSkuAuth(@Body requestLoginData: RequestLoginData): Call<ResponseLogin>
}
