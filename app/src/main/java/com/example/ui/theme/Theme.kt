package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
  primary = SkyBluePrimary,
  onPrimary = PureWhite,
  primaryContainer = SkyBlueLight,
  onPrimaryContainer = DeepNavy,
  secondary = CyanAccent,
  onSecondary = PureWhite,
  secondaryContainer = SkyBlueExtraLight,
  onSecondaryContainer = DeepNavy,
  tertiary = WarningAmber,
  onTertiary = PureWhite,
  background = BackgroundWhite,
  onBackground = TextPrimary,
  surface = SurfaceWhite,
  onSurface = TextPrimary,
  surfaceVariant = SurfaceVariantLight,
  onSurfaceVariant = TextSecondary,
  outline = BorderLight,
  outlineVariant = BorderLight
)

private val DarkColorScheme = darkColorScheme(
  primary = CyanAccent,
  onPrimary = DeepNavy,
  primaryContainer = DeepNavy,
  onPrimaryContainer = SkyBlueLight,
  secondary = SkyBluePrimary,
  onSecondary = PureWhite,
  background = Color(0xFF0B1320),
  onBackground = PureWhite,
  surface = Color(0xFF111D30),
  onSurface = PureWhite,
  surfaceVariant = Color(0xFF1E293B),
  onSurfaceVariant = Color(0xFFCBD5E1),
  outline = Color(0xFF334155)
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false, // Default to bright light-blue and white theme
  dynamicColor: Boolean = false, // Keep consistent brand colors
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
