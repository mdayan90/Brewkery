package com.example.brewkery.data.model

data class Order(
    val ticketId: String,
    val items: List<CartItem>,
    val subtotal: Double,
    val deliveryFee: Double,
    val tax: Double,
    val finalTotal: Double,
    val status: String = "PREPARING",
    val estimatedTime: String = "20 - 30 mins",
    val placedAtTimestamp: Long = System.currentTimeMillis()
)
