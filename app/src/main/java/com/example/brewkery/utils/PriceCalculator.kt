package com.example.brewkery.utils

import java.math.BigDecimal
import java.math.RoundingMode

object PriceCalculator {
    const val DELIVERY_FEE = 2.50
    const val TAX_RATE = 0.08 // 8%

    fun calculateUnitPrice(basePrice: Double, sizeExtra: Double = 0.0, milkExtra: Double = 0.0): Double {
        return roundTwoDecimals(basePrice + sizeExtra + milkExtra)
    }

    fun calculateItemTotal(unitPrice: Double, quantity: Int): Double {
        return roundTwoDecimals(unitPrice * quantity)
    }

    fun calculateSubtotal(itemTotals: List<Double>): Double {
        return roundTwoDecimals(itemTotals.sum())
    }

    fun calculateTax(subtotal: Double): Double {
        return roundTwoDecimals(subtotal * TAX_RATE)
    }

    fun calculateFinalTotal(subtotal: Double, deliveryFee: Double = DELIVERY_FEE, tax: Double = calculateTax(subtotal)): Double {
        if (subtotal <= 0.0) return 0.0
        return roundTwoDecimals(subtotal + deliveryFee + tax)
    }

    fun formatPrice(amount: Double, symbol: String = "$"): String {
        return "$symbol${String.format(java.util.Locale.US, "%.2f", amount)}"
    }

    fun roundTwoDecimals(value: Double): Double {
        return BigDecimal.valueOf(value)
            .setScale(2, RoundingMode.HALF_UP)
            .toDouble()
    }
}
