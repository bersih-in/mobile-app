package com.bersihin.mobileapp.ui.pages.general.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.bersihin.mobileapp.ui.components.common.FullscreenLoadingIndicator

@Composable
fun LoadingScreen() {
    FullscreenLoadingIndicator(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        Color(0xFF016b64)
                    ),
                    endY = with(LocalDensity.current) {
                        1500.dp.toPx()
                    },
                    tileMode = TileMode.Clamp
                )
            )
    )
}
