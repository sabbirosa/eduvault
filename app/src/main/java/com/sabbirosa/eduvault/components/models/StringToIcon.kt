package com.sabbirosa.eduvault.components.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.MoveToInbox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.outlined.AllInbox
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.MoveToInbox
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SaveAlt
import androidx.compose.ui.graphics.vector.ImageVector

val map = mutableMapOf(

    "filled_person" to Icons.Filled.Person,

    "filled_apartment" to Icons.Filled.Apartment,

    "auto_mirrored_filled_login" to Icons.AutoMirrored.Filled.Login,
    "auto_mirrored_filled_logout" to Icons.AutoMirrored.Filled.Logout,

    "filled_submit_resource" to Icons.Filled.CloudUpload,

    "filledMyVault" to Icons.Filled.MoveToInbox,
    "filledPublicVault" to Icons.Filled.AllInbox,
    "filledSavedResources" to Icons.Filled.SaveAlt,

    ///////////////////////////////////////////

    "outlined_person" to Icons.Outlined.Person,

    "outlined_apartment" to Icons.Outlined.Apartment,

    "auto_mirrored_outlined_login" to Icons.AutoMirrored.Outlined.Login,
    "auto_mirrored_outlined_logout" to Icons.AutoMirrored.Outlined.Logout,

    "outlined_submit_resource" to Icons.Outlined.CloudUpload,

    "outlinedMyVault" to Icons.Outlined.MoveToInbox,
    "outlinedPublicVault" to Icons.Outlined.AllInbox,
    "outlinedSavedResources" to Icons.Outlined.SaveAlt
)

fun IconMap(icon: String): ImageVector{
    val value = map[icon]
    if (value!= null){
        return value
    }
    else{
        return Icons.Outlined.Error
    }
}
