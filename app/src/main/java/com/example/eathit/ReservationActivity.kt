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
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar


class ReservationActivity : AppCompatActivity() {

    private val numberOfPeopleOptions = listOf("Number of Guests") + (1..10).map { it.toString() }
    private val seatingOptions = listOf("Preferred Seating", "Indoors", "Outdoors", "Bar")
    private val paymentMethods = listOf("Credit Card", "PayPal", "Cash")

    @SuppressLint("DefaultLocale", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        val fullNameInput: EditText = findViewById(R.id.fullNameInput)
        val phoneInput: EditText = findViewById(R.id.phoneInput)
        val emailInput: EditText = findViewById(R.id.emailInput)
        val datePickerButton: Button = findViewById(R.id.datePickerButton)
        val timePickerButton: Button = findViewById(R.id.timePickerButton)
        val numberOfPeopleSpinner: Spinner = findViewById(R.id.numberOfPeopleSpinner)
        val seatingPreferenceSpinner: Spinner = findViewById(R.id.seatingPreferenceSpinner)
        val paymentMethodSpinner: Spinner = findViewById(R.id.paymentMethodSpinner)
        val veganSwitch: Switch = findViewById(R.id.veganSwitch)
        veganSwitch.setOnCheckedChangeListener { _, isChecked ->
            val mainLayout = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.mainLayout) // עדכן את ה-ID בהתאם לקובץ ה-XML שלך
            if (isChecked) {
      // שינוי צבע המתג במצב דלוק
                veganSwitch.thumbDrawable?.setTint(android.graphics.Color.GREEN)
            } else {
                // מחזיר את צבע המתג למצב כבוי
                veganSwitch.thumbDrawable?.setTint(android.graphics.Color.GRAY)
            }
        }

        val submitButton: Button = findViewById(R.id.submitButton)
        val backButton: Button = findViewById(R.id.backButton) // הוספת כפתור חזרה

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.number_of_people, // מזהה המערך
            android.R.layout.simple_spinner_item // עיצוב ברירת המחדל
        )

// עיצוב התפריט הנפתח
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

// חיבור המערך לספינר
        numberOfPeopleSpinner.adapter = adapter

        val seatingAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.seating_preferences, // מזהה המערך
            android.R.layout.simple_spinner_item // עיצוב ברירת המחדל לפריט בספינר
        )

// עיצוב התפריט הנפתח
        seatingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

// חיבור המערך לספינר
        seatingPreferenceSpinner.adapter = seatingAdapter

        val paymentAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.payment_methods, // מזהה המערך
            android.R.layout.simple_spinner_item // עיצוב ברירת המחדל לפריט בספינר
        )

// עיצוב התפריט הנפתח
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

// חיבור המערך לספינר
        paymentMethodSpinner.adapter = paymentAdapter

        fullNameInput.addTextChangedListener(createTextWatcher(fullNameInput, "Name is required") { input ->
            // בדוק אם השפה היא עברית או אנגלית
            val isRTL = resources.configuration.layoutDirection == android.view.View.LAYOUT_DIRECTION_RTL

            // עדכן את הכיווניות בהתאם לשפה
            fullNameInput.textDirection = if (isRTL) android.view.View.TEXT_DIRECTION_RTL else android.view.View.TEXT_DIRECTION_LTR
            fullNameInput.layoutDirection = if (isRTL) android.view.View.LAYOUT_DIRECTION_RTL else android.view.View.LAYOUT_DIRECTION_LTR

            // בדיקת תקינות: קלט בעברית או אנגלית עם מינימום 2 תווים
            input.matches(Regex("^[\\u0590-\\u05FF\\sA-Za-z]{2,}$"))
        })

        phoneInput.addTextChangedListener(createTextWatcher(phoneInput, "Phone must be 9-10 digits") {
            it.matches(Regex("\\d{9,10}"))
        })
        emailInput.addTextChangedListener(createTextWatcher(emailInput, "Invalid email format") {
            Patterns.EMAIL_ADDRESS.matcher(it).matches()
        })

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

        timePickerButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                timePickerButton.text = selectedTime
            }, hour, minute, true).show()
        }

        submitButton.setOnClickListener {
            // ביצוע האנימציות
            submitButton.animate()
                .scaleX(1.1f) // הרחבה
                .scaleY(1.1f)
                .setDuration(20) // משך ההרחבה
                .start()

            // שינוי צבע הדרגתי
            val originalColor = submitButton.backgroundTintList
            submitButton.setBackgroundColor(getColor(R.color.backgroundGradientStart)) // שינוי לצבע בולט זמני
            submitButton.postDelayed({
                submitButton.backgroundTintList = originalColor // החזרה לצבע המקורי
            }, 300) // המתנה של 300ms

            // הפעלת האינטנט
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

        // מאזין לכפתור חזרה
        backButton.setOnClickListener {
            finish() // מחזיר למסך הקודם
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

        }
    }

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
