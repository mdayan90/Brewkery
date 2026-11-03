package com.example.brewkery.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.brewkery.data.model.MenuItem
import com.example.brewkery.ui.components.BrewkerySearchBar
import com.example.brewkery.ui.components.CartBottomBar
import com.example.brewkery.ui.components.CategoryChipsRow
import com.example.brewkery.ui.components.ErrorState
import com.example.brewkery.ui.components.LoadingState
import com.example.brewkery.ui.components.ProductCard
import com.example.brewkery.ui.components.StoreHeaderBanner
import com.example.brewkery.ui.theme.Crema
import com.example.brewkery.ui.theme.TextMuted
import com.example.brewkery.ui.theme.TextPrimary

@Composable
fun HomeScreen(
    onItemClick: (Int) -> Unit,
    onViewCartClick: () -> Unit,
    onActiveOrderClick: () -> Unit,
    viewModel: HomeViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Crema,
        bottomBar = {
            if (uiState is HomeUiState.Success) {
                val state = uiState as HomeUiState.Success
                CartBottomBar(
                    totalItems = state.totalCartCount,
                    subtotal = state.cartSubtotal,
                    onViewCartClick = onViewCartClick
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is HomeUiState.Loading -> {
                    LoadingState()
                }

                is HomeUiState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = { viewModel.loadMenu() }
                    )
                }

                is HomeUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 90.dp)
                    ) {
                        // Store Announcement Banner
                        item {
                            StoreHeaderBanner(
                                shopMeta = state.meta,
                                activeOrder = state.activeOrder,
                                onActiveOrderClick = onActiveOrderClick
                            )
                        }

                        // Search Bar
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            BrewkerySearchBar(
                                query = state.searchQuery,
                                onQueryChange = { viewModel.onSearchQueryChanged(it) },
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                        }

                        // Categories Horizontal Row
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            CategoryChipsRow(
                                categories = state.categories,
                                selectedCategoryId = state.selectedCategoryId,
                                totalItemsCount = state.allItems.size,
                                onCategorySelected = { viewModel.onCategorySelected(it) }
                            )
                        }

                        // Section Heading
                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                            val sectionTitle = state.selectedCategoryId?.let { catId ->
                                state.categories.find { it.id == catId }?.name ?: "Menu Items"
                            } ?: "All Artisanal Creations"

                            Text(
                                text = sectionTitle,
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // Empty State for Search / Filter
                        if (state.filteredItems.isEmpty()) {
                            item {
                                EmptySearchState(
                                    query = state.searchQuery,
                                    modifier = Modifier.padding(top = 40.dp)
                                )
                            }
                        } else {
                            // Product Cards List
                            items(
                                items = state.filteredItems,
                                key = { it.id }
                            ) { item ->
                                ProductCard(
                                    item = item,
                                    onItemClick = { onItemClick(item.id) },
                                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptySearchState(
    query: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.SearchOff,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(54.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "No delicious items found",
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = if (query.isNotBlank()) "We couldn't find any coffee or pastry matching \"$query\"." else "No items available in this category.",
            color = TextMuted,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}
