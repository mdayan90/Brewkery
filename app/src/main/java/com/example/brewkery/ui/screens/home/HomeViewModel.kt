package com.example.brewkery.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewkery.data.model.CartItem
import com.example.brewkery.data.model.Category
import com.example.brewkery.data.model.MenuItem
import com.example.brewkery.data.model.Order
import com.example.brewkery.data.model.ShopMeta
import com.example.brewkery.data.repository.BrewkeryRepository
import com.example.brewkery.data.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: BrewkeryRepository = BrewkeryRepository(),
    private val cartRepository: CartRepository = CartRepository
) : ViewModel() {

    private val _rawMenuState = MutableStateFlow<MenuDataState>(MenuDataState.Loading)
    private val _selectedCategoryId = MutableStateFlow<String?>(null)
    private val _searchQuery = MutableStateFlow("")

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadMenu()
        observeDataChanges()
    }

    fun loadMenu() {
        viewModelScope.launch {
            _rawMenuState.value = MenuDataState.Loading
            _uiState.value = HomeUiState.Loading

            repository.getMenu()
                .onSuccess { response ->
                    _rawMenuState.value = MenuDataState.Success(
                        meta = response.meta,
                        categories = response.categories,
                        items = response.items
                    )
                }
                .onFailure { error ->
                    _rawMenuState.value = MenuDataState.Error(
                        error.localizedMessage ?: "Failed to load menu. Please check your internet connection."
                    )
                }
        }
    }

    fun onCategorySelected(categoryId: String?) {
        _selectedCategoryId.value = categoryId
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    private fun observeDataChanges() {
        viewModelScope.launch {
            combine(
                _rawMenuState,
                _selectedCategoryId,
                _searchQuery,
                cartRepository.cartItems,
                cartRepository.activeOrder
            ) { rawMenu, selectedCat, query, cart, activeOrder ->
                when (rawMenu) {
                    is MenuDataState.Loading -> HomeUiState.Loading
                    is MenuDataState.Error -> HomeUiState.Error(rawMenu.message)
                    is MenuDataState.Success -> {
                        val filtered = filterItems(rawMenu.items, selectedCat, query)
                        HomeUiState.Success(
                            meta = rawMenu.meta,
                            categories = rawMenu.categories,
                            allItems = rawMenu.items,
                            filteredItems = filtered,
                            selectedCategoryId = selectedCat,
                            searchQuery = query,
                            cartItems = cart,
                            activeOrder = activeOrder
                        )
                    }
                }
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    private fun filterItems(
        items: List<MenuItem>,
        categoryId: String?,
        query: String
    ): List<MenuItem> {
        return items.filter { item ->
            val matchesCategory = categoryId == null || item.categoryId.equals(categoryId, ignoreCase = true)
            val matchesQuery = query.isBlank() ||
                    item.name.contains(query, ignoreCase = true) ||
                    item.tagline.contains(query, ignoreCase = true) ||
                    item.description.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }

    private sealed interface MenuDataState {
        data object Loading : MenuDataState
        data class Error(val message: String) : MenuDataState
        data class Success(
            val meta: ShopMeta,
            val categories: List<Category>,
            val items: List<MenuItem>
        ) : MenuDataState
    }
}
