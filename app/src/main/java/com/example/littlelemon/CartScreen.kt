package com.example.littlelemon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.room.Database


@Composable
fun CartScreen(navController: NavController,database: AppDatabase, cartViewModel: CartViewModel) {

    val menuItems by database
        .menuItemDao()
        .getAll()
        .observeAsState(emptyList())

    var showDialog by remember { mutableStateOf(false) }

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
            CartContent(menuItems, cartViewModel)
        }
        CheckoutButton(cartViewModel) {
            showDialog = true
        }
    }

    if (showDialog) {
        OrderSuccessDialog(
            cartViewModel,
            onDismiss = { showDialog = false }
        )
    }
}


@Composable
fun CheckoutButton(cartViewModel: CartViewModel, onClick: () -> Unit) {
    val subtotal = cartViewModel.getSubtotal()
    val delivery = 2.0
    val service = 1.0
    val total = subtotal + delivery + service
    Button(
        onClick = onClick,
        enabled = cartViewModel.cartItems.isNotEmpty(),
        shape = RoundedCornerShape(20), // 🔥 کاملاً گرد
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF4CE14),
            contentColor = Color.Black
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .height(40.dp) // 🔥 ارتفاع استاندارد
    ) {

        Text(
            text = "Checkout • $${"%.2f".format(total)}",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
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
fun Cutlery(cartViewModel: CartViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = "Cutlery",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Help reduce plastic waste. Only ask for cutlery if you need it",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Checkbox(
                checked = cartViewModel.isCutlerySelected,
                onCheckedChange = { cartViewModel.isCutlerySelected = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF495E57)
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Divider(color = Color.LightGray)
    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CartItemsList(cartViewModel: CartViewModel) {

    Column {
        //  Order Summary Title
        Text(
            text = "Order Summary",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        //  Items Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE0E0E0))
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            Text(text = "Items",
                style = MaterialTheme.typography.titleSmall)

        }
        //  Items List
        cartViewModel.cartItems.forEach { cartItem ->

            val imageUrl = cartItem.item.image
                .replace("github.com", "raw.githubusercontent.com")
                .replace("/blob/", "/")
                .replace("?raw=true", "")

            val price = cartItem.item.price.toDoubleOrNull() ?: 0.0

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.Top
            ) {

                //  Image
                GlideImage(
                    model = imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(12.dp))
                )


                Spacer(modifier = Modifier.width(12.dp))

                //  Right Section
                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    // Title
                    Text(
                        text = cartItem.item.title,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Price + Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val totalPrice = price * cartItem.quantity.value
                        // Price
                        Text(
                            text = "$%.2f".format(totalPrice),
                            color = Color.Gray
                        )

                        // Controls
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            IconButton(
                                modifier = Modifier.size(32.dp),
                                onClick = { cartViewModel.decrease(cartItem) },
                                enabled = cartItem.quantity.value > 1
                            ) {
                                Text("-")
                            }

                            Text(
                                text = cartItem.quantity.value.toString(),
                                modifier = Modifier.padding(horizontal = 6.dp)
                            )

                            IconButton(
                                modifier = Modifier.size(32.dp),
                                onClick = { cartViewModel.increase(cartItem) }
                            ) {
                                Text("+")
                            }

                            IconButton(
                                modifier = Modifier.size(32.dp),
                                onClick = { cartViewModel.remove(cartItem) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }

            Divider(
                modifier = Modifier.padding(start = 94.dp),
                color = Color(0xFFE0E0E0)
            )
        }
    }
}
@Composable
fun CartContent(menuItems: List<MenuItemRoom>, cartViewModel: CartViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Cutlery(cartViewModel)
        CartItemsList(cartViewModel)
        Divider(modifier = Modifier.padding(vertical = 16.dp))
        MoreOrderSection(menuItems, cartViewModel)
        Divider(modifier = Modifier.padding(vertical = 16.dp))
        PriceSummary(cartViewModel)
    }
}
@Composable
fun MoreOrderSection(menuItems: List<MenuItemRoom>, cartViewModel: CartViewModel) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE0E0E0))
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            Text(text = "Add More To Your Order!",
                style = MaterialTheme.typography.titleSmall)
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(menuItems) { menuItem ->
                MoreOrderItem(menuItem, cartViewModel)
            }
        }
    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MoreOrderItem(menuItem: MenuItemRoom, cartViewModel: CartViewModel) {

    val imageUrl = menuItem.image
        .replace("github.com", "raw.githubusercontent.com")
        .replace("/blob/", "/")
        .replace("?raw=true", "")

    Column(
        modifier = Modifier
            .width(260.dp)
            .padding(8.dp)
    ) {
        Row {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = menuItem.title,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = menuItem.description,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "$${menuItem.price}",
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            GlideImage(
                model = imageUrl,
                contentDescription = menuItem.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                cartViewModel.addToCart(menuItem, 1)
            },
            shape = RoundedCornerShape(20),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF495E57),
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
            modifier = Modifier
                .fillMaxWidth()
        ){
            Text(
                text = "Add",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )

        }
    }
}
@Composable
fun PriceSummary(cartViewModel: CartViewModel) {

    val subtotal = cartViewModel.getSubtotal()
    val delivery = 2.0
    val service = 1.0
    val total = subtotal + delivery + service

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {

        RowPrice("Subtotal", subtotal)

        Spacer(modifier = Modifier.height(6.dp))

        RowPrice("Delivery", delivery)

        Spacer(modifier = Modifier.height(6.dp))

        RowPrice("Service", service)

        Spacer(modifier = Modifier.height(10.dp))

        Divider(
            thickness = 1.dp,
            color = Color(0xFFE0E0E0)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Total",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Text(
                "$%.2f".format(total),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF495E57)
            )
        }
    }
}
@Composable
fun RowPrice(label: String, value: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 14.sp
        )
        Text(
            text = "$%.2f".format(value),
            fontSize = 14.sp
        )
    }
}

@Composable
fun OrderSuccessDialog(cartViewModel: CartViewModel, onDismiss: () -> Unit) {

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    cartViewModel.clearCart()   // 🔥 خالی کردن سبد
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF4CE14)
                ),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Track Order")
            }
        },
        title = {
            Text("Success!", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Your order will be with you shortly.",
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Thank you for your business.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    )
}