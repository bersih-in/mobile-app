package com.bersihin.mobileapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val darkColorScheme = darkColorScheme(
    primary = Color(0xff2aadad),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF016b64),
    surface = Color(0XFF001826),
    error = Color(0xFFFFDBDB),
    onErrorContainer = Color(0xFFd10042),
    secondaryContainer = Color(0xFF016b64),
    onSecondaryContainer = Color(0xFFD2FDFA),
)

val lightColorScheme = lightColorScheme(
    primary = Color(0xff2aadad),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF016b64),
    surface = Color(0xFFF2FAFA),
    error = Color(0xFFd10042),
    onErrorContainer = Color(0xFFFFDBDB),
    secondaryContainer = Color(0xFFD2FDFA),
    onSecondaryContainer = Color(0xFF016b64),
)

data class ColorTheme(val isDarkMode: Boolean = false)

val LocalTheme = compositionLocalOf { ColorTheme() }

@Composable
fun BersihinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme =
        if (!darkTheme) {
            lightColorScheme
        } else {
            darkColorScheme
        }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = createTypography(darkTheme),
        content = content
    )
}