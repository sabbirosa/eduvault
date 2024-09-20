package com.sabbirosa.eduvault.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.sabbirosa.eduvault.backend.viewmodels.AuthenticationVM
import com.sabbirosa.eduvault.ui.screens.Authentication.LoginScreen
import com.sabbirosa.eduvault.ui.screens.Authentication.RegistrationScreen

@Composable
fun Authentication(authvm: AuthenticationVM, navController: NavHostController) {

    var login by rememberSaveable {
        mutableStateOf(true)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){

            if (login) {
                LoginScreen(authvm, navController)
            } else {
                RegistrationScreen(authvm, navController)
            }
            TextButton(
                onClick = {
                    login = !login
                }
            ) {
                Text(
                    text = if (login) "Register" else "Login"
                )
            }

    }


}