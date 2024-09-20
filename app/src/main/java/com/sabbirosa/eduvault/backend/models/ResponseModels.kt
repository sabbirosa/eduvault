package com.sabbirosa.eduvault.backend.models



data class ResponseString(
    val message: String
)

data class LoginResponse(
    val message: String,
    val userData: UserData
)

data class UserData(
    val userId: String,
    val fullName: String,
    val email: String,
    val studentId: String,
    val department: String
)

