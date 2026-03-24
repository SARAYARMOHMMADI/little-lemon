package com.example.littlelemon

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument

@Composable
fun MyNavigation(navController: NavHostController, database: AppDatabase) {

    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("LittleLemon", Context.MODE_PRIVATE)
    val isLoggedIn = sharedPref.getString("firstName", null) != null
    val cartViewModel: CartViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn)
            Destinations.Home
        else
            Destinations.Onboarding
    ) {

        composable(Destinations.Onboarding) {
            Onboarding(navController)
        }

        composable(Destinations.Home) {
            Home(navController, database, cartViewModel)
        }

        composable(Destinations.Profile) {
            Profile(navController)
        }

        composable(Destinations.Cart) {
            CartScreen(navController, database, cartViewModel)
        }

        composable(Destinations.Track) {
            TrackOrderScreen(navController, cartViewModel)
        }

        composable(
            route = Destinations.Detail,
            arguments = listOf(
                navArgument("itemId") { type = NavType.IntType }
            )
        ) { backStackEntry ->

            val id = backStackEntry.arguments?.getInt("itemId") ?: 0

            DetailScreen(
                navController = navController,
                id = id,
                database = database,
                cartViewModel = cartViewModel
            )
        }
    }
}