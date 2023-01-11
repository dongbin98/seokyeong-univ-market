package com.dbsh.skumarket.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseUserInfo (
    // 학번
    @SerializedName("ID")
    @Expose
    val id : String?,

    // 이름
    @SerializedName("KOR_NAME")
    @Expose
    val korName: String?,

    // 전화번호
    @SerializedName("PHONE_MOBILE")
    @Expose
    val phoneMobile : String?,

    // 단대
    @SerializedName("COL_NM")
    @Expose
    val colNm : String?,

    // 학과
    @SerializedName("TEAM_NM")
    @Expose
    val teamNm : String?
)