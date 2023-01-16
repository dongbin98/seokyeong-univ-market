package com.dbsh.skumarket.repository.retrofit

import com.dbsh.skumarket.api.SkuAuthService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class RetrofitClient {
    private var skuAuthService : SkuAuthService
    private var retrofit : Retrofit

    // SeoKyeong Portal Login Url
    private val baseUrl : String = "https://sportal.skuniv.ac.kr/sportal/"

    init {
        // 클라이언트 초기화
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getUnsafeOkHttpClient().build())
            .build()
        skuAuthService = retrofit.create(SkuAuthService::class.java)
    }

    // SSL Auth Avoid
    private fun getUnsafeOkHttpClient() : OkHttpClient.Builder {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {

            }

            override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {

            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())

        val sslSocketFactory = sslContext.socketFactory

        var builder = OkHttpClient.Builder()
        builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        builder.hostnameVerifier { _, _ -> true}

        return builder
    }

    fun getLoginService() : SkuAuthService {
        return skuAuthService
    }
}