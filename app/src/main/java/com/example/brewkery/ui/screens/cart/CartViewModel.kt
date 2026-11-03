package com.example.brewkery.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewkery.data.model.Order
import com.example.brewkery.data.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository = CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        observeCart()
    }

    private fun observeCart() {
        viewModelScope.launch {
            cartRepository.cartItems.collect { items ->
                _uiState.update { current ->
                    current.copy(items = items)
                }
            }
        }
    }

    fun incrementQuantity(cartItemId: String, currentQuantity: Int) {
        if (currentQuantity < 20) {
            cartRepository.updateQuantity(cartItemId, currentQuantity + 1)
        }
    }

    fun decrementQuantity(cartItemId: String, currentQuantity: Int) {
        cartRepository.updateQuantity(cartItemId, currentQuantity - 1)
    }

    fun removeItem(cartItemId: String) {
        cartRepository.removeItem(cartItemId)
    }

    fun placeOrder(onSuccess: (Order) -> Unit) {
        val order = cartRepository.placeOrder()
        if (order != null) {
            _uiState.update { it.copy(isOrderPlaced = true, placedOrder = order) }
            onSuccess(order)
        }
    }
}
