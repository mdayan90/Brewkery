package com.example.brewkery.ui.screens.order

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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.brewkery.data.model.Order
import com.example.brewkery.data.repository.CartRepository
import com.example.brewkery.ui.theme.Caramel
import com.example.brewkery.ui.theme.Crema
import com.example.brewkery.ui.theme.CremaDarker
import com.example.brewkery.ui.theme.Espresso
import com.example.brewkery.ui.theme.GreenBadgeBg
import com.example.brewkery.ui.theme.GreenBadgeText
import com.example.brewkery.ui.theme.SurfaceWhite
import com.example.brewkery.ui.theme.Terracotta
import com.example.brewkery.ui.theme.TextMuted
import com.example.brewkery.ui.theme.TextPrimary
import com.example.brewkery.ui.theme.TextSecondary
import com.example.brewkery.ui.theme.WarmBorder

@Composable
fun OrderStatusScreen(
    onBackToMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeOrder by CartRepository.activeOrder.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Crema,
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)),
                color = SurfaceWhite
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Button(
                        onClick = onBackToMenuClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Espresso),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = "Back to Menu",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        activeOrder?.let { order ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Success Brewing Badge & Animation Card
                item {
                    StatusHeroCard(order = order)
                }

                // Estimated Delivery Box
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    DeliveryEstimateCard(order = order)
                }

                // Order Items Summary
                item {
                    Spacer(modifier = Modifier.height(18.dp))
                    OrderItemsBreakdownCard(order = order)
                }
            }
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No active order found",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = onBackToMenuClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Espresso),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Go to Menu", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusHeroCard(order: Order) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, WarmBorder, RoundedCornerShape(24.dp)),
        color = SurfaceWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Brewing Icon Badge
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(CremaDarker),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Coffee,
                    contentDescription = null,
                    tint = Terracotta,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Brewing in Progress!",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Your ticket was dispatched to our artisan barista.",
                color = TextSecondary,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Ticket ID Monospace Badge
            Surface(
                color = CremaDarker,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ticket ID:",
                        color = TextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = order.ticketId,
                        color = Espresso,
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // High Visibility PREPARING Badge
            Surface(
                color = GreenBadgeBg,
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(GreenBadgeText)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = order.status,
                        color = GreenBadgeText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DeliveryEstimateCard(order: Order) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, WarmBorder, RoundedCornerShape(18.dp)),
        color = SurfaceWhite
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = CremaDarker,
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = Caramel,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = "Estimated Time",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = order.estimatedTime,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun OrderItemsBreakdownCard(order: Order) {
    Surface(
        modifier = Modifier
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
                text = "Order Details",
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            order.items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${item.quantity}x ${item.menuItem.name}",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (item.optionsDescription.isNotBlank()) {
                            Text(
                                text = item.optionsDescription,
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Text(
                        text = "$${String.format(java.util.Locale.US, "%.2f", item.totalPrice)}",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = WarmBorder, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Subtotal", color = TextSecondary, fontSize = 12.sp)
                Text(
                    text = "$${String.format(java.util.Locale.US, "%.2f", order.subtotal)}",
                    color = TextPrimary,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Delivery Fee", color = TextSecondary, fontSize = 12.sp)
                Text(
                    text = "$${String.format(java.util.Locale.US, "%.2f", order.deliveryFee)}",
                    color = TextPrimary,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Tax (8%)", color = TextSecondary, fontSize = 12.sp)
                Text(
                    text = "$${String.format(java.util.Locale.US, "%.2f", order.tax)}",
                    color = TextPrimary,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = WarmBorder, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Paid",
                    color = Espresso,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "$${String.format(java.util.Locale.US, "%.2f", order.finalTotal)}",
                    color = Terracotta,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}
