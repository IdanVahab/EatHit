package com.example.eathit.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eathit.R
import java.util.Calendar

/**
 * Activity for making a reservation.
 * Allows users to select date, time, number of guests, seating preference, and payment method.
 */
class ReservationActivity : AppCompatActivity() {

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        // Initialize views
        val fullNameInput: EditText = findViewById(R.id.fullNameInput)
        val phoneInput: EditText = findViewById(R.id.phoneInput)
        val emailInput: EditText = findViewById(R.id.emailInput)
        val datePickerButton: Button = findViewById(R.id.datePickerButton)
        val timePickerButton: Button = findViewById(R.id.timePickerButton)
        val numberOfPeopleSpinner: Spinner = findViewById(R.id.numberOfPeopleSpinner)
        val seatingPreferenceSpinner: Spinner = findViewById(R.id.seatingPreferenceSpinner)
        val paymentMethodSpinner: Spinner = findViewById(R.id.paymentMethodSpinner)
        val veganSwitch: Switch = findViewById(R.id.veganSwitch)
        val submitButton: Button = findViewById(R.id.submitButton)
        val backButton: Button = findViewById(R.id.backButton)

        // Setup UI components
        setupSpinners(numberOfPeopleSpinner, seatingPreferenceSpinner, paymentMethodSpinner)
        setupVeganSwitch(veganSwitch)
        setupDatePicker(datePickerButton)
        setupTimePicker(timePickerButton)
        setupValidation(fullNameInput, phoneInput, emailInput)
        setupSubmitButton(submitButton, fullNameInput, phoneInput, emailInput, datePickerButton, timePickerButton, numberOfPeopleSpinner, seatingPreferenceSpinner, paymentMethodSpinner, veganSwitch)
        setupBackButton(backButton)
    }

    /**
     * Configures dropdown menus for guests, seating preference, and payment method.
     */
    private fun setupSpinners(vararg spinners: Spinner) {
        spinners.forEach { spinner ->
            val adapter = ArrayAdapter.createFromResource(
                this,
                when (spinner.id) {
                    R.id.numberOfPeopleSpinner -> R.array.number_of_people
                    R.id.seatingPreferenceSpinner -> R.array.seating_preferences
                    R.id.paymentMethodSpinner -> R.array.payment_methods
                    else -> throw IllegalArgumentException("Unknown spinner ID")
                },
                android.R.layout.simple_spinner_item
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    /**
     * Configures the vegan preference switch.
     */
    private fun setupVeganSwitch(switch: Switch) {
        switch.setOnCheckedChangeListener { _, isChecked ->
            switch.thumbDrawable?.setTint(if (isChecked) android.graphics.Color.GREEN else android.graphics.Color.GRAY)
        }
    }

    /**
     * Sets up the DatePicker for selecting a reservation date.
     */
    private fun setupDatePicker(button: Button) {
        button.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    button.text = String.format("%02d/%02d/%d", day, month + 1, year)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    /**
     * Sets up the TimePicker for selecting a reservation time.
     */
    private fun setupTimePicker(button: Button) {
        button.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(
                this,
                { _, hour, minute ->
                    button.text = String.format("%02d:%02d", hour, minute)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    /**
     * Sets up text watchers for input validation.
     */
    private fun setupValidation(vararg inputs: EditText) {
        inputs.forEach { input ->
            val (errorMessage, isValid) = when (input.id) {
                R.id.fullNameInput -> "Name is required" to { text: String -> text.isNotBlank() }
                R.id.phoneInput -> "Phone must be 9-10 digits" to { text: String -> text.matches(Regex("\\d{9,10}")) }
                R.id.emailInput -> "Invalid email format" to { text: String -> Patterns.EMAIL_ADDRESS.matcher(text).matches() }
                else -> "" to { _: String -> true }
            }
            input.addTextChangedListener(createTextWatcher(input, errorMessage, isValid))
        }
    }

    /**
     * Sets up the submit button with validation and navigation to the confirmation screen.
     */
    private fun setupSubmitButton(
        button: Button,
        fullNameInput: EditText,
        phoneInput: EditText,
        emailInput: EditText,
        datePickerButton: Button,
        timePickerButton: Button,
        numberOfPeopleSpinner: Spinner,
        seatingPreferenceSpinner: Spinner,
        paymentMethodSpinner: Spinner,
        veganSwitch: Switch
    ) {
        button.setOnClickListener {
            if (!validateForm(fullNameInput, phoneInput, emailInput, datePickerButton, timePickerButton, numberOfPeopleSpinner, seatingPreferenceSpinner, paymentMethodSpinner)) {
                Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, ConfirmationActivity::class.java).apply {
                putExtra("FULL_NAME", fullNameInput.text.toString())
                putExtra("PHONE", phoneInput.text.toString())
                putExtra("EMAIL", emailInput.text.toString())
                putExtra("DATE", datePickerButton.text.toString())
                putExtra("TIME", timePickerButton.text.toString())
                putExtra("NUMBER_OF_PEOPLE", numberOfPeopleSpinner.selectedItem.toString())
                putExtra("SEATING_PREFERENCE", seatingPreferenceSpinner.selectedItem.toString())
                putExtra("PAYMENT_METHOD", paymentMethodSpinner.selectedItem.toString())
                putExtra("IS_VEGAN", veganSwitch.isChecked)
            }
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    /**
     * Validates the reservation form fields.
     */
    private fun validateForm(
        fullNameInput: EditText,
        phoneInput: EditText,
        emailInput: EditText,
        datePickerButton: Button,
        timePickerButton: Button,
        numberOfPeopleSpinner: Spinner,
        seatingPreferenceSpinner: Spinner,
        paymentMethodSpinner: Spinner
    ): Boolean {
        val validators = listOf(
            fullNameInput to "Name is required",
            phoneInput to "Valid phone is required",
            emailInput to "Valid email is required",
            datePickerButton to "Date is required",
            timePickerButton to "Time is required"
        )
        return validators.all { (view, message) ->
            if ((view as? EditText)?.text.isNullOrEmpty()) {
                view.error = message
                false
            } else {
                true
            }
        }
    }

    /**
     * Creates a TextWatcher for validating an input field.
     */
    private fun createTextWatcher(editText: EditText, errorMessage: String, isValid: (String) -> Boolean): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                editText.error = if (s.isNullOrEmpty() || !isValid(s.toString())) errorMessage else null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
    }
    /**
     * Sets up the back button with a simple animation and navigation.
     */
    private fun setupBackButton(button: Button) {
        button.setOnClickListener {
            button.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(150)
                .withEndAction {
                    button.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(150)
                        .withEndAction {
                            finish()
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        }.start()
                }.start()
        }
    }

}
