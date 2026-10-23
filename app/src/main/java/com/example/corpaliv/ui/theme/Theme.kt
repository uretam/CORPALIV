package com.example.corpaliv.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// La misma paleta verde se aplica a los controles de Material 3.
@Composable
fun CORPALIVTheme(content: @Composable () -> Unit) {
    val colores = if (isSystemInDarkTheme()) {
        darkColorScheme(
            primary = Color(0xFF9DD6B3),
            onPrimary = Color(0xFF113D2B),
            primaryContainer = Color(0xFF275540),
            onPrimaryContainer = Color(0xFFCBEDD7),
            secondary = Color(0xFFB4D6BE),
            onSecondary = Color(0xFF203D2C),
            secondaryContainer = Color(0xFF365442),
            onSecondaryContainer = Color(0xFFD0EBD6)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF176B46),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFD6EBDD),
            onPrimaryContainer = Color(0xFF173D29),
            secondary = Color(0xFF426B50),
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFE1EDE3),
            onSecondaryContainer = Color(0xFF25442F)
        )
    }
    MaterialTheme(
        colorScheme = colores,
        content = content
    )
}
