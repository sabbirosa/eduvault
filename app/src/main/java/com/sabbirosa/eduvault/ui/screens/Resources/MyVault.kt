package com.sabbirosa.eduvault.ui.screens.Resources

import android.widget.Toast
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sabbirosa.eduvault.backend.models.Resource
import com.sabbirosa.eduvault.backend.viewmodels.ResourceVM
import com.sabbirosa.eduvault.components.CircularLoadingBasic
import com.sabbirosa.eduvault.components.ResourceButton
import com.sabbirosa.eduvault.components.ResourceDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

@Composable
fun MyVaultScreen(navController: NavHostController, userid: String, resourcevm: ResourceVM) {

    val scope = rememberCoroutineScope()

    var resources by remember{
        mutableStateOf(listOf<Resource>())
    }
    var isLoading by remember {
        mutableStateOf(true)
    }
    var deleteStatus by remember{
        mutableStateOf("")
    }
    val context = LocalContext.current

    LaunchedEffect(resources, deleteStatus) {
        scope.launch {
            delay(500)
            resourcevm.getCreatedResources(
                userId = userid.toInt(),
                setList = { list ->
                    resources = list
                }
            )

            isLoading = false
        }
    }
    Text(
        text = "EduVault",
        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier
            .padding(top = 28.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center
    )

    if(isLoading){
        CircularLoadingBasic("Loading Resources...")
    }
    else{
        LazyColumn(
            modifier = Modifier
                .padding(top = 130.dp, bottom = 20.dp)
        ) {
            items(resources){ resource ->
//                Text(text = "Title: ${resource.title}")
//                Text(text = "Description: ${resource.description}")
//                Text(text = "Category: ${resource.category}")
//                Text(text = "Course Code: ${resource.course_code}")
//                Text(text = "URL: ${resource.public_url}")
                MyResource(
                    resource = resource,
                    edit = {
                        navController.navigate("Submit Resource/${resource.id}")
                    },
                    delete = {
                        resourcevm.deleteResource(
                            id = resource.id!!.toInt(),
                            onSubmit = { status ->
                                deleteStatus = status
                                scope.launch(Dispatchers.Main) {
                                    Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
                                }
                            }
                        )

                    }
                )
            }
        }
    }
    MovableFloatingActionButton(
        onClick = {
            navController.navigate("Submit Resource")
        }
    )

}

@Composable
fun MyResource(
    resource: Resource,
    edit: () -> Unit,
    delete: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
        ,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ResourceButton(resource)
        Row{
            Button(
                onClick = edit,
                modifier = Modifier
                    .padding(horizontal = 5.dp),
                shape= MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(Color(0xFF5A5A5A)),
            ) {
                Text("Edit")
            }
            Button(
                onClick = delete,
                modifier = Modifier
                    .padding(horizontal = 5.dp),
                shape= MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(Color(0xFF5A5A5A)),
            ) {
                Text("Delete")
            }
        }
    }

}

@Composable
fun MovableFloatingActionButton(onClick: () -> Unit) {
    // Remember the offset to allow dragging
    val offsetX = rememberSaveable { mutableStateOf(0f) }
    val offsetY = rememberSaveable { mutableStateOf(0f) }

    // Box to hold the button
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 140.dp, end = 20.dp) // To add some padding from the screen edges
    ) {
        // Small Floating Action Button with drag functionality
        FloatingActionButton(
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
                .align(Alignment.BottomEnd) // Default bottom-right position
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        // Update offset values based on the drag amount
                        change.consume() // Consume the drag event to prevent further propagation
                        offsetX.value += dragAmount.x
                        offsetY.value += dragAmount.y
                    }
                },
            shape = CircleShape
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Small floating action button.")
        }
    }
}

