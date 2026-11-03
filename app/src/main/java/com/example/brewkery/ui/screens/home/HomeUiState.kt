package com.example.brewkery.ui.screens.home

import com.example.brewkery.data.model.CartItem
import com.example.brewkery.data.model.Category
import com.example.brewkery.data.model.MenuItem
import com.example.brewkery.data.model.Order
import com.example.brewkery.data.model.ShopMeta

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Error(val message: String) : HomeUiState
    data class Success(
        val meta: ShopMeta = ShopMeta(),
        val categories: List<Category> = emptyList(),
        val allItems: List<MenuItem> = emptyList(),
        val filteredItems: List<MenuItem> = emptyList(),
        val selectedCategoryId: String? = null,
        val searchQuery: String = "",
        val cartItems: List<CartItem> = emptyList(),
        val activeOrder: Order? = null
    ) : HomeUiState {
        val totalCartCount: Int
            get() = cartItems.sumOf { it.quantity }

        val cartSubtotal: Double
            get() = cartItems.sumOf { it.totalPrice }
    }
}
