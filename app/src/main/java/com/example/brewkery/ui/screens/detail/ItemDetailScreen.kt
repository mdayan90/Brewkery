package com.example.brewkery.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.brewkery.data.model.MilkOption
import com.example.brewkery.data.model.SizeOption
import com.example.brewkery.ui.components.ErrorState
import com.example.brewkery.ui.components.LoadingState
import com.example.brewkery.ui.theme.Caramel
import com.example.brewkery.ui.theme.Crema
import com.example.brewkery.ui.theme.CremaDarker
import com.example.brewkery.ui.theme.Espresso
import com.example.brewkery.ui.theme.OrangeBadgeBg
import com.example.brewkery.ui.theme.OrangeBadgeText
import com.example.brewkery.ui.theme.SurfaceWhite
import com.example.brewkery.ui.theme.Terracotta
import com.example.brewkery.ui.theme.TextMuted
import com.example.brewkery.ui.theme.TextPrimary
import com.example.brewkery.ui.theme.TextSecondary
import com.example.brewkery.ui.theme.WarmBorder

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ItemDetailScreen(
    itemId: Int,
    onBackClick: () -> Unit,
    onNavigateToCart: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ItemDetailViewModel = viewModel(factory = ItemDetailViewModelFactory(itemId))
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Crema,
        bottomBar = {
            if (uiState is ItemDetailUiState.Success) {
                val state = uiState as ItemDetailUiState.Success
                DetailBottomBar(
                    totalPrice = state.totalPrice,
                    quantity = state.quantity,
                    onIncrement = { viewModel.incrementQuantity() },
                    onDecrement = { viewModel.decrementQuantity() },
                    onAddToCart = {
                        viewModel.addToCart(onSuccess = onNavigateToCart)
                    }
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
                is ItemDetailUiState.Loading -> {
                    LoadingState("Preparing artisan recipe...")
                }

                is ItemDetailUiState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = { viewModel.loadItemDetail() }
                    )
                }

                is ItemDetailUiState.Success -> {
                    val item = state.item

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 100.dp)
                    ) {
                        // Hero Image with Floating Back Button & Badge
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                                    .background(CremaDarker)
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(item.imageUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = item.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )

                                // Back button
                                Surface(
                                    shape = CircleShape,
                                    color = Color.White.copy(alpha = 0.9f),
                                    modifier = Modifier
                                        .padding(start = 20.dp, top = 20.dp)
                                        .size(42.dp)
                                        .align(Alignment.TopStart)
                                ) {
                                    IconButton(onClick = onBackClick) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Back",
                                            tint = Espresso,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                // Badge
                                item.badge?.let { badgeText ->
                                    Surface(
                                        color = OrangeBadgeBg,
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier
                                            .padding(end = 20.dp, top = 20.dp)
                                            .align(Alignment.TopEnd)
                                    ) {
                                        Text(
                                            text = badgeText,
                                            color = OrangeBadgeText,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Product Title, Rating, Calories & Prep Time
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 18.dp)
                            ) {
                                Text(
                                    text = item.name,
                                    color = TextPrimary,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = item.tagline,
                                    color = Caramel,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                // Quick Stats Row (Rating, Calories, Prep Time)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    QuickStatPill(
                                        icon = Icons.Default.Star,
                                        iconTint = Caramel,
                                        text = "${item.rating} (${item.reviewCount})",
                                        modifier = Modifier.weight(1f)
                                    )
                                    QuickStatPill(
                                        icon = Icons.Default.LocalFireDepartment,
                                        iconTint = Terracotta,
                                        text = "${item.calories} kcal",
                                        modifier = Modifier.weight(1f)
                                    )
                                    QuickStatPill(
                                        icon = Icons.Default.Schedule,
                                        iconTint = Espresso,
                                        text = item.prepTime,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }

                        // Description
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                            ) {
                                Text(
                                    text = "Description",
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = item.description,
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    lineHeight = 19.sp
                                )
                            }
                        }

                        // Ingredients Chips
                        if (item.ingredients.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(18.dp))
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp)
                                ) {
                                    Text(
                                        text = "Ingredients",
                                        color = TextPrimary,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        item.ingredients.forEach { ingredient ->
                                            Surface(
                                                color = SurfaceWhite,
                                                shape = RoundedCornerShape(12.dp),
                                                border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder)
                                            ) {
                                                Text(
                                                    text = ingredient,
                                                    color = TextPrimary,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Size Customization
                        if (item.customizations.sizes.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(20.dp))
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp)
                                ) {
                                    Text(
                                        text = "Select Size",
                                        color = TextPrimary,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        item.customizations.sizes.forEach { size ->
                                            val isSelected = state.selectedSize?.id == size.id
                                            OptionRowCard(
                                                title = size.label,
                                                extraPrice = size.extraPrice,
                                                isSelected = isSelected,
                                                onClick = { viewModel.onSizeSelected(size) }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Milk / Spread Customization
                        if (item.customizations.milkOptions.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(20.dp))
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp)
                                ) {
                                    Text(
                                        text = "Milk & Spread Options",
                                        color = TextPrimary,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        item.customizations.milkOptions.forEach { milk ->
                                            val isSelected = state.selectedMilk?.id == milk.id
                                            OptionRowCard(
                                                title = milk.name,
                                                extraPrice = milk.extraPrice,
                                                isSelected = isSelected,
                                                onClick = { viewModel.onMilkSelected(milk) }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Sugar / Prep Temperature Customization
                        if (item.customizations.sugarLevels.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(20.dp))
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp)
                                ) {
                                    Text(
                                        text = "Sweetness & Temperature",
                                        color = TextPrimary,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        item.customizations.sugarLevels.forEach { sugar ->
                                            val isSelected = state.selectedSugar == sugar
                                            OptionRowCard(
                                                title = sugar,
                                                extraPrice = 0.0,
                                                isSelected = isSelected,
                                                onClick = { viewModel.onSugarSelected(sugar) }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickStatPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = SurfaceWhite,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                color = TextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun OptionRowCard(
    title: String,
    extraPrice: Double,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Terracotta else WarmBorder
    val bgColor = if (isSelected) SurfaceWhite else SurfaceWhite

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() },
        color = bgColor,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(if (isSelected) 1.5.dp else 1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Radio indicator
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .border(2.dp, if (isSelected) Terracotta else TextMuted, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Terracotta)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }

            if (extraPrice > 0.0) {
                Text(
                    text = "+$${String.format(java.util.Locale.US, "%.2f", extraPrice)}",
                    color = Caramel,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun DetailBottomBar(
    totalPrice: Double,
    quantity: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onAddToCart: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)),
        color = SurfaceWhite,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Quantity Stepper
            Surface(
                color = CremaDarker,
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    IconButton(
                        onClick = onDecrement,
                        enabled = quantity > 1,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease",
                            tint = if (quantity > 1) Espresso else TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = "$quantity",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    IconButton(
                        onClick = onIncrement,
                        enabled = quantity < 20,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase",
                            tint = Espresso,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Add to Cart Button
            Button(
                onClick = onAddToCart,
                colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Add to Cart • $${String.format(java.util.Locale.US, "%.2f", totalPrice)}",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
