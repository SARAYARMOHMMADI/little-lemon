package com.example.littlelemon

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CartViewModel : ViewModel() {

    var cartItems = mutableStateListOf<CartItem>()
        private set


    fun addToCart(item: MenuItemRoom, quantity: Int) {

        val existing = cartItems.find { it.item.id == item.id }

        if (existing != null) {
            existing.quantity.value += quantity
        } else {
            cartItems.add(
                CartItem(item, mutableStateOf(quantity))
            )
        }
    }

    fun increase(item: CartItem) {
        item.quantity.value++
    }

    fun decrease(item: CartItem) {
        if (item.quantity.value > 1) {
            item.quantity.value--
        } else {
            cartItems.remove(item)
        }
    }

    fun remove(item: CartItem) {
        cartItems.remove(item)
    }

    fun getSubtotal(): Double {
        return cartItems.sumOf {
            it.item.price.toDouble() * it.quantity.value
        }
    }

    fun getTotalCount(): Int {
        return cartItems.sumOf { it.quantity.value }
    }
}
