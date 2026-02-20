package com.example.littlelemon

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun Profile (navController: NavHostController) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("LittleLemon", Context.MODE_PRIVATE)

    Column {

        Text("Profile Screen")

        Button(onClick = {
            sharedPref.edit().clear().apply()
            navController.navigate(Destinations.Onboarding.route) {
                popUpTo(Destinations.Home.route) { inclusive = true }
            }
        }) {
            Text("Logout")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    val navController = rememberNavController()
    Profile(navController)
}