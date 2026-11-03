package com.example.brewkery.ui.screens.cart

import com.example.brewkery.data.model.CartItem
import com.example.brewkery.data.model.Order
import com.example.brewkery.utils.PriceCalculator

data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val deliveryFee: Double = PriceCalculator.DELIVERY_FEE,
    val isOrderPlaced: Boolean = false,
    val placedOrder: Order? = null
) {
    val totalCount: Int
        get() = items.sumOf { it.quantity }

    val subtotal: Double
        get() = PriceCalculator.calculateSubtotal(items.map { it.totalPrice })

    val tax: Double
        get() = PriceCalculator.calculateTax(subtotal)

    val finalTotal: Double
        get() = PriceCalculator.calculateFinalTotal(subtotal, deliveryFee, tax)

    val isEmpty: Boolean
        get() = items.isEmpty()
}
