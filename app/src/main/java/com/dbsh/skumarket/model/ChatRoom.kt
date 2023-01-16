package com.dbsh.skumarket.model

import kotlin.collections.HashMap
data class ChatRoom(val users: HashMap<String, Boolean> = HashMap(), val messages: HashMap<String, Chat> = HashMap(), var otherOne: String){
    constructor() : this(otherOne = "") {
    }
}
