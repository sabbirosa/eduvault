package com.sabbirosa.eduvault.backend.models

data class Resource(
    val id: Int?,
    val title: String?,
    val description: String?,
    val category: String?,
    val course_code: String?,
    val public_url: String?,
    val userId: String?
)

