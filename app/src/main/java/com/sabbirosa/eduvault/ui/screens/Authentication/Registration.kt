package com.sabbirosa.eduvault.ui.screens.Authentication

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.sabbirosa.eduvault.backend.viewmodels.AuthenticationVM
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(registervm: AuthenticationVM, navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }



    val textFieldModifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 5.dp, horizontal = 20.dp)
        .border(1.dp, Color.Transparent, RectangleShape)

    Text(
        text = "EduVault",
        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier
            .padding(bottom = 32.dp, top = 55.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center
    )

    Text(
        text = "Registration",
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier
            .padding(bottom = 32.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center
    )

    LazyColumn(
        modifier = Modifier

            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        item{

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = textFieldModifier,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                shape = MaterialTheme.shapes.medium,
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = studentId,
                onValueChange = { studentId = it },
                label = { Text("Student ID") },
                modifier = textFieldModifier,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                shape = MaterialTheme.shapes.medium,
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = department,
                onValueChange = { department = it.uppercase() },
                label = { Text("Department") },
                modifier = textFieldModifier,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                shape = MaterialTheme.shapes.medium,
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
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
                onValueChange = { password=  it },
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
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                modifier = textFieldModifier,
                shape = MaterialTheme.shapes.medium,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (confirmPasswordVisible)
                        Icons.Filled.VisibilityOff
                    else Icons.Filled.Visibility

                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = image, contentDescription = null)
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Register Button
            Button(
                onClick = {
                    scope.launch {
                        registervm.registerUser(
                            fullName = name,
                            studentId = studentId,
                            department = department,
                            email = email,
                            password = password
                        )
                        Toast.makeText(context, "Registration Succesful!!", Toast.LENGTH_SHORT)
                            .show()

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
                    text = "Register",
                    color = Color.White,
                    modifier = Modifier
                        .padding(vertical = 8.dp),

                    )
            }
        }
    }
}
