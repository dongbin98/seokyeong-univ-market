package com.dbsh.skumarket.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseUserInfo {
    // 학번
    @SerializedName("ID")
    @Expose
    private var id : String? = null

    // 이름
    @SerializedName("KOR_NAME")
    @Expose
    var korName: String? = null

    // 전화번호
    @SerializedName("PHONE_MOBILE")
    @Expose
    private var phoneMobile : String? = null

    // 단대
    @SerializedName("COL_NM")
    @Expose
    private var colNm : String? = null

    // 학과
    @SerializedName("TEAM_NM")
    @Expose
    private var teamNm : String? = null
}