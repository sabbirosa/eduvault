package com.sabbirosa.eduvault.components.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
sealed class BottomNavItem(
    val title: String,
    val selectedIcon: String,
    val unselectedIcon: String,
): Parcelable {

    @Parcelize
    object MyVault: BottomNavItem(
        title = "My Vault",
        selectedIcon = "filledMyVault",
        unselectedIcon = "outlinedMyVault",
        )
    @Parcelize
    object PublicVault: BottomNavItem(
        title = "Public Vault",
        selectedIcon = "filledPublicVault",
        unselectedIcon = "outlinedPublicVault",
    )
    @Parcelize
    object SavedResources: BottomNavItem(
        title = "Saved Resources",
        selectedIcon = "filledSavedResources",
        unselectedIcon = "outlinedSavedResources",
    )

    companion object{
        val vaultBottomNavItemList  = listOf(
            MyVault,
            PublicVault,
            SavedResources
        )
    }



}
