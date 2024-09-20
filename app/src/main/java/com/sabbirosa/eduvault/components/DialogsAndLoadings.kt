package com.sabbirosa.eduvault.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CircularLoadingBasic(text: String = ""){
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center

    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = text,
                color = Color.Black
            )
            CircularProgressIndicator()
        }
    }
}