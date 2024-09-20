package com.sabbirosa.eduvault.backend.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabbirosa.eduvault.backend.models.Resource
import com.sabbirosa.eduvault.backend.remote.EduVaultService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
open class ResourceVM @Inject constructor(
    private val api: EduVaultService

) : ViewModel() {

    private fun jsonToRequestBody(jsonObject: JSONObject): RequestBody {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        return jsonObject.toString().toRequestBody(mediaType)
    }

    fun createResource(
        resource: Resource, onSubmit: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                // Convert Resource object to JSON object
                val jsonObject = JSONObject().apply {
                    put("title", resource.title)
                    put("description", resource.description)
                    put("category", resource.category)
                    put("course_code", resource.course_code)
                    put("public_url", resource.public_url)
                    put("user_id", resource.userId!!.toInt())
                }

                println(jsonObject.toString())

                // Convert JSON object to RequestBody
                val requestBody = jsonToRequestBody(jsonObject)


                // Make the API call
                val response = api.createResource(requestBody).awaitResponse()

                if (response.isSuccessful) {
                    Log.d(
                        "CreateResource",
                        "Resource created successfully: ${response.body()?.message}"
                    )
                    onSubmit("Submit Successful")
                } else {
                    Log.e(
                        "CreateResource",
                        "Failed to create resource: ${response.errorBody()?.string()}"
                    )
                    onSubmit("Submit Failed")
                }
            } catch (e: Exception) {
                Log.e("CreateResource", "Error: ${e.message}")
            }
        }
    }

    private val _resources = MutableLiveData<List<Resource>>()
    val resources: LiveData<List<Resource>> = _resources

    private val _resourceCount = MutableLiveData<Int>()
    val resourceCount: LiveData<Int> = _resourceCount

    suspend fun getCreatedResources(userId: Int, setList: (List<Resource>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.getCreatedResources(userId).awaitResponse()
                if (response.isSuccessful) {
                    response.body()?.let { resourceList ->
                        _resources.postValue(resourceList)
                        _resourceCount.postValue(resourceList.size) // Post the size of the list
                        Log.d(
                            "ResourceVM-Success",
                            "Number of resources: ${resourceList.size} ${resourceList[0].title}"
                        )
                        setList(resourceList)
                    }

                } else {
                    // Handle specific 404 case
                    if (response.code() == 404) {
                        Log.d("ResourceVM404", "No saved resources found")
                    } else {
                        Log.e(
                            "ResourceVM",
                            "Error fetching resources: ${response.errorBody()?.string()}"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("ResourceVM-Exception", "Exception: ${e.message}")
            }
        }
    }

    fun getResourceById(resourceId: Int, setResource: (Resource?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.getResourceById(resourceId).awaitResponse()
                if (response.isSuccessful) {
                    // If response is successful, pass the single resource to the lambda
                    response.body()?.let { resource ->
                        Log.d("ResourceVM-Success", "Resource title: ${resource.title}")
                        setResource(resource)
                    } ?: run {
                        // In case the response body is null, still pass null to the lambda
                        Log.e("ResourceVM", "Response body is null")
                        setResource(null)
                    }
                } else {
                    // Handle specific cases such as 404 or others
                    if (response.code() == 404) {
                        Log.d("ResourceVM404", "No resource found")
                    } else {
                        Log.e(
                            "ResourceVM",
                            "Error fetching resource: ${response.errorBody()?.string()}"
                        )
                    }
                    setResource(null) // Pass null to indicate failure
                }
            } catch (e: Exception) {
                // Handle any exception that occurs during the network call
                Log.e("ResourceVM-Exception", "Exception: ${e.message}")
                setResource(null) // Pass null to indicate an exception
            }
        }
    }

    fun updateResource(
        id: Int, resource: Resource, onUpdate: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Convert Resource object to JSON object
                val jsonObject = JSONObject().apply {
                    put("title", resource.title)
                    put("description", resource.description)
                    put("category", resource.category)
                    put("course_code", resource.course_code)
                    put("public_url", resource.public_url)
                }

                println("Updating resource: $jsonObject")

                // Convert JSON object to RequestBody
                val requestBody = jsonToRequestBody(jsonObject)

                // Make the API call
                val response = api.updateResource(id, requestBody).awaitResponse()

                if (response.isSuccessful) {
                    Log.d(
                        "UpdateResource",
                        "Resource updated successfully: ${response.body()?.message}"
                    )
                    onUpdate("Update Successful")
                } else {
                    Log.e(
                        "UpdateResource",
                        "Failed to update resource: ${response.errorBody()?.string()}"
                    )
                    onUpdate("Update Failed")
                }
            } catch (e: Exception) {
                Log.e("UpdateResource", "Error: ${e.message}")
                onUpdate("Error: ${e.message}")
            }
        }
    }

    fun deleteResource(id: Int, onSubmit: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.deleteResource(id).awaitResponse()
                if (response.isSuccessful) {
                    Log.d("ResourceVM-Success", "Resource deleted successfully")
                    onSubmit("Deleted") // Pass success message
                } else {
                    Log.e(
                        "ResourceVM", "Error deleting resource: ${response.errorBody()?.string()}"
                    )
                    onSubmit("Failed to delete") // Pass failure message
                }
            } catch (e: Exception) {
                Log.e("ResourceVM-Exception", "Exception: ${e.message}")
                onSubmit("Failed to delete") // Pass failure message on exception
            }
        }
    }

    suspend fun getAllResources(setList: (List<Resource>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.getResources().awaitResponse()
                if (response.isSuccessful) {
                    response.body()?.let { resourceList ->
                        Log.d("ResourceVM-Success", "Fetched resources: ${resourceList.size}")
                        setList(resourceList) // Pass the resource list to the callback
                    } ?: run {
                        Log.e("ResourceVM", "No resources found")
                        setList(emptyList()) // Handle case where body is null
                    }
                } else {
                    Log.e(
                        "ResourceVM", "Error fetching resources: ${response.errorBody()?.string()}"
                    )
                    setList(emptyList()) // Return empty list on error
                }
            } catch (e: Exception) {
                Log.e("ResourceVM-Exception", "Exception: ${e.message}")
                setList(emptyList()) // Return empty list on exception
            }
        }
    }

    suspend fun saveResource(userId: Int, resourceId: Int, onSubmit: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            // Create the request body
            val requestBody = JSONObject().apply {
                put("user_id", userId)
                put("resource_id", resourceId)
            }.toString().toRequestBody("application/json".toMediaTypeOrNull())

            try {
                val response = api.saveResource(requestBody).awaitResponse()
                if (response.isSuccessful) {
                    // Return success message
                    Log.d("ResourceVM-Success", "Resource saved successfully.")
                    onSubmit("Resource saved successfully.")
                } else {
                    // Handle error response
                    val errorBody = response.errorBody()?.string()
                    val errorMessage =
                        extractMessageFromError(errorBody) ?: "Failed to save resource."
                    Log.e("ResourceVM", "Error saving resource: $errorMessage")
                    onSubmit(errorMessage) // Pass the specific error message
                }
            } catch (e: Exception) {
                Log.e("ResourceVM-Exception", "Exception: ${e.message}")
                onSubmit("Failed to save resource.")
            }
        }
    }

    private fun extractMessageFromError(errorBody: String?): String? {
        return try {
            val jsonObject = JSONObject(errorBody)
            jsonObject.getString("message")
        } catch (e: Exception) {
            Log.e("ResourceVM", "Error parsing error message: ${e.message}")
            null
        }
    }

    suspend fun getUserSavedResources(userId: Int, setList: (List<Resource>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.getUserSavedResources(userId).awaitResponse()
                if (response.isSuccessful) {
                    response.body()?.let { resourceList ->
                        Log.d(
                            "ResourceVM-Success", "Number of saved resources: ${resourceList.size}"
                        )
                        setList(resourceList) // Pass the resource list to the callback
                    } ?: run {
                        Log.e("ResourceVM", "Error: Response body is null.")
                    }
                } else {
                    Log.e(
                        "ResourceVM",
                        "Error fetching saved resources: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("ResourceVM-Exception", "Exception: ${e.message}")
            }
        }
    }

    fun unsaveResource(userId: Int, resourceId: Int, onSubmit: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            // Create the request body
            val requestBody = JSONObject().apply {
                put("user_id", userId)
                put("resource_id", resourceId)
            }.toString().toRequestBody("application/json".toMediaTypeOrNull())

            try {
                val response = api.unsaveResource(requestBody).awaitResponse()
                if (response.isSuccessful) {
                    Log.d("ResourceVM-Success", "Resource unsaved successfully.")
                    onSubmit("Resource unsaved successfully.")
                } else {
                    Log.e(
                        "ResourceVM", "Error unsaving resource: ${response.errorBody()?.string()}"
                    )
                    onSubmit("Failed to unsave resource.")
                }
            } catch (e: Exception) {
                Log.e("ResourceVM-Exception", "Exception: ${e.message}")
                onSubmit("Failed to unsave resource.")
            }
        }
    }
}

