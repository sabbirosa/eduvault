package com.sabbirosa.eduvault.backend.viewmodels

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

fun jsonToRequestBody(jsonObject: JSONObject): RequestBody {
    val mediaType = "application/json; charset=utf-8".toMediaType()
    return jsonObject.toString().toRequestBody(mediaType)
}