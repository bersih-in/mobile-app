package com.bersihin.mobileapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.bersihin.mobileapp.R

val fontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val PrimaryFont = FontFamily(
    Font(googleFont = GoogleFont("Lexend Deca"), fontProvider = fontProvider),
)

val SecondaryFont = FontFamily(
    Font(googleFont = GoogleFont("Lato"), fontProvider = fontProvider),
)

fun createTypography(isDarkMode: Boolean): Typography {
    return Typography(
        titleLarge = TextStyle(
            fontFamily = PrimaryFont,
            fontWeight = FontWeight.W700,
            fontSize = 34.sp,
            letterSpacing = 0.sp,
        ),
        labelLarge = TextStyle(
            fontFamily = SecondaryFont,
            fontWeight = FontWeight.W600,
            fontSize = 18.sp,
            letterSpacing = 0.sp
        ),
        labelMedium = TextStyle(
            fontFamily = SecondaryFont,
            fontWeight = FontWeight.W600,
            fontSize = 16.sp,
            letterSpacing = 0.sp
        ),
        labelSmall = TextStyle(
            fontFamily = SecondaryFont,
            fontSize = 14.sp,
            letterSpacing = 0.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = SecondaryFont,
            fontSize = 18.sp,
            letterSpacing = 0.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = SecondaryFont,
            fontSize = 16.sp,
            letterSpacing = 0.sp
        ),
        bodySmall = TextStyle(
            fontFamily = SecondaryFont,
            fontSize = 14.sp,
            letterSpacing = 0.sp
        ),
    )
}