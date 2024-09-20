package com.sabbirosa.eduvault.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sabbirosa.eduvault.backend.local.models.Session
import com.sabbirosa.eduvault.backend.viewmodels.AuthenticationVM
import com.sabbirosa.eduvault.backend.viewmodels.ResourceVM
import com.sabbirosa.eduvault.components.CircularLoadingBasic
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable

fun Profile(session: Session?){
    val resourcevm: ResourceVM = hiltViewModel()

    var isLoading by rememberSaveable {
        mutableStateOf(true)
    }
    val scope = rememberCoroutineScope()
    var uploadedResources by remember{
        mutableIntStateOf(0)
    }
    var savedResources by remember{
        mutableIntStateOf(0)
    }


    LaunchedEffect(session) {
        scope.launch {
            isLoading = true
            delay(1000)
            if(session != null){
                resourcevm.getUserSavedResources(
                    userId = session.userId.toInt(),
                    setList = { list ->
                        savedResources = list.size
                    }
                )
                resourcevm.getCreatedResources(
                    userId = session.userId.toInt(),
                    setList = { list ->
                        uploadedResources = list.size
                    }
                )
            }
            isLoading = false
        }
    }




    if(session == null){
        Text("No Login Data")
    }
    else{
        Box(
            modifier = Modifier
                .padding(top = 100.dp)
                .fillMaxSize()
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Card for Personal Information
                Card(
                    elevation = CardDefaults.cardElevation(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        item{
                            Text(
                                text = "User Information",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

//                        Text(text = "Name: ${session.fullName}", style = MaterialTheme.typography.bodyLarge)
//                        Text(text = "Email: ${session.email}", style = MaterialTheme.typography.bodyLarge)
//                        Text(text = "Department: ${session.department}", style = MaterialTheme.typography.bodyLarge)
//                        Text(text = "Student ID: ${session.studentId}", style = MaterialTheme.typography.bodyLarge)
//                        Text(text = "User ID: ${session.userId}", style = MaterialTheme.typography.bodyLarge)
                            CardWithTitle(title = "Name", info = session.fullName)
                            CardWithTitle(title = "Email", info = session.email)
                            CardWithTitle(title = "Department", info = session.department)
                            CardWithTitle(title = "Student ID", info = session.studentId)
                            CardWithTitle(title = "User ID", info = session.userId)
                            CardWithTitle(title = "Uploaded Resources", info = uploadedResources.toString())
                            CardWithTitle(title = "Saved Resources", info = savedResources.toString())
                        }
                    }
                }


            }

        }
    }

}

@Composable
fun CardWithTitle(
    title: String,
    info: String
){
    Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(bottom = 4.dp)
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Text(
            text = info,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
    }
}