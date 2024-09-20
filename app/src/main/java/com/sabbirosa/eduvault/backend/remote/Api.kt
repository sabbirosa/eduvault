package com.sabbirosa.eduvault.backend.remote

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Api {

    const val URL = "https://eduvault.sabbir.co/api/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val jsonAdapter: JsonAdapter<List<Map<String, Any>>> = moshi.adapter(
        Types.newParameterizedType(List::class.java, Map::class.java)
    )

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(URL)
        .build()

    val retrofitService: EduVaultService by lazy {
        retrofit.create(EduVaultService::class.java)
    }

}