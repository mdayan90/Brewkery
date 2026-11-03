package com.example.brewkery

import com.example.brewkery.utils.PriceCalculator
import org.junit.Assert.assertEquals
import org.junit.Test

class PriceCalculatorTest {

    @Test
    fun calculateUnitPrice_withBaseOnly_returnsBasePrice() {
        val unitPrice = PriceCalculator.calculateUnitPrice(basePrice = 4.85)
        assertEquals(4.85, unitPrice, 0.001)
    }

    @Test
    fun calculateUnitPrice_withCustomizations_returnsSum() {
        // Base 4.85 + Grande (+0.65) + Almond Milk (+0.50) = 6.00
        val unitPrice = PriceCalculator.calculateUnitPrice(
            basePrice = 4.85,
            sizeExtra = 0.65,
            milkExtra = 0.50
        )
        assertEquals(6.00, unitPrice, 0.001)
    }

    @Test
    fun calculateItemTotal_multipliesByQuantity() {
        val itemTotal = PriceCalculator.calculateItemTotal(unitPrice = 6.00, quantity = 3)
        assertEquals(18.00, itemTotal, 0.001)
    }

    @Test
    fun calculateTax_calculates8PercentCorrectly() {
        // 8% of 10.00 is 0.80
        val tax = PriceCalculator.calculateTax(subtotal = 10.00)
        assertEquals(0.80, tax, 0.001)

        // 8% of 18.50 is 1.48
        val tax2 = PriceCalculator.calculateTax(subtotal = 18.50)
        assertEquals(1.48, tax2, 0.001)
    }

    @Test
    fun calculateFinalTotal_combinesSubtotalDeliveryFeeAndTax() {
        // Subtotal = 10.00, Delivery = 2.50, Tax = 0.80 -> Total = 13.30
        val total = PriceCalculator.calculateFinalTotal(
            subtotal = 10.00,
            deliveryFee = 2.50,
            tax = 0.80
        )
        assertEquals(13.30, total, 0.001)
    }

    @Test
    fun calculateFinalTotal_emptyCart_returnsZero() {
        val total = PriceCalculator.calculateFinalTotal(subtotal = 0.0)
        assertEquals(0.00, total, 0.001)
    }

    @Test
    fun formatPrice_formatsCurrencyCorrectly() {
        val formatted = PriceCalculator.formatPrice(4.85)
        assertEquals("$4.85", formatted)

        val formattedWhole = PriceCalculator.formatPrice(5.0)
        assertEquals("$5.00", formattedWhole)
    }
}
