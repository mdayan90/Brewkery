package com.example.brewkery.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey
    val cartItemId: String,
    val menuItemJson: String,
    val selectedSizeJson: String?,
    val selectedMilkJson: String?,
    val selectedSugar: String?,
    val quantity: Int
)
