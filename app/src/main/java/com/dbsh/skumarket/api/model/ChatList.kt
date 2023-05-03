package com.dbsh.skumarket.api.model

data class ChatList(val roomId: String, val lastMessage: String, val lastDate: String, var opponentName: String, val opponentId: String, var opponentImage: String?)
