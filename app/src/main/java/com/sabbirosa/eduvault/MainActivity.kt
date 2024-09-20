package com.sabbirosa.eduvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.sabbirosa.eduvault.ui.screens.MainScreen
import com.sabbirosa.eduvault.ui.theme.EduVaultTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduVaultTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPaddings ->
                    MainScreen(modifier = Modifier.padding(innerPaddings))
                }
            }
        }
    }
}
