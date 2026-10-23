package com.example.corpaliv.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.corpaliv.R

// Acompaña el mensaje escrito de validación; el color no es la única señal.
@Composable
fun ErrorIcon() {
    Icon(
        painter = painterResource(R.drawable.ic_error),
        contentDescription = "Campo con error",
        tint = MaterialTheme.colorScheme.error
    )
}
