package com.dbsh.skumarket.api.model

import kotlin.collections.HashMap
data class Post(val uid: String, val title: String, val time: String, val price: String, val content: String, val images: HashMap<Int, String> = HashMap())
