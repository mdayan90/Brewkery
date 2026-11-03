package com.example.brewkery.data.model

import com.google.gson.annotations.SerializedName

data class Customizations(
    @SerializedName("sizes")
    val sizes: List<SizeOption> = emptyList(),
    @SerializedName("sugar_levels")
    val sugarLevels: List<String> = emptyList(),
    @SerializedName("milk_options")
    val milkOptions: List<MilkOption> = emptyList()
)

data class SizeOption(
    @SerializedName("id")
    val id: String,
    @SerializedName("label")
    val label: String,
    @SerializedName("extra_price")
    val extraPrice: Double = 0.0
)

data class MilkOption(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("extra_price")
    val extraPrice: Double = 0.0
)
