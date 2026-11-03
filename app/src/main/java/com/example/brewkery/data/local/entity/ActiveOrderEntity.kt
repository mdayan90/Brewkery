package com.example.brewkery.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "active_orders")
data class ActiveOrderEntity(
    @PrimaryKey
    val ticketId: String,
    val itemsJson: String,
    val subtotal: Double,
    val deliveryFee: Double,
    val tax: Double,
    val finalTotal: Double,
    val status: String,
    val estimatedTime: String,
    val placedAtTimestamp: Long
)
