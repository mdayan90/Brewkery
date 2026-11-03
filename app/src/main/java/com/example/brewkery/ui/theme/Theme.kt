package com.example.brewkery.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Terracotta,
    onPrimary = SurfaceWhite,
    primaryContainer = EspressoLight,
    onPrimaryContainer = Crema,
    secondary = Caramel,
    onSecondary = SurfaceWhite,
    background = Espresso,
    onBackground = Crema,
    surface = EspressoLight,
    onSurface = Crema,
    surfaceVariant = Espresso,
    onSurfaceVariant = WarmBorder,
    outline = WarmBorder
)

private val LightColorScheme = lightColorScheme(
    primary = Terracotta,
    onPrimary = SurfaceWhite,
    primaryContainer = CremaDarker,
    onPrimaryContainer = Espresso,
    secondary = Caramel,
    onSecondary = SurfaceWhite,
    background = Crema,
    onBackground = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = CremaDarker,
    onSurfaceVariant = TextSecondary,
    outline = WarmBorder
)

@Composable
fun BrewkeryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
