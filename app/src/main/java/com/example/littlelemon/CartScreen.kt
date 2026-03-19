package com.example.littlelemon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CartScreen(navController: NavController, cartViewModel: CartViewModel) {

    val items = cartViewModel.cartItems
    val subtotal = cartViewModel.getSubtotal()
    val delivery = 2.0
    val service = 1.0
    val total = subtotal + delivery + service

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {

        // 🔙 Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }

            Text("Order Summary", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.width(24.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🧾 Items
        Text("Items", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        items.forEach { item ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text("1 x ${item.title}")

                Text("$${item.price}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Divider()

        Spacer(modifier = Modifier.height(16.dp))

        // 💰 Summary
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Text("Subtotal")
            Text("$${"%.2f".format(subtotal)}")
        }

        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Text("Delivery")
            Text("$${"%.2f".format(delivery)}")
        }

        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Text("Service")
            Text("$${"%.2f".format(service)}")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Text("Total", style = MaterialTheme.typography.titleMedium)
            Text("$${"%.2f".format(total)}", color = Color(0xFF495E57))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🟡 Checkout Button
        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4CE14))
        ) {
            Text("Checkout", color = Color.Black)
        }
    }
}