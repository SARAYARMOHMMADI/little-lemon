package com.example.littlelemon

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CartViewModel(
    private val database: AppDatabase
) : ViewModel() {

    val menuItems = database.menuItemDao().getAll()

    var isCutlerySelected by mutableStateOf(true)
        private set

    var cartItems by mutableStateOf(listOf<CartItem>())
        private set

    fun addToCart(item: MenuItemRoom, quantity: Int) {

        val existing = cartItems.find { it.item.id == item.id }

        cartItems = if (existing != null) {
            cartItems.map {
                if (it.item.id == item.id) {
                    it.copy(quantity = it.quantity + quantity)
                } else it
            }
        } else {
            cartItems + CartItem(item, quantity)
        }
    }

    fun increase(item: CartItem) {
        cartItems = cartItems.map {
            if (it.item.id == item.item.id) {
                it.copy(quantity = it.quantity + 1)
            } else it
        }
    }

    fun decrease(item: CartItem) {
        cartItems = cartItems.mapNotNull {
            if (it.item.id == item.item.id) {
                if (it.quantity > 1) it.copy(quantity = it.quantity - 1)
                else null
            } else it
        }
    }

    fun remove(item: CartItem) {
        cartItems = cartItems.filter { it.item.id != item.item.id }
    }

    fun clearCart() {
        cartItems = emptyList()
    }

    fun getSubtotal(): Double {
        return cartItems.sumOf {
            (it.item.price.toDoubleOrNull() ?: 0.0) * it.quantity
        }
    }

    fun getTotalCount(): Int {
        return cartItems.sumOf { it.quantity }
    }

    fun toggleCutlery(value: Boolean) {
        isCutlerySelected = value
    }

    fun getItemById(id: Int) = database.menuItemDao().getById(id)

}