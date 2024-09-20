package com.sabbirosa.eduvault.ui.screens.Resources

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastCbrt
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("MutableCollectionMutableState")
@Composable
fun PublicVaultScreen(userid: String, resourcevm: ResourceVM) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(true) }
    var resourceList by remember{
        mutableStateOf(listOf<Resource>())
    }
    val savedResourceList = remember { mutableStateListOf<Int>() }
    var trigger by remember {
        mutableStateOf(true)
    }
    var filteredResourceList by remember{
        mutableStateOf(resourceList)
    }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val filters = listOf(
        "Title",
        "Description",
        "Course Code"
    )
    var currentFilter by remember{
        mutableStateOf(filters[0])
    }

    LaunchedEffect(resourceList, trigger, currentFilter, searchQuery) {
        scope.launch {
            resourcevm.getAllResources { list ->
                resourceList = list
            }
            resourcevm.getUserSavedResources(
                userId = userid.toInt(),
                setList = { list ->
                    list.forEach { r ->
                        savedResourceList.add(r.id!!)
                    }
                }
            )

            when(currentFilter){
                filters[0] ->{
                    filteredResourceList = filterByTitle(
                        resources = resourceList,
                        title = searchQuery.text
                    )
                }
                filters[1] ->{
                    filteredResourceList = filterByDescription(
                        resources = resourceList,
                        description = searchQuery.text
                    )
                }
                filters[2] ->{
                    filteredResourceList = filterByCourseCode(
                        resources = resourceList,
                        courseCode = searchQuery.text
                    )
                }
                else -> false
            }

            delay(500)
            println(savedResourceList)
            isLoading = false
        }
    }
    Column(
        modifier = Modifier
            .padding(bottom = 40.dp)
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
            modifier = Modifier
                .padding(top = 20.dp)
        ){
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

    if(isLoading){
        CircularLoadingBasic("Loading Resources...")
    }
    else{
        LazyColumn(
            modifier = Modifier
                .padding(top = 170.dp, bottom = 20.dp)
        ) {
            items(
                if (searchQuery.text == "") resourceList
                else filteredResourceList
            ) { resource ->
                PublicReSource(
                    resource = resource,
                    onSave = {
                        scope.launch {
                            resourcevm.saveResource(
                                userId = userid.toInt(),
                                resourceId = resource.id!!.toInt(),
                                onSubmit = { status ->
                                    // Show the toast directly in the callback on the main thread
                                    (context as? Activity)?.runOnUiThread {
                                        Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                            trigger = !trigger
                        }
                    },
                    saved = savedResourceList.contains(resource.id)
                )
            }
        }
    }
}

@Composable
fun PublicReSource(
    resource: Resource,
    onSave: (Int) -> Unit,
    saved: Boolean
){
    println(saved)
    val icon = if (saved) Icons.Filled.Done else Icons.Outlined.Bookmark
    Row(
        modifier =Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        ResourceButton(resource)

        IconButton(
            onClick = { onSave(resource.id!!.toInt()) },
            modifier = Modifier.size(56.dp),
            enabled = !saved
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Save",
                tint = Color(0xFF5A5A5A) ,
            )
        }

    }
}