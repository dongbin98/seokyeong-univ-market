package com.dbsh.skumarket.api.model

import kotlin.collections.HashMap
data class Post(var uid: String, var title: String, var time: String, var price: String, var content: String, var images: MutableMap<String, String> = HashMap()) {
    constructor() : this("", "", "", "", "", mutableMapOf<String, String>() as HashMap<String, String>)
}