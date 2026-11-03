package com.example.brewkery.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewkery.data.model.CartItem
import com.example.brewkery.data.model.MenuItem
import com.example.brewkery.data.model.MilkOption
import com.example.brewkery.data.model.SizeOption
import com.example.brewkery.data.repository.BrewkeryRepository
import com.example.brewkery.data.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ItemDetailViewModel(
    private val itemId: Int,
    private val repository: BrewkeryRepository = BrewkeryRepository(),
    private val cartRepository: CartRepository = CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ItemDetailUiState>(ItemDetailUiState.Loading)
    val uiState: StateFlow<ItemDetailUiState> = _uiState.asStateFlow()

    init {
        loadItemDetail()
    }

    fun loadItemDetail() {
        viewModelScope.launch {
            _uiState.value = ItemDetailUiState.Loading
            repository.getItemDetail(itemId)
                .onSuccess { item ->
                    _uiState.value = ItemDetailUiState.Success(
                        item = item,
                        selectedSize = item.customizations.sizes.firstOrNull(),
                        selectedMilk = item.customizations.milkOptions.firstOrNull(),
                        selectedSugar = item.customizations.sugarLevels.firstOrNull(),
                        quantity = 1
                    )
                }
                .onFailure { error ->
                    _uiState.value = ItemDetailUiState.Error(
                        error.localizedMessage ?: "Failed to load product details."
                    )
                }
        }
    }

    fun onSizeSelected(size: SizeOption) {
        _uiState.update { current ->
            if (current is ItemDetailUiState.Success) {
                current.copy(selectedSize = size)
            } else current
        }
    }

    fun onMilkSelected(milk: MilkOption) {
        _uiState.update { current ->
            if (current is ItemDetailUiState.Success) {
                current.copy(selectedMilk = milk)
            } else current
        }
    }

    fun onSugarSelected(sugar: String) {
        _uiState.update { current ->
            if (current is ItemDetailUiState.Success) {
                current.copy(selectedSugar = sugar)
            } else current
        }
    }

    fun incrementQuantity() {
        _uiState.update { current ->
            if (current is ItemDetailUiState.Success && current.quantity < 20) {
                current.copy(quantity = current.quantity + 1)
            } else current
        }
    }

    fun decrementQuantity() {
        _uiState.update { current ->
            if (current is ItemDetailUiState.Success && current.quantity > 1) {
                current.copy(quantity = current.quantity - 1)
            } else current
        }
    }

    fun addToCart(onSuccess: () -> Unit) {
        val currentState = _uiState.value
        if (currentState is ItemDetailUiState.Success) {
            val cartItem = CartItem(
                menuItem = currentState.item,
                selectedSize = currentState.selectedSize,
                selectedMilk = currentState.selectedMilk,
                selectedSugar = currentState.selectedSugar,
                quantity = currentState.quantity
            )
            cartRepository.addToCart(cartItem)
            _uiState.update { (it as ItemDetailUiState.Success).copy(isAddedToCart = true) }
            onSuccess()
        }
    }
}
