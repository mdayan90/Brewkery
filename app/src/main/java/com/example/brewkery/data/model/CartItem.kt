package com.example.brewkery.data.model

import java.util.UUID

data class CartItem(
    val cartItemId: String = UUID.randomUUID().toString(),
    val menuItem: MenuItem,
    val selectedSize: SizeOption?,
    val selectedMilk: MilkOption?,
    val selectedSugar: String?,
    val quantity: Int = 1
) {
    val unitPrice: Double
        get() = menuItem.basePrice + (selectedSize?.extraPrice ?: 0.0) + (selectedMilk?.extraPrice ?: 0.0)

    val totalPrice: Double
        get() = unitPrice * quantity

    val optionsDescription: String
        get() = buildList {
            selectedSize?.let { add(it.label) }
            selectedMilk?.let { add(it.name) }
            selectedSugar?.let { add(it) }
        }.joinToString(" • ")
}
