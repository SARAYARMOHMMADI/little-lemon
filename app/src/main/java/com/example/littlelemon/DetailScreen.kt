package com.example.littlelemon


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun DetailScreen(navController: NavController, id: Int, database: AppDatabase,cartViewModel: CartViewModel) {

    val item by database.menuItemDao()
        .getById(id)
        .observeAsState(initial = null)

    item?.let { menuItem ->

        val imageUrl = menuItem.image
            .replace("github.com", "raw.githubusercontent.com")
            .replace("/blob/", "/")
            .replace("?raw=true", "")


        var selected by remember { mutableStateOf("") }
        var count by remember { mutableStateOf(1) }

        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 100.dp)
            ) {
                DetailHeader(navController, cartCount = cartViewModel.getTotalCount())
                DetailImage(imageUrl)
                DetailContent(menuItem = menuItem)
            }

            DetailBottomBar(
                price = menuItem.price,
                count = count,
                onIncrease = { count++ },
                onDecrease = { if (count > 1) count-- },
                onAddToCart = {
                    cartViewModel.addToCart(menuItem, count)
                    count = 1
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailHeader(navController: NavController,  cartCount: Int) {

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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailImage(imageUrl: String) {

    GlideImage(
        model = imageUrl,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun DetailContent(
    menuItem: MenuItemRoom,
) {

    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            text = menuItem.title,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = menuItem.description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
 }
}


@Composable
fun DetailBottomBar(
    price: String,
    count: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = onDecrease,
                colors = buttonColors(containerColor = Color(0xFF495E57))
            ) {
                Text("-")
            }

            Text(
                text = count.toString(),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Button(
                onClick = onIncrease,
                colors = buttonColors(containerColor = Color(0xFF495E57))
            ) {
                Text("+")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))


        Button(
            onClick = onAddToCart,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = buttonColors(containerColor = Color(0xFFF4CE14))
        ) {
            val totalPrice = price.toDouble() * count
            Text("Add for $${"%.2f".format(totalPrice)}", color = Color.Black)
        }
    }
}


