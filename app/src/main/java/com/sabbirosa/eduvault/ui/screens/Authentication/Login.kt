package com.sabbirosa.eduvault.ui.screens.Authentication

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.sabbirosa.eduvault.backend.viewmodels.AuthenticationVM
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(authvm: AuthenticationVM, navController: NavHostController) {
    val (email, setEmail) = rememberSaveable { mutableStateOf("") }
    var (password, setPassword) = rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var passwordVisible by remember { mutableStateOf(false) }

    val textFieldModifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 5.dp, horizontal = 20.dp)
        .border(1.dp, Color.Transparent, RectangleShape)

    Column(
        modifier = Modifier
            .padding(top = 55.dp)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "EduVault",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = email,
            onValueChange = { setEmail(it) },
            label = { Text("E-mail") },
            modifier = textFieldModifier,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            shape = MaterialTheme.shapes.medium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { setPassword(it) },
            label = { Text("Password") },
            modifier = textFieldModifier,
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.VisibilityOff
                else Icons.Filled.Visibility

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                scope.launch{
                    authvm.loginUser(
                        email = email,
                        password = password
                    )
                    Toast.makeText(context, "Login Succesful!!", Toast.LENGTH_SHORT).show()
                    navController.navigate("Profile")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 100.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF5A5A5A)),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = "Login",
                color = Color.White,
                modifier = Modifier
                    .padding(vertical = 8.dp),

                )
        }
    }
}
