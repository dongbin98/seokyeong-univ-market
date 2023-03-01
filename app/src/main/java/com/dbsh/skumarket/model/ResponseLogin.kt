package com.dbsh.skumarket.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseLogin(
    @SerializedName("access_token")
    @Expose
    val accessToken: String?,

    @SerializedName("USER_INFO")
    @Expose
    val userInfo: ResponseUserInfo?,

    @SerializedName("RTN_STATUS")
    @Expose
    val rtnStatus: String?,
)
