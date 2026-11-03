package com.example.brewkery.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.brewkery.data.model.MenuItem
import com.example.brewkery.ui.theme.Caramel
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

@Composable
fun ProductCard(
    item: MenuItem,
    onItemClick: (MenuItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, WarmBorder, RoundedCornerShape(20.dp))
            .clickable { onItemClick(item) },
        color = SurfaceWhite,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail Image with Badge
            Box(
                modifier = Modifier
                    .size(105.dp)
                    .clip(RoundedCornerShape(16.dp))
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

                item.badge?.let { badgeText ->
                    Surface(
                        color = OrangeBadgeBg,
                        shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomEnd = 8.dp, bottomStart = 0.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = badgeText,
                            color = OrangeBadgeText,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Item Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = item.tagline,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Rating & Review count
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Caramel,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${item.rating}",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "(${item.reviewCount})",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Price and Customize Action Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${String.format(java.util.Locale.US, "%.2f", item.basePrice)}",
                        color = Espresso,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Button(
                        onClick = { onItemClick(item) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Terracotta
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            horizontal = 10.dp,
                            vertical = 4.dp
                        ),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Customize",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Customize",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
