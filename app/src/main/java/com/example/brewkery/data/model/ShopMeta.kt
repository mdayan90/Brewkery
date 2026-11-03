package com.example.brewkery.data.model

import com.google.gson.annotations.SerializedName

data class ShopMeta(
    @SerializedName("app")
    val app: String = "Brewkery",
    @SerializedName("version")
    val version: String = "1.0.0",
    @SerializedName("tagline")
    val tagline: String = "Artisanal Coffee & Fresh Oven Bakes",
    @SerializedName("currency")
    val currency: String = "USD",
    @SerializedName("currency_symbol")
    val currencySymbol: String = "$",
    @SerializedName("delivery_fee")
    val deliveryFee: Double = 2.50,
    @SerializedName("tax_rate_percent")
    val taxRatePercent: Double = 8.0,
    @SerializedName("estimated_delivery_time")
    val estimatedDeliveryTime: String = "20 - 30 mins"
)
