package com.dbsh.skumarket.model

data class RequestLoginData(
    val username: String,
    val password: String,
    val grant_type: String,
    val userType: String,
)
