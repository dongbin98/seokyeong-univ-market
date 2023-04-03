package com.dbsh.skumarket.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SellingModelData (
    @SerializedName("uId") val uId : String?,
    @SerializedName("title") val title : String?,
    @SerializedName("posttime") val posttime: String?,
    @SerializedName("price") val price : String?,
    @SerializedName("contents") val contents : String?,
    @SerializedName("imageUrl") val imageUrl : String
) : Parcelable {
    // 파이어베이스에 클래스 단위로 올리려면 인자빈생성자가 필요함
    constructor() : this ("", "", "", "", "", "")
}
