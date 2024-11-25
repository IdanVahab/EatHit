package com.example.eathit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale



class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val foodAnimation: View = findViewById(R.id.foodAnimation)
        val drinksAnimation: View = findViewById(R.id.drinksAnimation)
        val dessertAnimation: View = findViewById(R.id.dessertAnimation)
        val dishesContainer: LinearLayout = findViewById(R.id.dishesContainer)
        val backButton: Button = findViewById(R.id.backButtonMenu)



        // לחיצה על אנימציות
        foodAnimation.setOnClickListener {
            showDishes(getMeals(), dishesContainer)

        }

        drinksAnimation.setOnClickListener {
            showDishes(getDrinks(), dishesContainer)
        }

        dessertAnimation.setOnClickListener {
            showDishes(getDesserts(), dishesContainer)
        }

        backButton.setOnClickListener {
            finish() // חזרה לעמוד הקודם
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

        }
    }

    private fun showDishes(dishes: List<Dish>, container: LinearLayout) {
        container.removeAllViews()
        container.visibility = View.VISIBLE

        container.alpha = 0f
        container.animate().alpha(1f).setDuration(500).start()

        val locale = Locale.getDefault().language // בדיקת שפת המכשיר

        dishes.forEach { dish ->
            val dishView = LayoutInflater.from(this).inflate(R.layout.menu_item_card, container, false)

            val dishImage: ImageView = dishView.findViewById(R.id.dishImage)
            val dishName: TextView = dishView.findViewById(R.id.dishName)
            val dishDescription: TextView = dishView.findViewById(R.id.dishDescription)
            val dishPrice: TextView = dishView.findViewById(R.id.dishPrice)

            dishImage.setImageResource(dish.imageRes)
            // הצגת טקסט לפי שפת המכשיר
            dishName.text = if (locale == "he") dish.nameHebrew else dish.name
            dishDescription.text = if (locale == "he") dish.descriptionHebrew else dish.description
            dishPrice.text = dish.price

            dishView.setOnClickListener {
                dishView.animate().scaleX(1.05f).scaleY(1.05f).setDuration(150).withEndAction {
                    dishView.animate().scaleX(1f).scaleY(1f).setDuration(150).start()
                }
                val toastText = if (locale == "he") "${dish.nameHebrew} נוסף לעגלה!" else "${dish.name} added to cart!"
                Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
            }

            container.addView(dishView)
        }
    }

    private fun getMeals(): List<Dish> {
        return listOf(
            Dish(
                "Vegan Burger", "המבורגר טבעוני",
                "A delicious vegan patty with fresh veggies.", "המבורגר טבעוני טעים עם ירקות טריים, מגיע עם ציפס ושתייה.",
                "₪29.00", R.drawable.vegan
            ),
            Dish(
                "Beef Burger", "המבורגר בקר",
                "Juicy beef patty with fresh toppings.", "המבורגר בקר עסיסי עם תוספות טריות, מגיע עם ציפס ושתייה.",
                "₪39.00", R.drawable.beefburger
            ),
            Dish(
                "Chicken Burger", "המבורגר עוף",
                "Crispy chicken patty with lettuce.", "המבורגר עוף פריך עם חסה, מגיע עם ציפס ושתייה.",
                "₪35.00", R.drawable.chickenburger
            ),
            Dish(
                "Pizza", "פיצה",
                "Classic Margherita pizza with fresh basil.", "פיצה מרגריטה קלאסית עם בזיליקום טרי.",
                "₪49.00", R.drawable.pizza
            ),
            Dish(
                "Salad", "סלט",
                "Fresh greens with cherry tomatoes.", "ירקות טריים עם עגבניות שרי.",
                "₪25.00", R.drawable.salad
            )
        )
    }

    private fun getDrinks(): List<Dish> {
        return listOf(
            Dish(
                "Fanta", "פאנטה",
                "Delicious orange-flavored soda.", "משקה מוגז בטעם תפוז.",
                "₪8.00", R.drawable.fanta
            ),
            Dish(
                "Water", "מים",
                "Refreshing mineral water.", "מים מינרליים מרעננים.",
                "₪5.00", R.drawable.water
            ),
            Dish(
                "Sprite", "ספרייט",
                "Cool and bubbly lemon-lime soda.", "משקה מוגז בטעם לימון-ליים.",
                "₪8.00", R.drawable.sprite
            ),
            Dish(
                "Coca-Cola", "קוקה קולה",
                "Classic Coca-Cola soda.", "משקה קוקה קולה קלאסי.",
                "₪10.00", R.drawable.cola
            )
        )
    }

    private fun getDesserts(): List<Dish> {
        return listOf(
            Dish(
                "Chocolate Mousse", "מוס שוקולד",
                "Rich chocolate mousse with a mint leaf.", "מוס שוקולד עשיר עם עלה נענע.",
                "₪20.00", R.drawable.chocho_mosse
            ),
            Dish(
                "Churros", "צ'ורוס",
                "Fried dough sticks with chocolate dip.", "מקלות בצק מטוגן עם רוטב שוקולד.",
                "₪25.00", R.drawable.churros
            ),
            Dish(
                "Pancakes", "פנקייקים",
                "Fluffy pancakes with maple syrup.", "פנקייקים רכים עם סירופ מייפל.",
                "₪30.00", R.drawable.pancakes
            )
        )
    }
}

// מחלקת מנה מעודכנת
data class Dish(
    val name: String,                // שם באנגלית
    val nameHebrew: String,          // שם בעברית
    val description: String,         // תיאור באנגלית
    val descriptionHebrew: String,   // תיאור בעברית
    val price: String,               // מחיר
    val imageRes: Int                // תמונה
)
