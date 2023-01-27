package com.dbsh.skumarket.model

data class Chat(val uid: String, val message: String?, val imageUrl: String?, val time: String, val name: String, val read: Boolean) {
    constructor(): this("", "", "", "", "", false)

}