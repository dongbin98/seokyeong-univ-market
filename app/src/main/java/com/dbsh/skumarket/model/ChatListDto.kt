package com.dbsh.skumarket.model

data class ChatListDto(val roomId: String, val lastMessage: String, val lastDate: String, var opponentName: String, val opponentId: String, var opponentImage: String?)
