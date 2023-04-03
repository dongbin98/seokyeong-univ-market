package com.dbsh.skumarket.api.model

data class Chat(val uid: String, val message: String?, val imageUrl: String?, val time: String, val name: String) {
    constructor() : this("", "", "", "", "")
}
