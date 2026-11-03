package com.example.brewkery.data.repository

import com.example.brewkery.data.model.CartItem
import com.example.brewkery.data.model.Order
import com.example.brewkery.utils.PriceCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

object CartRepository {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _activeOrder = MutableStateFlow<Order?>(null)
    val activeOrder: StateFlow<Order?> = _activeOrder.asStateFlow()

    fun addToCart(item: CartItem) {
        _cartItems.update { current ->
            // Check if identical item already exists (same product and customizations)
            val existingIndex = current.indexOfFirst {
                it.menuItem.id == item.menuItem.id &&
                        it.selectedSize?.id == item.selectedSize?.id &&
                        it.selectedMilk?.id == item.selectedMilk?.id &&
                        it.selectedSugar == item.selectedSugar
            }

            if (existingIndex >= 0) {
                current.toMutableList().apply {
                    val existing = this[existingIndex]
                    this[existingIndex] = existing.copy(quantity = existing.quantity + item.quantity)
                }
            } else {
                current + item
            }
        }
    }

    fun updateQuantity(cartItemId: String, newQuantity: Int) {
        _cartItems.update { current ->
            if (newQuantity <= 0) {
                current.filterNot { it.cartItemId == cartItemId }
            } else {
                current.map {
                    if (it.cartItemId == cartItemId) it.copy(quantity = newQuantity) else it
                }
            }
        }
    }

    fun removeItem(cartItemId: String) {
        _cartItems.update { current ->
            current.filterNot { it.cartItemId == cartItemId }
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun placeOrder(deliveryFee: Double = PriceCalculator.DELIVERY_FEE): Order? {
        val currentItems = _cartItems.value
        if (currentItems.isEmpty()) return null

        val subtotal = PriceCalculator.calculateSubtotal(currentItems.map { it.totalPrice })
        val tax = PriceCalculator.calculateTax(subtotal)
        val finalTotal = PriceCalculator.calculateFinalTotal(subtotal, deliveryFee, tax)

        val ticketId = "#BWK-${Random.nextInt(1000, 9999)}"

        val newOrder = Order(
            ticketId = ticketId,
            items = currentItems,
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            tax = tax,
            finalTotal = finalTotal,
            status = "PREPARING"
        )

        _activeOrder.value = newOrder
        clearCart()
        return newOrder
    }

    fun clearActiveOrder() {
        _activeOrder.value = null
    }
}
