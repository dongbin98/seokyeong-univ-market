package com.dbsh.skumarket.model

data class Chat(val uid: String? = null, val message: String? = null, val time: String? = null, val name: String?) {
    constructor() : this("", "", "", "")
}