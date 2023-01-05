package com.dbsh.skumarket.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseLogin {
    @SerializedName("access_token")
    @Expose
    private var accessToken: String? = null

    @SerializedName("USER_INFO")
    @Expose
    private var userInfo: ResponseUserInfo? = null

    @SerializedName("RTN_STATUS")
    @Expose
    private var rtnStatus: String? = null

    fun getRtnStatus() : String? { return rtnStatus }
    fun getUserInfo() : ResponseUserInfo? { return userInfo }
}