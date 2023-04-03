package com.dbsh.skumarket.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SkuAuthResponse(
    @SerializedName("access_token")
    @Expose
    val accessToken: String?,

    @SerializedName("USER_INFO")
    @Expose
    val userInfo: UserInfoResponse?,

    @SerializedName("RTN_STATUS")
    @Expose
    val rtnStatus: String?,
)
