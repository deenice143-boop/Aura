package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = GoldPrimary,
    onPrimary = OnGold,
    secondary = GoldSecondary,
    onSecondary = OnGold,
    tertiary = GoldTertiary,
    onTertiary = OnGold,
    background = CharcoalBg,
    onBackground = TextLight,
    surface = CharcoalSurface,
    onSurface = TextLight,
    surfaceVariant = CharcoalCard,
    onSurfaceVariant = TextMuted,
    outline = CharcoalBorder
  )

private val LightColorScheme =
  lightColorScheme(
    primary = GoldPrimary,
    onPrimary = OnGold,
    secondary = GoldSecondary,
    onSecondary = OnGold,
    tertiary = GoldTertiary,
    onTertiary = OnGold,
    background = CharcoalBg, // Keep consistent high-end dark brand feel
    onBackground = TextLight,
    surface = CharcoalSurface,
    onSurface = TextLight,
    surfaceVariant = CharcoalCard,
    onSurfaceVariant = TextMuted,
    outline = CharcoalBorder
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is disabled by default to enforce brand-specific gold/charcoal hex codes globally
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
