package com.sabbirosa.eduvault.backend.remote

import com.sabbirosa.eduvault.backend.models.LoginResponse
import com.sabbirosa.eduvault.backend.models.Resource
import com.sabbirosa.eduvault.backend.models.ResponseString
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface EduVaultService {

    // Register a new user
    @POST("register")
    fun registerUser(@Body body: RequestBody): Call<ResponseBody>



    // User login
    @POST("login")
    fun loginUser(@Body body: RequestBody): Call<ResponseBody>

    // Create a new resource
    @POST("resources")
    fun createResource(@Body body: RequestBody): Call<ResponseString>

    @GET("resources")
    fun getResources(): Call<List<Resource>>

    @GET("resources/{id}")
    fun getResourceById(@Path("id") id: Int): Call<Resource>

    @PUT("resources/{id}")
    fun updateResource(
        @Path("id") id: Int,
        @Body requestBody: RequestBody
    ): Call<ResponseString>

    @DELETE("resources/{id}")
    fun deleteResource(@Path("id") id: Int): Call<ResponseString>

    @POST("save_resource")
    fun saveResource(@Body requestBody: RequestBody): Call<ResponseString>

    @POST("unsave_resource")
    fun unsaveResource(@Body requestBody: RequestBody): Call<ResponseString>

    @GET("user/{user_id}/created_resources")
    fun getCreatedResources(@Path("user_id") userId: Int): Call<List<Resource>>

    @GET("user/{user_id}/saved_resources")
    fun getUserSavedResources(@Path("user_id") userId: Int): Call<List<Resource>>

}
