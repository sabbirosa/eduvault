package com.sabbirosa.eduvault.backend.viewmodels

import android.app.Application
import android.content.Context
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabbirosa.eduvault.backend.local.repository.SessionRepository
import com.sabbirosa.eduvault.backend.models.UserData
import com.sabbirosa.eduvault.backend.remote.Api
import com.sabbirosa.eduvault.backend.remote.EduVaultService
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
open class AuthenticationVM @Inject constructor(
    private val sessionR: SessionRepository,
    private val api: EduVaultService

) : ViewModel() {



    val allSessions = sessionR.getAllSession()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    val firstSession = allSessions.map { sessions ->
        sessions.firstOrNull()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )



    suspend fun registerUser(
        fullName: String,
        studentId: String,
        department: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Create JSON object
                val jsonObject = JSONObject().apply {
                    put("full_name", fullName)
                    put("student_id", studentId)
                    put("department", department)
                    put("email", email)
                    put("password", password)
                }

                // Convert JSON object to RequestBody
                val requestBody = jsonToRequestBody(jsonObject)

                // Make the API call
                val response = api.registerUser(requestBody).awaitResponse()

                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()

                    responseBody?.let { it ->
                        // Parse the JSON array using the adapter
                        val jsonResponse = Api.jsonAdapter.fromJson(it)

                        jsonResponse?.let { list ->
                            val message = list[0]["message"] as? String
                            val userData = list[1]["user_data"] as? Map<*, *>
                            userData?.let {
                                val userId = it["id"]?.toString()
                                println("user id is $userId")
                                val data = UserData(
                                    studentId = studentId,
                                    email = email,
                                    fullName = fullName,
                                    department = department,
                                    userId = userId!!.toFloat().toInt().toString()
                                )
                                sessionR.createSession(data)
                                println("User added to local")
                            }
                        }
                    }


                    Log.d("Registration Successful", "Registration successful, created sesstion")
                } else {
                    Log.e("Register", "Registration failed: ${response.errorBody()?.string()}")
                    Log.e("Registration data", "$fullName $studentId $department $email $password")
                }
            } catch (e: Exception) {
                Log.e("Register", "Error: ${e.message}")
            }
        }
    }

    // Function to handle user login
    suspend fun loginUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val jsonObject = JSONObject().apply {
                    put("email", email)
                    put("password", password)
                }

                val requestBody = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
                val response = api.loginUser(requestBody).awaitResponse()

                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()

                    responseBody?.let { it ->
                        // Parse the JSON array using the adapter
                        val jsonResponse = Api.jsonAdapter.fromJson(it)

                        jsonResponse?.let { list ->
                            val message = list[0]["message"] as? String
                            val userData = list[1]["user_data"] as? Map<*, *>

                            Log.d("Login", "Login successful: $message")
                            userData?.let {
                                val emailFromResponse = it["email"] as? String
                                Log.d("User Data", "Full Name: ${it["full_name"]}, Email: $emailFromResponse ${it["user_id"].toString()}")
                                val data = UserData(
                                    studentId = it["student_id"].toString(),
                                    email = it["email"].toString(),
                                    fullName = it["full_name"].toString(),
                                    department = it["department"].toString(),
                                    userId = it["id"].toString().toFloat().toInt().toString()
                                )
                                sessionR.createSession(data)
                                Log.d("Login Successful", "Login successful, created sesstion")

                            }
                        }
                    }
                } else {
                    Log.e("Login", "Login failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Login", "Error: ${e.message}")
            }
        }
    }

    suspend fun logout(){
        sessionR.deleteAllSession()
    }


    fun getUser(id: String){

    }

}