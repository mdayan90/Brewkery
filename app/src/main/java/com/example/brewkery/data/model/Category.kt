package com.example.brewkery.data.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("item_count")
    val itemCount: Int
)
