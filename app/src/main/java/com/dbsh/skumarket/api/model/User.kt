package com.dbsh.skumarket.api.model

data class User(var name: String? = "", var uid: String? = "", var profileImage: String? = "") {
    constructor() : this("", "", "")
}