package com.santheconnect.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont

// ── Brand colours ──────────────────────────────────────────────────────────
object SantheColors {
    val Saffron   = Color(0xFFE8621A)
    val Turmeric  = Color(0xFFF5A623)
    val Leaf      = Color(0xFF2D7A3A)
    val Earth     = Color(0xFF8B4513)
    val Cream     = Color(0xFFFDF6E3)
    val DeepGreen = Color(0xFF1A4A24)
    val Rust      = Color(0xFFC0392B)
    val Gold      = Color(0xFFD4A017)
    val Charcoal  = Color(0xFF2C1810)
    val White     = Color(0xFFFFFFFF)
    val LightGrey = Color(0xFF888888)
}

private val LightColorScheme = lightColorScheme(
    primary        = SantheColors.Saffron,
    secondary      = SantheColors.Leaf,
    tertiary       = SantheColors.Turmeric,
    background     = SantheColors.Cream,
    surface        = SantheColors.White,
    onPrimary      = SantheColors.White,
    onBackground   = SantheColors.Charcoal,
    onSurface      = SantheColors.Charcoal,
)

@Composable
fun SantheConnectTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
