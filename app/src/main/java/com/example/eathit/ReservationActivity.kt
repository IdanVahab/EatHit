package com.example.eathit

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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

/**
 * ReservationActivity:
 * This activity allows users to make a reservation by filling out a form.
 * Users can select a date, time, number of guests, seating preference, and payment method.
 * Input validation is performed to ensure all fields are correctly filled before submission.
 * The form also includes a "Vegan Preference" switch and localized support for RTL languages.
 */
class ReservationActivity : AppCompatActivity() {



    @SuppressLint("DefaultLocale", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        // Input fields and UI components
        val fullNameInput: EditText = findViewById(R.id.fullNameInput)
        val phoneInput: EditText = findViewById(R.id.phoneInput)
        val emailInput: EditText = findViewById(R.id.emailInput)
        val datePickerButton: Button = findViewById(R.id.datePickerButton)
        val timePickerButton: Button = findViewById(R.id.timePickerButton)
        val numberOfPeopleSpinner: Spinner = findViewById(R.id.numberOfPeopleSpinner)
        val seatingPreferenceSpinner: Spinner = findViewById(R.id.seatingPreferenceSpinner)
        val paymentMethodSpinner: Spinner = findViewById(R.id.paymentMethodSpinner)
        val veganSwitch: Switch = findViewById(R.id.veganSwitch)
        // Configure the vegan preference switch with a color indication
        veganSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                veganSwitch.thumbDrawable?.setTint(android.graphics.Color.GREEN)
            } else {
                veganSwitch.thumbDrawable?.setTint(android.graphics.Color.GRAY)
            }
        }

        val submitButton: Button = findViewById(R.id.submitButton)
        val backButton: Button = findViewById(R.id.backButton)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.number_of_people,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        numberOfPeopleSpinner.adapter = adapter

        val seatingAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.seating_preferences,
            android.R.layout.simple_spinner_item
        )

        seatingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        seatingPreferenceSpinner.adapter = seatingAdapter

        val paymentAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.payment_methods,
            android.R.layout.simple_spinner_item
        )

        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        paymentMethodSpinner.adapter = paymentAdapter

        fullNameInput.addTextChangedListener(createTextWatcher(fullNameInput, "Name is required") { input ->
            val isRTL = resources.configuration.layoutDirection == android.view.View.LAYOUT_DIRECTION_RTL

            fullNameInput.textDirection = if (isRTL) android.view.View.TEXT_DIRECTION_RTL else android.view.View.TEXT_DIRECTION_LTR
            fullNameInput.layoutDirection = if (isRTL) android.view.View.LAYOUT_DIRECTION_RTL else android.view.View.LAYOUT_DIRECTION_LTR

            input.matches(Regex("^[\\u0590-\\u05FF\\sA-Za-z]{2,}$"))
        })

        phoneInput.addTextChangedListener(createTextWatcher(phoneInput, "Phone must be 9-10 digits") {
            it.matches(Regex("\\d{9,10}"))
        })
        emailInput.addTextChangedListener(createTextWatcher(emailInput, "Invalid email format") {
            Patterns.EMAIL_ADDRESS.matcher(it).matches()
        })
        // DatePicker setup
        datePickerButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                datePickerButton.text = selectedDate
            }, year, month, day).show()
        }
        // TimePicker setup
        timePickerButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                timePickerButton.text = selectedTime
            }, hour, minute, true).show()
        }
        // Submit button with validation and navigation
        submitButton.setOnClickListener {
            // Perform scaling animation
            submitButton.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(150)
                .withEndAction {
                    submitButton.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(150)
                        .start()
                }.start()

            // Check if all fields are valid
            var isFormValid = true

            // Validate Full Name
            if (fullNameInput.text.isNullOrEmpty()) {
                fullNameInput.error = "Full Name is required"
                fullNameInput.setBackgroundResource(R.drawable.field_error_background)
                isFormValid = false
            } else {
                fullNameInput.setBackgroundResource(0)
            }

            // Validate Phone
            if (phoneInput.text.isNullOrEmpty() || !phoneInput.text.toString().matches(Regex("\\d{9,10}"))) {
                phoneInput.error = "Valid Phone Number is required"
                phoneInput.setBackgroundResource(R.drawable.field_error_background)
                isFormValid = false
            } else {
                phoneInput.setBackgroundResource(0)
            }

            // Validate Email
            if (emailInput.text.isNullOrEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailInput.text.toString()).matches()) {
                emailInput.error = "Valid Email is required"
                emailInput.setBackgroundResource(R.drawable.field_error_background)
                isFormValid = false
            } else {
                emailInput.setBackgroundResource(0)
            }

            // Validate Date
            if (datePickerButton.text.isNullOrEmpty() || datePickerButton.text.toString() == getString(R.string.datePickerButton)) {
                datePickerButton.error = "חובה לבחור תאריך"
                datePickerButton.setBackgroundResource(R.drawable.field_error_background)
                isFormValid = false
            } else {
                datePickerButton.setBackgroundResource(0)
            }


            if (timePickerButton.text.isNullOrEmpty() || timePickerButton.text.toString() == getString(R.string.select_time)) {
                timePickerButton.error = "חובה לבחור שעה"
                timePickerButton.setBackgroundResource(R.drawable.field_error_background) //
                isFormValid = false
            } else {
                timePickerButton.setBackgroundResource(0)
            }
            // Validate Number of People
            if (numberOfPeopleSpinner.selectedItem == "Number of Guests") {
                (numberOfPeopleSpinner.selectedView as? TextView)?.error = "Please select the number of guests"
                (numberOfPeopleSpinner.selectedView as? TextView)?.setBackgroundResource(R.drawable.field_error_background)
                isFormValid = false
            } else {
                (numberOfPeopleSpinner.selectedView as? TextView)?.setBackgroundResource(0) // Reset background
            }

            // Validate Seating Preference
            if (seatingPreferenceSpinner.selectedItem == "Preferred Seating") {
                (seatingPreferenceSpinner.selectedView as? TextView)?.error = "Please select seating preference"
                (seatingPreferenceSpinner.selectedView as? TextView)?.setBackgroundResource(R.drawable.field_error_background)
                isFormValid = false
            } else {
                (seatingPreferenceSpinner.selectedView as? TextView)?.setBackgroundResource(0) // Reset background
            }

            // Validate Payment Method
            if (paymentMethodSpinner.selectedItem == "Payment Method") {
                (paymentMethodSpinner.selectedView as? TextView)?.error = "Please select payment method"
                (paymentMethodSpinner.selectedView as? TextView)?.setBackgroundResource(R.drawable.field_error_background)
                isFormValid = false
            } else {
                (paymentMethodSpinner.selectedView as? TextView)?.setBackgroundResource(0) // Reset background
            }

            // If the form is valid, proceed to the next activity
            if (isFormValid) {
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
            } else {
                // Show error message if form is invalid
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            }
        }
        // Back button to return to the previous activity
        backButton.setOnClickListener {
            // Perform scaling animation
            backButton.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(150)
                .withEndAction {
                    backButton.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(150)
                        .withEndAction {
                            finish()
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out) // Transition animation
                        }.start()
                }.start()
        }
    }
    /**
     * Creates a TextWatcher for validating an EditText field.
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
}
