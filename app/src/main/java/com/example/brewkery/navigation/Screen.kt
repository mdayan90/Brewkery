package com.example.brewkery.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object ItemDetail : Screen("item_detail/{itemId}") {
        fun createRoute(itemId: Int) = "item_detail/$itemId"
    }
    data object Cart : Screen("cart")
    data object OrderStatus : Screen("order_status")
}
