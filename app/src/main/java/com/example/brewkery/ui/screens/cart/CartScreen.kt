package com.example.brewkery.ui.screens.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.brewkery.data.model.CartItem
import com.example.brewkery.ui.theme.Caramel
import com.example.brewkery.ui.theme.Crema
import com.example.brewkery.ui.theme.CremaDarker
import com.example.brewkery.ui.theme.Espresso
import com.example.brewkery.ui.theme.SurfaceWhite
import com.example.brewkery.ui.theme.Terracotta
import com.example.brewkery.ui.theme.TextMuted
import com.example.brewkery.ui.theme.TextPrimary
import com.example.brewkery.ui.theme.TextSecondary
import com.example.brewkery.ui.theme.WarmBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onBackClick: () -> Unit,
    onOrderPlaced: () -> Unit,
    onBrowseMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CartViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Crema,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "My Cart",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Espresso
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Crema
                )
            )
        },
        bottomBar = {
            if (!uiState.isEmpty) {
                CartCheckoutBottomBar(
                    finalTotal = uiState.finalTotal,
                    onPlaceOrder = {
                        viewModel.placeOrder {
                            onOrderPlaced()
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        if (uiState.isEmpty) {
            EmptyCartView(
                onBrowseMenuClick = onBrowseMenuClick,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                // Cart Items List
                items(
                    items = uiState.items,
                    key = { it.cartItemId }
                ) { item ->
                    CartItemRow(
                        item = item,
                        onIncrement = { viewModel.incrementQuantity(item.cartItemId, item.quantity) },
                        onDecrement = { viewModel.decrementQuantity(item.cartItemId, item.quantity) },
                        onRemove = { viewModel.removeItem(item.cartItemId) },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                    )
                }

                // Bill Summary Card
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    OrderSummaryCard(
                        subtotal = uiState.subtotal,
                        deliveryFee = uiState.deliveryFee,
                        tax = uiState.tax,
                        finalTotal = uiState.finalTotal,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CartItemRow(
    item: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, WarmBorder, RoundedCornerShape(18.dp)),
        color = SurfaceWhite
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail Image
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(CremaDarker)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.menuItem.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = item.menuItem.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details and Stepper
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = item.menuItem.name,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Remove",
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                if (item.optionsDescription.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.optionsDescription,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${String.format(java.util.Locale.US, "%.2f", item.totalPrice)}",
                        color = Espresso,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    // Small Quantity Stepper
                    Surface(
                        color = CremaDarker,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            IconButton(
                                onClick = onDecrement,
                                modifier = Modifier.size(26.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Decrease",
                                    tint = Espresso,
                                    modifier = Modifier.size(13.dp)
                                )
                            }

                            Text(
                                text = "${item.quantity}",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp)
                            )

                            IconButton(
                                onClick = onIncrement,
                                modifier = Modifier.size(26.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Increase",
                                    tint = Espresso,
                                    modifier = Modifier.size(13.dp)
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
private fun OrderSummaryCard(
    subtotal: Double,
    deliveryFee: Double,
    tax: Double,
    finalTotal: Double,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, WarmBorder, RoundedCornerShape(20.dp)),
        color = SurfaceWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = "Bill Breakdown",
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(14.dp))

            SummaryRow(label = "Subtotal", amount = subtotal)
            Spacer(modifier = Modifier.height(8.dp))
            SummaryRow(label = "Delivery Fee", amount = deliveryFee)
            Spacer(modifier = Modifier.height(8.dp))
            SummaryRow(label = "Estimated Tax (8%)", amount = tax)

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = WarmBorder, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Amount",
                    color = Espresso,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "$${String.format(java.util.Locale.US, "%.2f", finalTotal)}",
                    color = Terracotta,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, amount: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 13.sp
        )
        Text(
            text = "$${String.format(java.util.Locale.US, "%.2f", amount)}",
            color = TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun CartCheckoutBottomBar(
    finalTotal: Double,
    onPlaceOrder: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)),
        color = SurfaceWhite,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Button(
                onClick = onPlaceOrder,
                colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Place Order • $${String.format(java.util.Locale.US, "%.2f", finalTotal)}",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun EmptyCartView(
    onBrowseMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(CremaDarker),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = null,
                    tint = Caramel,
                    modifier = Modifier.size(42.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Your Cart is Empty",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Explore our warm bakeries, freshly brewed coffees, and artisan cold brews to fill your bag.",
                color = TextMuted,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onBrowseMenuClick,
                colors = ButtonDefaults.buttonColors(containerColor = Espresso),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.height(46.dp)
            ) {
                Text(
                    text = "Browse Menu",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
