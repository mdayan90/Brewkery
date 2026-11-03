package com.example.brewkery.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brewkery.data.model.Category
import com.example.brewkery.ui.theme.Espresso
import com.example.brewkery.ui.theme.SurfaceWhite
import com.example.brewkery.ui.theme.TextPrimary
import com.example.brewkery.ui.theme.WarmBorder

@Composable
fun CategoryChipsRow(
    categories: List<Category>,
    selectedCategoryId: String?,
    totalItemsCount: Int,
    onCategorySelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        // "All Items" Chip
        item {
            val isSelected = selectedCategoryId == null
            CategoryChipItem(
                label = "All Items",
                icon = "✨",
                count = totalItemsCount,
                isSelected = isSelected,
                onClick = { onCategorySelected(null) }
            )
        }

        // Dynamic categories from API
        items(categories) { category ->
            val isSelected = selectedCategoryId == category.id
            CategoryChipItem(
                label = category.name,
                icon = category.icon,
                count = category.itemCount,
                isSelected = isSelected,
                onClick = { onCategorySelected(category.id) }
            )
        }
    }
}

@Composable
private fun CategoryChipItem(
    label: String,
    icon: String,
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) Espresso else SurfaceWhite
    val contentColor = if (isSelected) Color.White else TextPrimary
    val borderColor = if (isSelected) Espresso else WarmBorder

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                color = contentColor,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
            if (count > 0) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "($count)",
                    color = if (isSelected) Color(0xCCFFFFFF) else Color(0xFF8B817A),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}
