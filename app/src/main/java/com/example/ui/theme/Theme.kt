package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TerraVaultLightColorScheme = lightColorScheme(
    primary = ForestGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = ForestGreenLight,
    onPrimaryContainer = ForestGreenDark,
    secondary = SageGreenAccent,
    onSecondary = Color.White,
    secondaryContainer = ForestGreenLight,
    onSecondaryContainer = ForestGreenDark,
    tertiary = WarningAmber,
    onTertiary = Color.White,
    error = DangerRed,
    onError = Color.White,
    errorContainer = DangerRedBg,
    onErrorContainer = DangerRed,
    background = IvoryBackground,
    onBackground = CharcoalTextPrimary,
    surface = PureWhiteSurface,
    onSurface = CharcoalTextPrimary,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = CharcoalTextSecondary,
    outline = SubtleBorder,
    outlineVariant = DividerColor
)

private val TerraVaultDarkColorScheme = darkColorScheme(
    primary = Color(0xFF2DC598),
    onPrimary = Color(0xFF003827),
    primaryContainer = Color(0xFF0F543E),
    onPrimaryContainer = Color(0xFFB4F2DC),
    secondary = SageGreenAccent,
    onSecondary = Color(0xFF1B352E),
    tertiary = Color(0xFFFFB951),
    background = Color(0xFF121B1A),
    onBackground = Color(0xFFE4EDE9),
    surface = Color(0xFF192523),
    onSurface = Color(0xFFE4EDE9),
    surfaceVariant = Color(0xFF223230),
    onSurfaceVariant = Color(0xFFADC0BB),
    outline = Color(0xFF384C48)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent gov-tech branding
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) TerraVaultDarkColorScheme else TerraVaultLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

