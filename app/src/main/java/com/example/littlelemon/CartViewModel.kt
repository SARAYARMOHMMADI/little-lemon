package com.example.littlelemon

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CartViewModel : ViewModel() {
    var cartItems = mutableStateListOf<MenuItemRoom>()
        private set
    var cartCount = mutableStateOf(0)
        private set

    fun addToCart(item: MenuItemRoom) {
        cartItems.add(item)
        cartCount.value += 1
    }

    fun getSubtotal(): Double {
        return cartItems.sumOf { it.price.toDouble() }
    }
}