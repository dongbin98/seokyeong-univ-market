package com.dbsh.skumarket.api

import android.annotation.SuppressLint
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

// 패키지 단위 함수

fun provideSkuAuthApi(): SkuAuthApi = Retrofit.Builder()
    .baseUrl("https://sportal.skuniv.ac.kr/sportal/")
    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .client(provideUnsafeOkHttpClient().build())
    .build()
    .create(SkuAuthApi::class.java)

fun provideUnsafeOkHttpClient(): OkHttpClient.Builder = OkHttpClient.Builder().run {
    sslSocketFactory(provideSslSocketFactory(), provideX509TrustManager()[0] as X509TrustManager)
    hostnameVerifier { _, _ -> true }
}

fun provideX509TrustManager(): Array<out TrustManager> {
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        @SuppressLint("TrustAllX509TrustManager")
        override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {
        }
        @SuppressLint("TrustAllX509TrustManager")
        override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {
        }
        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    })
    return trustAllCerts
}

fun provideSslSocketFactory(): SSLSocketFactory {
    val sslContext = SSLContext.getInstance("SSL").apply {
        init(null, provideX509TrustManager(), java.security.SecureRandom())
    }
    return sslContext.socketFactory
}