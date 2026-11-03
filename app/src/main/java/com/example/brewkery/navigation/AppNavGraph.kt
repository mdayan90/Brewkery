package com.example.brewkery.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.brewkery.ui.screens.cart.CartScreen
import com.example.brewkery.ui.screens.detail.ItemDetailScreen
import com.example.brewkery.ui.screens.home.HomeScreen
import com.example.brewkery.ui.screens.order.OrderStatusScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        }
    ) {
        // Screen 1: Home / Menu
        composable(Screen.Home.route) {
            HomeScreen(
                onItemClick = { itemId ->
                    navController.navigate(Screen.ItemDetail.createRoute(itemId))
                },
                onViewCartClick = {
                    navController.navigate(Screen.Cart.route)
                },
                onActiveOrderClick = {
                    navController.navigate(Screen.OrderStatus.route)
                }
            )
        }

        // Screen 2: Item Detail & Customization
        composable(
            route = Screen.ItemDetail.route,
            arguments = listOf(
                navArgument("itemId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 1
            ItemDetailScreen(
                itemId = itemId,
                onBackClick = { navController.popBackStack() },
                onNavigateToCart = {
                    navController.navigate(Screen.Cart.route) {
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }

        // Screen 3: Cart & Checkout
        composable(Screen.Cart.route) {
            CartScreen(
                onBackClick = { navController.popBackStack() },
                onOrderPlaced = {
                    navController.navigate(Screen.OrderStatus.route) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onBrowseMenuClick = {
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                }
            )
        }

        // Screen 4: Order Status
        composable(Screen.OrderStatus.route) {
            OrderStatusScreen(
                onBackToMenuClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
