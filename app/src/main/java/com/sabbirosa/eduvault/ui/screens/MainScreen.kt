package com.sabbirosa.eduvault.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import android.annotation.SuppressLint

import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sabbirosa.eduvault.backend.viewmodels.AuthenticationVM
import com.sabbirosa.eduvault.components.CircularLoadingBasic
import com.sabbirosa.eduvault.components.NavDrawer
import com.sabbirosa.eduvault.components.TopActionBar
import com.sabbirosa.eduvault.components.models.BottomNavItem
import com.sabbirosa.eduvault.components.models.NavDrawerItem
import com.sabbirosa.eduvault.ui.screens.Resources.SubmitResourceForm
import kotlinx.coroutines.delay


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun MainScreen(
    modifier: Modifier
){

    val authvm: AuthenticationVM = hiltViewModel()
    val allSessions by authvm.allSessions.collectAsState()
    val firstSession by authvm.firstSession.collectAsState(initial = null)
    val navController = rememberNavController()
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var scope = rememberCoroutineScope()
    var scrollState = rememberScrollState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    var selectedIndexBotNav by rememberSaveable {
        mutableIntStateOf(0)
    }
    var selectedIndexDrawer by rememberSaveable {
        mutableIntStateOf(0)
    }

    val navDrawerItemList by rememberSaveable {
        mutableStateOf(NavDrawerItem.navDrawerItems)
    }
    var loginStatus by rememberSaveable {
        mutableStateOf(false)
    }
    var isLoading by rememberSaveable {
        mutableStateOf(true)
    }


    LaunchedEffect(allSessions) {
        scope.launch {
            isLoading = true
            delay(1000)
            loginStatus = (firstSession != null)
            isLoading = false
        }
    }
    LaunchedEffect(navBackStackEntry?.destination) {
        when (navBackStackEntry?.destination?.route) {
            "Profile" -> selectedIndexDrawer = navDrawerItemList.indexOfFirst { it.title == "Profile" }
            "Resources" -> selectedIndexDrawer = navDrawerItemList.indexOfFirst { it.title == "Resources" }
            // Add other routes here if needed
        }
    }


    if (isLoading){
        CircularLoadingBasic("Checking Session...")
    }
    else{

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    NavDrawer(
                        scrollState = scrollState,
                        selectedIndex = selectedIndexDrawer,
                        onClick = { item ->
                            navController.navigate(item.title)
                            selectedIndexDrawer = navDrawerItemList.indexOf(item)
                            scope.launch {
                                drawerState.close()
                            }


                        },
                        isLogin = loginStatus

                    )
                }
            },
            gesturesEnabled = true
        ) {

            Scaffold(
                modifier = modifier,
                topBar = { TopActionBar(drawerState = drawerState, scope = scope, isLogin = loginStatus) }
            ) {
                NavHost(
                    navController = navController, startDestination =
                    if (loginStatus) "Profile" else "Login/Registration"
                )
                {
                    // Routes
                    composable("Profile") {
                        Profile(firstSession)
                    }
                    composable("Login/Registration") {
                        Authentication(authvm, navController)
                    }
                    composable("Logout") {
                        scope.launch {
                            authvm.logout()
                        }
                        Profile(firstSession)
                    }
                    composable("Resource") {
                        Resources(firstSession?.userId ?: "0")
                    }

                }
            }


        }
    }
}