package com.sabbirosa.eduvault.ui.screens.Resources

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.sabbirosa.eduvault.backend.models.Resource
import com.sabbirosa.eduvault.backend.viewmodels.ResourceVM
import com.sabbirosa.eduvault.components.CircularLoadingBasic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SubmitResourceForm(
    userID: String,
    resourcevm: ResourceVM,
    navController: NavHostController,
    resourceId: String = ""
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var submitStatus by remember {
        mutableStateOf("Submit Failed")
    }

    val (title, setTitle) = rememberSaveable { mutableStateOf("") }
    val (description, setDescription) = rememberSaveable { mutableStateOf("") }
    val (publicUrl, setPublicUrl) = rememberSaveable { mutableStateOf("") }
    val (courseCode, setCourseCode) = rememberSaveable { mutableStateOf("") }
    val (category, setCategory) = rememberSaveable { mutableStateOf("Notes") }
    var isLoading by remember{ mutableStateOf(true) }

    val pageTitle = if(resourceId == "") "Submit Resource"
                    else "Edit Resource"
    val subText = if(resourceId == "") "Add a resource here with proper details"
                    else "Update your resources with proper details"

    LaunchedEffect(resourceId) {
        scope.launch {
            if(resourceId != ""){
                resourcevm.getResourceById(resourceId.toInt()){ value ->
                    if(value != null){
                        setTitle(value.title!!)
                        setDescription(value.description!!)
                        setCategory(value.category!!)
                        setCourseCode(value.course_code!!)
                        setPublicUrl(value.public_url!!)
                    }
                }
                delay(200)
                isLoading = false
            }
            else{
                isLoading = false
            }
        }
    }


    if(isLoading){
        CircularLoadingBasic("Getting Resource...")
    }
    else{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = pageTitle,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = subText,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Title Text Field
            CustomTextField(
                label = "Title",
                value = title,
                onValueChange = setTitle
            )

            // Description Text Field
            CustomTextField(
                label = "Description",
                value = description,
                onValueChange = setDescription,

                )

            // Category Dropdown
            CategoryDropdown(
                current = if (resourceId != "") category else "Notes",
                onChange = { value ->
                    setCategory(value)
                }
            )

            // Course Code Text Field
            CustomTextField(
                label = "Course Code",
                value = courseCode,
                onValueChange = setCourseCode,

                )

            // Public URL Text Field
            CustomTextField(
                label = "Public URL eg. Drive Link",
                value = publicUrl,
                onValueChange = setPublicUrl,

                )

            // Add Resource Button
            OutlinedButton(
                onClick = {
                    scope.launch {
                        val resource = Resource(
                            title = title,
                            description = description,
                            category = category,
                            course_code = courseCode,
                            public_url = publicUrl,
                            userId = userID,
                            id = 0
                        )

                        if(resourceId != ""){

                            resourcevm.updateResource(
                                id = resourceId.toInt(),
                                resource = resource,
                                onUpdate = { status ->
                                    submitStatus = status
                                }
                            )
                        }
                        else{
                            resourcevm.createResource(
                                resource = resource,
                                onSubmit = { status ->
                                    submitStatus = status
                                }
                            )
                        }


                        withContext(Dispatchers.Main){
                            Toast.makeText(context, submitStatus, Toast.LENGTH_SHORT).show()
                        }
                        navController.navigate("My Vault")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Gray),
                enabled = (
                        title != "" &&
                                description != "" &&
                                courseCode != "" &&
                                publicUrl != ""
                        )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp),
                    )
                    Text("Upload Resource")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    label: String,
    value: String?,
    onValueChange: (String) -> Unit,
    readOnly: Boolean = false
) {
    OutlinedTextField(
        value = value ?: "",
        onValueChange = onValueChange,
        label = { Text(text = label) },
        readOnly = readOnly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            disabledTextColor = Color.Black,
            disabledLabelColor = Color.Gray,
            disabledBorderColor = Color.Gray,
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.Gray
        )
    )
}

@Composable
fun CategoryDropdown(
    current: String = "Notes",
    onChange:(String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var category by remember {
        mutableStateOf(listOf(
            "Notes",
            "Quiz Questions",
            "Lectures",
            "Outlines"
        ))
    }
    println("Current is $current")
    var selectedCategory by remember {
        mutableStateOf(category[category.indexOf(current)])
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = selectedCategory,
                    modifier = Modifier.weight(1f),
                    color = Color.Black
                )
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {

            category.forEachIndexed { _, category ->
                DropdownMenuItem(
                    onClick = {
                        selectedCategory = category
                        onChange(category)
                        expanded = false
                    },
                    text = {
                        Text(text = category)
                    }
                )
            }

        }
    }
}


