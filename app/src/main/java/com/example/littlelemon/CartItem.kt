package com.example.littlelemon

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

data class CartItem(
    val item: MenuItemRoom,
    var quantity: MutableState<Int>
)