package com.sabbirosa.eduvault.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sabbirosa.eduvault.backend.viewmodels.ResourceVM
import com.sabbirosa.eduvault.components.BottomNavigation
import com.sabbirosa.eduvault.components.models.BottomNavItem
import com.sabbirosa.eduvault.ui.screens.Resources.MyVaultScreen
import com.sabbirosa.eduvault.ui.screens.Resources.PublicVaultScreen
import com.sabbirosa.eduvault.ui.screens.Resources.SavedResourceScreen
import com.sabbirosa.eduvault.ui.screens.Resources.SubmitResourceForm

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Resources(userid: String){

    var selectedIndexBotNav by rememberSaveable {
        mutableIntStateOf(0)
    }
    val bottomNavList by rememberSaveable {
        mutableStateOf(BottomNavItem.vaultBottomNavItemList)
    }
    val resourcevm: ResourceVM = hiltViewModel()
    val navController = rememberNavController()
    var scrollState = rememberScrollState()
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            BottomNavigation(
                items = bottomNavList,
                selectedIndex = selectedIndexBotNav
            ) { index ->
                selectedIndexBotNav = index
                navController.navigate(bottomNavList[index].title)

            }
        }
    ) {
        NavHost(navController = navController, startDestination = "My Vault" ){
            composable("My Vault"){
                MyVaultScreen(navController, userid, resourcevm)
            }
            composable("Public Vault"){
                PublicVaultScreen(userid, resourcevm)
            }
            composable("Saved Resources"){
                SavedResourceScreen(userid, resourcevm)
            }
            composable("Submit Resource") {
                SubmitResourceForm(userid, resourcevm, navController)
            }
            composable("Submit Resource/{resourceId}") { backStackEntry ->
                // Retrieve the resourceId from the back stack entry
                val Id = backStackEntry.arguments?.getString("resourceId")

                // Pass the resourceId to the form
                SubmitResourceForm(userid, resourcevm, navController, resourceId = Id!!)
            }
        }

    }



}