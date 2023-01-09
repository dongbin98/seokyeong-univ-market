package com.dbsh.skumarket.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseUserInfo (
    // 학번
    @SerializedName("ID")
    @Expose
    var id : String?,

    // 이름
    @SerializedName("KOR_NAME")
    @Expose

    var korName: String?,


    // 전화번호
    @SerializedName("PHONE_MOBILE")
    @Expose
    var phoneMobile : String?,

    // 단대
    @SerializedName("COL_NM")
    @Expose
    var colNm : String?,

    // 학과
    @SerializedName("TEAM_NM")
    @Expose
    var teamNm : String?
)