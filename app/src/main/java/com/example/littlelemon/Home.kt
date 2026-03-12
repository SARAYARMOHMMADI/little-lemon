package com.example.littlelemon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import com.bumptech.glide.integration.compose.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Home(navController: NavHostController, database: AppDatabase) {

    val databaseMenuItems by database
        .menuItemDao()
        .getAll()
        .observeAsState(emptyList())

    val menuItems =  databaseMenuItems

    var searchPhrase by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }


    val filteredItems = if (searchPhrase.isNotBlank()) {
        menuItems.filter {
            it.title.contains(searchPhrase, ignoreCase = true)
        }
    } else {
        menuItems
    }
    val categoryFiltered = if (selectedCategory.isNotBlank()) {
        filteredItems.filter {
            it.category == selectedCategory
        }
    } else {
        filteredItems
    }

    LazyColumn {
        item {
            Header(navController)
        }
        item {
            HeroSection(
                searchPhrase = searchPhrase,
                onSearchChanged = { searchPhrase = it }
            )
        }
        item {
            CategorySection(
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )
        }
        items(categoryFiltered) { item ->

            MenuItem(item)
        }
    }
}



@Composable
fun Header(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo"
        )

        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "Profile",
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    navController.navigate(Destinations.Profile.route)
                }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroSection(searchPhrase: String, onSearchChanged: (String) -> Unit)
{
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF495E57))
            .padding(16.dp)
    ) {

        Text(
            text = "Little Lemon",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFF4CE14)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            ) {

                Text(
                    text = "Chicago",
                    fontSize = 20.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "We are a owned Mediterranean restaurant, focused on traditional recipes served with a modern twist",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }

            Image(
                painter = painterResource(id = R.drawable.hero_image),
                contentDescription = "Hero Image",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(16.dp))



        TextField(
            value = searchPhrase,
            onValueChange = { onSearchChanged(it) },
            placeholder = { Text("Enter search phrase") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable

fun CategorySection(selectedCategory: String, onCategorySelected: (String) -> Unit)
{
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(
            text = "ORDER FOR DELIVERY!",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                CategoryButton(
                    text = "All",
                    isSelected = selectedCategory == "",
                    onClick = { onCategorySelected("") }
                )
            }

            item {
                CategoryButton(
                    text = "Starters",
                    isSelected = selectedCategory == "starters",
                    onClick = { onCategorySelected("starters") }
                )
            }

            item {
                CategoryButton(
                    text = "Mains",
                    isSelected = selectedCategory == "mains",
                    onClick = { onCategorySelected("mains") }
                )
            }

            item {
                CategoryButton(
                    text = "Desserts",
                    isSelected = selectedCategory == "desserts",
                    onClick = { onCategorySelected("desserts") }
                )
            }

            item {
                CategoryButton(
                    text = "Drinks",
                    isSelected = selectedCategory == "drinks",
                    onClick = { onCategorySelected("drinks") }
                )
            }
        }
    }

    Divider()
}
@Composable
fun CategoryButton(text: String, isSelected: Boolean, onClick: () -> Unit) {

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor =
            if (isSelected) Color(0xFFF4CE14)
            else Color(0xFFEDEFEE),

            contentColor = Color(0xFF495E57)

        )
    ) {

        Text(
            text = text,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuItem(menuItem: MenuItemRoom) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = menuItem.title,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(menuItem.description)

            Spacer(modifier = Modifier.height(4.dp))

            Text("$${menuItem.price}")
        }

        Spacer(modifier = Modifier.width(8.dp))

        val imageUrl = menuItem.image
            .replace("github.com", "raw.githubusercontent.com")
            .replace("/blob/", "/")
            .replace("?raw=true", "")

        GlideImage(
            model = imageUrl,
            contentDescription = menuItem.title,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop,
        )
    }
    Divider(
        thickness = 1.dp,
        color = Color(0xFFEDEFEE)
    )
}
