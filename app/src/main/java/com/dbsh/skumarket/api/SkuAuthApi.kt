package com.dbsh.skumarket.api

import com.dbsh.skumarket.api.model.SkuAuth
import com.dbsh.skumarket.api.model.SkuAuthResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SkuAuthApi {
    @Headers("Accept: application/json", "content-type: application/json")
    @POST("auth2/login.sku")
    fun getSkuAuth(
        @Body skuAuth: SkuAuth
    ): Observable<SkuAuthResponse>
}
