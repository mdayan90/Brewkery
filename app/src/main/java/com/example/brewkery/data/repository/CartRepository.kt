package com.example.brewkery.data.repository

import android.content.Context
import com.example.brewkery.data.local.BrewkeryDatabase
import com.example.brewkery.data.local.entity.ActiveOrderEntity
import com.example.brewkery.data.local.entity.CartItemEntity
import com.example.brewkery.data.model.CartItem
import com.example.brewkery.data.model.MenuItem
import com.example.brewkery.data.model.MilkOption
import com.example.brewkery.data.model.Order
import com.example.brewkery.data.model.SizeOption
import com.example.brewkery.utils.PriceCalculator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

object CartRepository {

    private val gson = Gson()
    private val scope = CoroutineScope(Dispatchers.IO)
    private var database: BrewkeryDatabase? = null

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _activeOrder = MutableStateFlow<Order?>(null)
    val activeOrder: StateFlow<Order?> = _activeOrder.asStateFlow()

    fun initialize(context: Context) {
        if (database == null) {
            val db = BrewkeryDatabase.getDatabase(context)
            database = db

            // Listen to local Room Cart Database
            scope.launch {
                db.cartDao().getAllCartItems().collect { entities ->
                    _cartItems.value = entities.mapNotNull { it.toDomain() }
                }
            }

            // Listen to local Room Active Order Database
            scope.launch {
                db.orderDao().getLatestOrder().collect { orderEntity ->
                    _activeOrder.value = orderEntity?.toDomain()
                }
            }
        }
    }

    fun addToCart(item: CartItem) {
        val current = _cartItems.value
        val existingIndex = current.indexOfFirst {
            it.menuItem.id == item.menuItem.id &&
                    it.selectedSize?.id == item.selectedSize?.id &&
                    it.selectedMilk?.id == item.selectedMilk?.id &&
                    it.selectedSugar == item.selectedSugar
        }

        if (existingIndex >= 0) {
            val existing = current[existingIndex]
            val updatedQuantity = existing.quantity + item.quantity
            updateQuantity(existing.cartItemId, updatedQuantity)
        } else {
            scope.launch {
                database?.cartDao()?.insertItem(item.toEntity())
            }
        }
    }

    fun updateQuantity(cartItemId: String, newQuantity: Int) {
        scope.launch {
            if (newQuantity <= 0) {
                database?.cartDao()?.deleteItem(cartItemId)
            } else {
                database?.cartDao()?.updateQuantity(cartItemId, newQuantity)
            }
        }
    }

    fun removeItem(cartItemId: String) {
        scope.launch {
            database?.cartDao()?.deleteItem(cartItemId)
        }
    }

    fun clearCart() {
        scope.launch {
            database?.cartDao()?.clearCart()
        }
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

        scope.launch {
            database?.orderDao()?.saveOrder(newOrder.toEntity())
            database?.cartDao()?.clearCart()
        }

        return newOrder
    }

    fun clearActiveOrder() {
        scope.launch {
            database?.orderDao()?.clearOrders()
        }
    }

    // Mapping Helpers
    private fun CartItem.toEntity(): CartItemEntity {
        return CartItemEntity(
            cartItemId = cartItemId,
            menuItemJson = gson.toJson(menuItem),
            selectedSizeJson = selectedSize?.let { gson.toJson(it) },
            selectedMilkJson = selectedMilk?.let { gson.toJson(it) },
            selectedSugar = selectedSugar,
            quantity = quantity
        )
    }

    private fun CartItemEntity.toDomain(): CartItem? {
        return try {
            val menuItem = gson.fromJson(menuItemJson, MenuItem::class.java)
            val selectedSize = selectedSizeJson?.let { gson.fromJson(it, SizeOption::class.java) }
            val selectedMilk = selectedMilkJson?.let { gson.fromJson(it, MilkOption::class.java) }
            CartItem(
                cartItemId = cartItemId,
                menuItem = menuItem,
                selectedSize = selectedSize,
                selectedMilk = selectedMilk,
                selectedSugar = selectedSugar,
                quantity = quantity
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun Order.toEntity(): ActiveOrderEntity {
        return ActiveOrderEntity(
            ticketId = ticketId,
            itemsJson = gson.toJson(items),
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            tax = tax,
            finalTotal = finalTotal,
            status = status,
            estimatedTime = estimatedTime,
            placedAtTimestamp = placedAtTimestamp
        )
    }

    private fun ActiveOrderEntity.toDomain(): Order? {
        return try {
            val itemsType = object : TypeToken<List<CartItem>>() {}.type
            val itemsList: List<CartItem> = gson.fromJson(itemsJson, itemsType)
            Order(
                ticketId = ticketId,
                items = itemsList,
                subtotal = subtotal,
                deliveryFee = deliveryFee,
                tax = tax,
                finalTotal = finalTotal,
                status = status,
                estimatedTime = estimatedTime,
                placedAtTimestamp = placedAtTimestamp
            )
        } catch (e: Exception) {
            null
        }
    }
}
