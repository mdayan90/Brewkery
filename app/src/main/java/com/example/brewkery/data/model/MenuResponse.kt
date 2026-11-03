package com.example.brewkery.data.model

import com.google.gson.annotations.SerializedName

data class MenuResponse(
    @SerializedName("meta")
    val meta: ShopMeta = ShopMeta(),
    @SerializedName("categories")
    val categories: List<Category> = emptyList(),
    @SerializedName("items")
    val items: List<MenuItem> = emptyList()
)
