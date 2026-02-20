package com.example.littlelemon

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun MyNavigation(navController: NavHostController) {

    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("LittleLemon", Context.MODE_PRIVATE)

    val isLoggedIn = sharedPref.getString("firstName", null) != null

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn)
            Destinations.Home.route
        else
            Destinations.Onboarding.route
    ) {

        composable(Destinations.Onboarding.route) {
            Onboarding(navController)
        }

        composable(Destinations.Home.route) {
            Home(navController)
        }

        composable(Destinations.Profile.route) {
            Profile(navController)
        }
    }
}
