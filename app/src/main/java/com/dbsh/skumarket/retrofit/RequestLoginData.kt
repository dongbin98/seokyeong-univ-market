package com.dbsh.skumarket.retrofit

class RequestLoginData(username: String, password: String, grant_type: String, userType: String) {
    lateinit var username : String
    lateinit var password : String
    lateinit var grant_type : String
    lateinit var userType : String
}