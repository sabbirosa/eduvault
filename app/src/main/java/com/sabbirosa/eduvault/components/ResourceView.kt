package com.sabbirosa.eduvault.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.sabbirosa.eduvault.backend.models.Resource
import com.sabbirosa.eduvault.ui.screens.CardWithTitle

@Composable
fun ResourceDialog(
    resource: Resource,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    if (resource != null) {
        Dialog(onDismissRequest = onDismiss) {

            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 20.dp,
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Resource Details",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    ResourceTextItem(title = "Title", value = resource.title ?: "N/A")
                    ResourceTextItem(title = "Description", value = resource.description ?: "N/A")
                    ResourceTextItem(title = "Category", value = resource.category ?: "N/A")
                    ResourceTextItem(title = "Course Code", value = resource.course_code ?: "N/A")

                    // URL Section
                    val url = resource.public_url ?: "N/A"
                    if (url != "N/A") {
                        Column(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ){
                                Text(
                                    text = "Go to resource",
                                    color = Color.Blue,
                                    modifier = Modifier.clickable {
                                        openUrl(context, url)
                                    },
                                    textDecoration = TextDecoration.Underline
                                )
                                IconButton(onClick = {
                                    copyToClipboard(context, url)
                                    Toast.makeText(
                                        context,
                                        "Copied to Clipboard",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy URL"
                                    )
                                }
                            }
                        }
                    } else {
                        ResourceTextItem(title = "Public URL", value = url)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(text = "Close")
                    }
                }
            }
        }


    }
}
@Composable
fun ResourceButton(resource: Resource){
    var showResource by remember { mutableStateOf(false) }
    Button(
        onClick = {
            showResource = true
        },
        modifier = Modifier
            .fillMaxHeight(),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(Color.Transparent)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if ((resource.title?.length ?: 0) > 15) {
                    "${resource.title?.take(10)}..."
                } else {
                    resource.title ?: ""
                },
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = if ((resource.description?.length ?: 0) > 15) {
                    "${resource.description?.take(10)}..."
                } else {
                    resource.description ?: ""
                },
                color = Color.Black
            )
        }
    }
    if (showResource){
        ResourceDialog(
            resource = resource,
            onDismiss = {
                showResource = false
            }
        )
    }
}

@Composable
fun ResourceTextItem(title: String, value: String) {
    CardWithTitle(title, value)
}

fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(context, intent, null)
}

fun copyToClipboard(context: Context, text: String) {
    val clipboard = ContextCompat.getSystemService(context, android.content.ClipboardManager::class.java)
    val clip = android.content.ClipData.newPlainText("Copied URL", text)
    clipboard?.setPrimaryClip(clip)
}