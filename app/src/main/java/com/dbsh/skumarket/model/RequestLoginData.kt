package com.dbsh.skumarket.model

data class RequestLoginData (
    var username : String,
    var password : String,
    var grant_type : String,
    var userType : String
)