package com.sabbirosa.eduvault.components.models

import android.os.Parcelable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.AutoAwesomeMosaic
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Room
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.AppRegistration
import androidx.compose.material.icons.outlined.AutoAwesomeMosaic
import androidx.compose.material.icons.outlined.CalendarViewWeek
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Room
import androidx.compose.material.icons.outlined.SupervisedUserCircle
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class NavDrawerItem(
    val selectedIcon: String? = null,
    val unselectedIcon: String? = null,
    val title: String,
    val isDivider: Boolean = false,
    val isHeader: Boolean = false,
): Parcelable{

    @Parcelize
    object Profile: NavDrawerItem(
        selectedIcon = "filled_person",
        unselectedIcon = "outlined_person",
        title = "Profile"
    )
    object Login: NavDrawerItem(
        selectedIcon = "auto_mirrored_filled_login",
        unselectedIcon = "auto_mirrored_outlined_login",
        title = "Login/Registration"
    )
    @Parcelize
    object Logout: NavDrawerItem(
        selectedIcon = "auto_mirrored_filled_logout",
        unselectedIcon = "auto_mirrored_outlined_logout",
        title = "Logout"
    )
    @Parcelize
    object AboutApp: NavDrawerItem(
        selectedIcon = "filled_apartment",
        unselectedIcon = "outlined_apartment",
        title = "About App"
    )
    @Parcelize
    object Resource: NavDrawerItem(
        selectedIcon = "filled_submit_resource",
        unselectedIcon = "outlined_submit_resource",
        title = "Resource"
    )
    @Parcelize
    object Divider: NavDrawerItem(
        title = "Divider",
        isDivider = true
    )





    companion object{

        val navDrawerItems = listOf(
            Profile,
            Resource,
            Divider,
            Login,
            Logout
        )


    }
}