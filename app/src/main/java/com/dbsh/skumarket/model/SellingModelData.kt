package com.dbsh.skumarket.model

data class SellingModelData (
    val uId : String,
    val title : String,
    val posttime: Long,
    val price : String,
    val imageUrl : String
) {
    // 파이어베이스에 클래스 단위로 올리려면 인자빈생성자가 필요함
    constructor() : this ("", "", 0, "", "")
}
