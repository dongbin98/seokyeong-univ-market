package com.dbsh.skumarket.model

import kotlin.collections.HashMap
data class ChatRoom(val users: HashMap<String, ChatUser> = HashMap(), val messages: HashMap<String, Chat> = HashMap())
