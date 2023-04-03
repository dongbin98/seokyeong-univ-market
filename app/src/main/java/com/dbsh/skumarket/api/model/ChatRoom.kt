package com.dbsh.skumarket.api.model

import kotlin.collections.HashMap
data class ChatRoom(val users: HashMap<String, ChatUser> = HashMap(), val messages: HashMap<String, Chat> = HashMap())
