package com.example.eathit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eathit.managers.CartItem
import com.example.eathit.managers.CartManager
import com.example.eathit.R

class CartAdapter(
    private val cartItems: MutableList<CartItem>,
    private val cartManager: CartManager,
    private val onCartUpdated: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.itemName)
        val itemPrice: TextView = view.findViewById(R.id.itemPrice)
        val itemQuantity: TextView = view.findViewById(R.id.itemQuantity)
        val incrementButton: ImageButton = view.findViewById(R.id.incrementButton)
        val decrementButton: ImageButton = view.findViewById(R.id.decrementButton)
        val removeButton: ImageButton = view.findViewById(R.id.removeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item_row, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]

        holder.itemName.text = cartItem.itemName
        holder.itemPrice.text = "₪${cartItem.unitPrice}"
        holder.itemQuantity.text = cartItem.quantity.toString()

        // Increment quantity
        holder.incrementButton.setOnClickListener {
            cartItem.quantity++
            cartManager.updateItemQuantity(cartItem.itemName, cartItem.quantity)
            notifyItemChanged(position)
            onCartUpdated()
        }

        // Decrement quantity
        holder.decrementButton.setOnClickListener {
            if (cartItem.quantity > 1) {
                cartItem.quantity--
                cartManager.updateItemQuantity(cartItem.itemName, cartItem.quantity)
                notifyItemChanged(position)
                onCartUpdated()
            }
        }

        // Remove item
        holder.removeButton.setOnClickListener {
            cartManager.removeItem(cartItem.itemName)
            cartItems.removeAt(position)
            notifyItemRemoved(position)
            onCartUpdated()
        }
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }
}
