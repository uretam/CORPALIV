package com.example.corpaliv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.corpaliv.ui.App
import com.example.corpaliv.ui.theme.CORPALIVTheme

// MainActivity  aplica el tema e inicia App.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CORPALIVTheme {
                App()
            }
        }
    }
}