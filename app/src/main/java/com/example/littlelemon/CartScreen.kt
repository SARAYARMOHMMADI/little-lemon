package com.example.littlelemon

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.ui.res.painterResource
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage


@Composable
fun CartScreen(navController: NavController, cartViewModel: CartViewModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        CartHeader(navController, cartViewModel.getTotalCount())

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            CartContent(cartViewModel)
        }

        CheckoutButton()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartHeader(navController: NavController,  cartCount: Int) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = {
            navController.popBackStack()
        }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF495E57))
        }

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.height(40.dp)
        )

        IconButton(onClick = {
            navController.navigate("cart")
        }) {
            BadgedBox(
                badge = {
                    if (cartCount > 0) {
                        Badge {
                            Text(cartCount.toString())
                        }
                    }
                }
            ){
                Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color(0xFF495E57))
            }
        }
    }
}

@Composable
fun CartContent(cartViewModel: CartViewModel) {

    Column(modifier = Modifier.padding(16.dp)) {

        Text("Order Summary", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))

        CartItemsList(cartViewModel)

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        PriceSummary(cartViewModel)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CartItemsList(cartViewModel: CartViewModel) {

    Column {

        cartViewModel.cartItems.forEach { cartItem ->

            val imageUrl = cartItem.item.image
                .replace("github.com", "raw.githubusercontent.com")
                .replace("/blob/", "/")
                .replace("?raw=true", "")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                GlideImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(70.dp)
                        .background(Color.LightGray, RoundedCornerShape(12.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(cartItem.item.title)
                    Text("$${cartItem.item.price}", color = Color.Gray)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {

                    IconButton(onClick = {
                        cartViewModel.decrease(cartItem)
                    }) {
                        Text("-")
                    }

                    Text(cartItem.quantity.toString())

                    IconButton(onClick = {
                        cartViewModel.increase(cartItem)
                    }) {
                        Text("+")
                    }
                }

                // 🔥 حذف
                IconButton(onClick = {
                    cartViewModel.remove(cartItem)
                }) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun PriceSummary(cartViewModel: CartViewModel) {

    val subtotal = cartViewModel.getSubtotal()
    val delivery = 10.0
    val service = 2.0
    val total = subtotal + delivery + service

    Column {

        RowPrice("Subtotal", subtotal)
        RowPrice("Delivery", delivery)
        RowPrice("Service", service)

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        RowPrice("Total", total, true)
    }
}

@Composable
fun RowPrice(label: String, value: Double, bold: Boolean = false) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(label)

        Text(
            "$%.2f".format(value),
            fontWeight = if (bold) androidx.compose.ui.text.font.FontWeight.Bold
            else androidx.compose.ui.text.font.FontWeight.Normal
        )
    }
}

@Composable
fun CheckoutButton() {

    Button(
        onClick = { },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(50.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4CE14))
    ) {
        Text("Checkout", color = Color.Black)
    }
}