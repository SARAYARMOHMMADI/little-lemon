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
fun MyNavigation(navController: NavHostController) {

    val context = LocalContext.current

    // SharedPreferences
    val sharedPref = context.getSharedPreferences("LittleLemon", Context.MODE_PRIVATE)
    val isLoggedIn = sharedPref.getString("firstName", null) != null

    //  Database instance
    val database = AppDatabase.getInstance(context)

    //  ViewModel with Factory
    val cartViewModel: CartViewModel = viewModel(
        factory = CartViewModelFactory(database)
    )

    val startDestination = if (isLoggedIn) {
        Destinations.Home
    } else {
        Destinations.Onboarding
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Destinations.Onboarding) {
            Onboarding(navController)
        }

        composable(Destinations.Home) {
            Home(navController, cartViewModel)
        }

        composable(Destinations.Profile) {
            Profile(navController)
        }

        composable(Destinations.Cart) {
            CartScreen(navController, cartViewModel)
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

            val id = requireNotNull(
                backStackEntry.arguments?.getInt("itemId")
            )

            DetailScreen(
                navController = navController,
                id = id,
                cartViewModel = cartViewModel
            )
        }
    }
}