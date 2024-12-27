package com.example.eathit.activities

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eathit.R
import com.example.eathit.managers.CartManager
import com.example.eathit.utils.setupButtonWithAnimation
import java.util.Calendar

/**
 * Activity for managing orders: collects user details, calculates costs, and confirms orders.
 */
class OrderActivity : AppCompatActivity() {

    private lateinit var totalCostTextView: TextView
    private var deliveryFee = 10.0
    private val coupons = mapOf("DISCOUNT20" to 0.2, "SAVE10" to 0.1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        // Initialize Views
        val fullNameEditText: EditText = findViewById(R.id.fullNameEditText)
        val addressEditText: EditText = findViewById(R.id.addressEditText)
        val selectTimeButton: Button = findViewById(R.id.selectTimeButton)
        val couponEditText: EditText = findViewById(R.id.couponEditText)
        totalCostTextView = findViewById(R.id.totalCostTextView)
        val paymentMethodSpinner: Spinner = findViewById(R.id.paymentMethodSpinner)
        val paymentDetailsLayout: LinearLayout = findViewById(R.id.paymentDetailsLayout)
        val backButton: Button = findViewById(R.id.backButton)
        val completeOrderButton: Button = findViewById(R.id.completeOrderButton)

        // Set initial total cost
        val initialCost = intent.getDoubleExtra("TOTAL_COST", 0.0)
        updateTotalCost(initialCost)

        // Set up listeners
        setupPaymentMethodSpinner(paymentMethodSpinner, paymentDetailsLayout)
        setupTimePicker(selectTimeButton)
        setupCouponListener(couponEditText, initialCost)
        setupOrderTypeSpinner(initialCost)
        setupBackButton(backButton)
        setupCompleteOrderButton(completeOrderButton, fullNameEditText, addressEditText, selectTimeButton, paymentMethodSpinner, initialCost)
    }

    /**
     * Updates the total cost displayed, including delivery fee.
     */
    private fun updateTotalCost(cost: Double) {
        val total = cost + deliveryFee
        totalCostTextView.text = "Total: ₪${String.format("%.2f", total)}"
    }

    /**
     * Sets up the payment method spinner to show or hide payment details.
     */
    private fun setupPaymentMethodSpinner(spinner: Spinner, paymentDetailsLayout: LinearLayout) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val paymentMethod = parent.getItemAtPosition(position).toString()
                paymentDetailsLayout.visibility = if (paymentMethod in listOf("Credit Card", "PayPal", "כרטיס אשראי", "פייפאל")) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    /**
     * Sets up the time picker for selecting delivery time.
     */
    private fun setupTimePicker(button: Button) {
        button.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(
                this,
                { _, hour, minute ->
                    val selectedTime = String.format("%02d:%02d", hour, minute)
                    if (!isValidDeliveryTime(selectedTime)) {
                        Toast.makeText(this, "Delivery time must be between 10:00 and 22:00.", Toast.LENGTH_SHORT).show()
                    } else {
                        button.text = selectedTime
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    /**
     * Sets up the coupon input field to apply discounts.
     */
    private fun setupCouponListener(couponEditText: EditText, initialCost: Double) {
        couponEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val couponCode = couponEditText.text.toString().trim()
                val discount = coupons[couponCode] ?: 0.0
                updateTotalCost(initialCost * (1 - discount))
                Toast.makeText(
                    this,
                    if (discount > 0) "Coupon applied! Discount: ${discount * 100}%" else "Invalid coupon code.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Sets up the order type spinner to adjust delivery fee.
     */
    private fun setupOrderTypeSpinner(initialCost: Double) {
        val orderTypeSpinner: Spinner = findViewById(R.id.orderTypeSpinner)
        orderTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val orderType = parent.getItemAtPosition(position).toString()
                deliveryFee = if (orderType in listOf("Delivery", "משלוח")) 10.0 else 0.0
                updateTotalCost(initialCost)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    /**
     * Sets up the back button to navigate to the previous screen.
     */
    private fun setupBackButton(button: Button) {
        setupButtonWithAnimation(button) {
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    /**
     * Sets up the complete order button to validate and process the order.
     */
    private fun setupCompleteOrderButton(
        button: Button,
        fullNameEditText: EditText,
        addressEditText: EditText,
        timeButton: Button,
        paymentMethodSpinner: Spinner,
        initialCost: Double
    ) {
        setupButtonWithAnimation(button) {
            val fullName = fullNameEditText.text.toString().trim()
            val address = addressEditText.text.toString().trim()
            val deliveryTime = timeButton.text.toString().trim()
            val paymentMethod = paymentMethodSpinner.selectedItem.toString()

            if (!validateOrderDetails(fullName, address, deliveryTime, paymentMethod)) return@setupButtonWithAnimation

            // Clear cart and navigate to confirmation screen
            CartManager(this).clearCart()
            val intent = Intent(this, FoodConfirmationActivity::class.java).apply {
                putExtra("FULL_NAME", fullName)
                putExtra("ADDRESS", address)
                putExtra("DELIVERY_TIME", deliveryTime)
                putExtra("TOTAL_COST", initialCost)
            }
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    /**
     * Validates the order details provided by the user.
     */
    private fun validateOrderDetails(fullName: String, address: String, deliveryTime: String, paymentMethod: String): Boolean {
        if (fullName.isEmpty()) {
            Toast.makeText(this, "Please enter your full name.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (address.isEmpty()) {
            Toast.makeText(this, "Please enter your address.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (deliveryTime == "Select Preferred Delivery Time" || !isValidDeliveryTime(deliveryTime)) {
            Toast.makeText(this, "Please select a valid delivery time.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    /**
     * Validates the delivery time to ensure it is within the allowed range.
     */
    private fun isValidDeliveryTime(time: String): Boolean {
        val parts = time.split(":")
        val hour = parts[0].toIntOrNull() ?: return false
        val minute = parts[1].toIntOrNull() ?: return false
        return (hour in 10..21) || (hour == 22 && minute == 0)
    }
}
