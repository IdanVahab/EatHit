package com.example.eathit.managers

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

// Data class representing an item in the cart
data class CartItem(val itemName: String, val unitPrice: Double, var quantity: Int) {
    fun getTotalPrice(): Double {
        return unitPrice * quantity
    }
}

class CartManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("CartPreferences", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Holds the cart items in memory
    private var cartItems: MutableList<CartItem> = mutableListOf()

    init {
        // Load cart items from SharedPreferences
        loadCartFromPreferences()
    }

    // Add item to cart
    fun addItem(item: CartItem) {
        val existingItem = cartItems.find { it.itemName == item.itemName }
        if (existingItem != null) {
            existingItem.quantity += item.quantity
        } else {
            cartItems.add(item)
        }
        saveCartToPreferences()
    }

    // Update item quantity
    fun updateItemQuantity(itemName: String, newQuantity: Int) {
        val item = cartItems.find { it.itemName == itemName }
        if (item != null) {
            if (newQuantity <= 0) {
                cartItems.remove(item)
            } else {
                item.quantity = newQuantity
            }
            saveCartToPreferences()
        }
    }

    // Remove item from cart
    fun removeItem(itemName: String) {
        cartItems.removeAll { it.itemName == itemName }
        saveCartToPreferences()
    }

    // Get total cost of items in cart
    fun getTotalCost(): Double {
        return cartItems.sumOf { it.getTotalPrice() }
    }

    // Get all items in the cart
    fun getItems(): List<CartItem> {
        return cartItems
    }

    // Save cart items to SharedPreferences
    private fun saveCartToPreferences() {
        val json = gson.toJson(cartItems)
        sharedPreferences.edit().putString("cart_items", json).apply()
    }

    // Load cart items from SharedPreferences
    private fun loadCartFromPreferences() {
        val json = sharedPreferences.getString("cart_items", null)
        if (json != null) {
            val type = object : TypeToken<MutableList<CartItem>>() {}.type
            cartItems = gson.fromJson(json, type)
        }
    }

    // Clear the cart
    fun clearCart() {
        cartItems.clear()
        saveCartToPreferences()
    }
}
