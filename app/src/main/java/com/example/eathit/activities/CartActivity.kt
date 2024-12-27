package com.example.eathit.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eathit.R
import com.example.eathit.adapters.CartAdapter
import com.example.eathit.managers.CartManager
import com.example.eathit.utils.setupButtonWithAnimation

class CartActivity : AppCompatActivity() {

    // Manager and Views
    private lateinit var cartManager: CartManager
    private lateinit var totalCostTextView: TextView
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Initialize CartManager
        cartManager = CartManager(this)

        // Initialize Views
        initViews()

        // Set up RecyclerView
        setupRecyclerView()

        // Update total cost initially
        updateTotalCost()
    }

    /**
     * Initialize views and set up click listeners.
     */
    private fun initViews() {
        totalCostTextView = findViewById(R.id.totalCostTextView)
        cartRecyclerView = findViewById(R.id.cartRecyclerView)

        // Complete Order Button
        val completeOrderButton: Button = findViewById(R.id.completeOrderButton)
        setupButtonWithAnimation(completeOrderButton) {
            navigateToOrderActivity()
        }

        // Back Button
        val backButton: Button = findViewById(R.id.backButton)
        setupButtonWithAnimation(backButton) {
            finish() // Return to the previous screen
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    /**
     * Set up the RecyclerView with the CartAdapter.
     */
    private fun setupRecyclerView() {
        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(
            cartManager.getItems().toMutableList(),
            cartManager
        ) { updateTotalCost() }
        cartRecyclerView.adapter = cartAdapter
    }

    /**
     * Navigate to the OrderActivity with total cost.
     */
    private fun navigateToOrderActivity() {
        val intent = Intent(this, OrderActivity::class.java)
        intent.putExtra("TOTAL_COST", cartManager.getTotalCost())
        startActivity(intent)
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

    /**
     * Update the total cost displayed in the TextView.
     */
    private fun updateTotalCost() {
        val totalCost = cartManager.getTotalCost()
        totalCostTextView.text = "Total: ₪${String.format("%.2f", totalCost)}"
    }
}
