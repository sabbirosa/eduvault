package com.sabbirosa.eduvault.backend

import com.sabbirosa.eduvault.backend.models.Resource

fun filterByTitle(resources: List<Resource>, title: String): List<Resource> {
    return resources.filter { resource ->
        resource.title?.contains(title, ignoreCase = true) == true
    }
}

fun filterByDescription(resources: List<Resource>, description: String): List<Resource> {
    return resources.filter { resource ->
        resource.description?.contains(description, ignoreCase = true) == true
    }
}

fun filterByCourseCode(resources: List<Resource>, courseCode: String): List<Resource> {
    return resources.filter { resource ->
        resource.course_code?.contains(courseCode, ignoreCase = true) == true
    }
}