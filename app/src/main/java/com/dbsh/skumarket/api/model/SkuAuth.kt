package com.dbsh.skumarket.api.model

data class SkuAuth(
    val username: String,
    val password: String,
    val grant_type: String,
    val userType: String,
)
