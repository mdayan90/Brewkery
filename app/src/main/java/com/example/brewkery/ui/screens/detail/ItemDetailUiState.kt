package com.example.brewkery.ui.screens.detail

import com.example.brewkery.data.model.MenuItem
import com.example.brewkery.data.model.MilkOption
import com.example.brewkery.data.model.SizeOption

sealed interface ItemDetailUiState {
    data object Loading : ItemDetailUiState
    data class Error(val message: String) : ItemDetailUiState
    data class Success(
        val item: MenuItem,
        val selectedSize: SizeOption?,
        val selectedMilk: MilkOption?,
        val selectedSugar: String?,
        val quantity: Int = 1,
        val isAddedToCart: Boolean = false
    ) : ItemDetailUiState {
        val unitPrice: Double
            get() = item.basePrice + (selectedSize?.extraPrice ?: 0.0) + (selectedMilk?.extraPrice ?: 0.0)

        val totalPrice: Double
            get() = unitPrice * quantity
    }
}
