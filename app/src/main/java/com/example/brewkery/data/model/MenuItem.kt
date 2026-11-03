package com.example.brewkery.data.model

import com.google.gson.annotations.SerializedName

data class MenuItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("category_id")
    val categoryId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("tagline")
    val tagline: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("base_price")
    val basePrice: Double,
    @SerializedName("rating")
    val rating: Double,
    @SerializedName("review_count")
    val reviewCount: Int,
    @SerializedName("prep_time")
    val prepTime: String,
    @SerializedName("calories")
    val calories: Int,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("badge")
    val badge: String? = null,
    @SerializedName("ingredients")
    val ingredients: List<String> = emptyList(),
    @SerializedName("customizations")
    val customizations: Customizations = Customizations()
)
