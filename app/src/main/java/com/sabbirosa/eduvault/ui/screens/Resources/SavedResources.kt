package com.sabbirosa.eduvault.ui.screens.Resources

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sabbirosa.eduvault.backend.filterByCourseCode
import com.sabbirosa.eduvault.backend.filterByDescription
import com.sabbirosa.eduvault.backend.filterByTitle
import com.sabbirosa.eduvault.backend.models.Resource
import com.sabbirosa.eduvault.backend.viewmodels.ResourceVM
import com.sabbirosa.eduvault.components.CircularLoadingBasic
import com.sabbirosa.eduvault.components.DropDownCard
import com.sabbirosa.eduvault.components.ResourceButton
import com.sabbirosa.eduvault.components.SearchBar
import com.sabbirosa.eduvault.ui.theme.paletteBlue1
import com.sabbirosa.eduvault.ui.theme.paletteBlue4
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SavedResourceScreen(userid: String, resourcevm: ResourceVM) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(true) }

    var savedResources by remember {
        mutableStateOf(listOf<Resource>())
    }

    var trigger by remember {
        mutableStateOf(true)
    }

    var filteredResourceList by remember {
        mutableStateOf(savedResources)
    }

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    val filters = listOf(
        "Title", "Description", "Course Code"
    )

    var currentFilter by remember {
        mutableStateOf(filters[0])
    }

    LaunchedEffect(savedResources, trigger, currentFilter, searchQuery) {
        scope.launch {
            resourcevm.getUserSavedResources(userId = userid.toInt(), setList = { list ->
                savedResources = list
            })
            when (currentFilter) {
                filters[0] -> {
                    filteredResourceList = filterByTitle(
                        resources = savedResources, title = searchQuery.text
                    )
                }

                filters[1] -> {
                    filteredResourceList = filterByDescription(
                        resources = savedResources, description = searchQuery.text
                    )
                }

                filters[2] -> {
                    filteredResourceList = filterByCourseCode(
                        resources = savedResources, courseCode = searchQuery.text
                    )
                }

                else -> false
            }
            delay(200)
            isLoading = false
        }
    }

    Column(
        modifier = Modifier.padding(bottom = 40.dp)
    ) {
        Text(
            text = "EduVault",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(top = 28.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier.padding(top = 20.dp)
        ) {
            SearchBar(
                action = { query ->
                    searchQuery = query
                },
                height = 40.dp,
                textSize = 13.sp,
                paddingStart = 20.dp,
                paddingEnd = 5.dp,
                text = "Search Resources",
                weight = 0.45f

            )

            DropDownCard(
                dropdownItems = filters,
                startPadding = 10.dp,
                endPadding = 20.dp,
                topPadding = 15.dp,
                onItemClick = { current ->
                    currentFilter = current
                },
                weight = 1f
            )
        }

    }
    if (isLoading) {
        CircularLoadingBasic("Loading Saved Resources...")
    } else {
        LazyColumn(
            modifier = Modifier.padding(top = 170.dp, bottom = 20.dp)
        ) {
            items(
                if (searchQuery.text == "") savedResources
                else filteredResourceList
            ) { resource ->
                SavedResource(resource = resource, onUnSave = {
                    scope.launch {
                        resourcevm.unsaveResource(userId = userid.toInt(),
                            resourceId = resource.id!!.toInt(),
                            onSubmit = { status ->
                                // Show the toast directly in the callback on the main thread
                                (context as? Activity)?.runOnUiThread {
                                    Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
                                }
                                trigger = !trigger
                            })
                    }
                })
            }
        }
    }
}

@Composable
fun SavedResource(
    resource: Resource, onUnSave: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ResourceButton(resource)

        IconButton(
            onClick = { onUnSave(resource.id!!.toInt()) },
            modifier = Modifier.size(56.dp) // Adjust size as needed
        ) {
            Icon(
                imageVector = Icons.Filled.BookmarkRemove, // Replace with your save icon resource
                contentDescription = "Save", tint = Color(0xFF5A5A5A) // Change color as needed
            )
        }
    }
}