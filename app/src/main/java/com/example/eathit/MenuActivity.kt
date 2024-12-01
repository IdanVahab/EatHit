package com.example.eathit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

/**
 * MenuActivity:
 * This activity displays a menu with categories (Meals, Drinks, Desserts) and their respective items.
 * Users can click on cards to view items in a category, and select items to add them to the cart.
 * Includes animations for user interactions and localized support for English and Hebrew.
 */

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Container for displaying the list of dishes
        val dishesContainer: LinearLayout = findViewById(R.id.dishesContainer)

        // Back button with a scaling animation and a transition back to the previous activity
        val backButton: Button = findViewById(R.id.backButtonMenu)
        backButton.setOnClickListener {
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
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                        }.start()
                }.start()
        }
        // Food card to display meal items
        val foodCard: FrameLayout = findViewById(R.id.foodCard)
        foodCard.setOnClickListener {
            foodCard.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .withEndAction {
                    foodCard.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(150)
                        .start()
                }.start()
            // Display meal items in the container
            showDishes(getMeals(), dishesContainer)
        }
        // Drinks card to display drink items
        val drinksCard: FrameLayout = findViewById(R.id.drinksCard)
        drinksCard.setOnClickListener {
            drinksCard.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(150)
                .withEndAction {
                    drinksCard.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(150)
                        .start()
                }.start()
            // Display drink items in the container
            showDishes(getDrinks(), dishesContainer)
        }
        // Dessert card to display dessert items
        val dessertCard: FrameLayout = findViewById(R.id.dessertCard)
        dessertCard.setOnClickListener {
            dessertCard.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(150)
                .withEndAction {
                    dessertCard.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(150)
                        .start()
                }.start()
            // Display dessert items in the container
            showDishes(getDesserts(), dishesContainer)
        }


    }
    /**
     * Displays a list of dishes in the provided container with animations and click listeners.
     * @param dishes List of dishes to display.
     * @param container Layout container where the dishes will be displayed.
     */
    private fun showDishes(dishes: List<Dish>, container: LinearLayout) {
        container.removeAllViews()
        container.visibility = View.VISIBLE
        container.alpha = 0f
        container.animate().alpha(1f).setDuration(500).start()
        val locale = Locale.getDefault().language

        dishes.forEach { dish ->
            // Inflate a new dish card view
            val dishView = LayoutInflater.from(this).inflate(R.layout.menu_item_card, container, false)
            // Bind data to the dish card views
            val dishImage: ImageView = dishView.findViewById(R.id.dishImage)
            val dishName: TextView = dishView.findViewById(R.id.dishName)
            val dishDescription: TextView = dishView.findViewById(R.id.dishDescription)
            val dishPrice: TextView = dishView.findViewById(R.id.dishPrice)

            dishImage.setImageResource(dish.imageRes)
            dishName.text = if (locale == "he") dish.nameHebrew else dish.name
            dishDescription.text = if (locale == "he") dish.descriptionHebrew else dish.description
            dishPrice.text = dish.price

            // Add click listener to the dish card
            dishView.setOnClickListener {
                // Animate selection of the dish
                dishView.animate().scaleX(1.05f).scaleY(1.05f).setDuration(150).withEndAction {
                    dishView.animate().scaleX(1f).scaleY(1f).setDuration(150).start()
                }
                // Display a toast message indicating the dish was added to the cart
                val toastText = if (locale == "he") "${dish.nameHebrew} נוסף לעגלה!" else "${dish.name} added to cart!"
                Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
            }
            // Add the dish card to the container
            container.addView(dishView)
        }
    }
    // Returns a list of meals available in the menu
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
    // Returns a list of drinks available in the menu
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
    // Returns a list of desserts available in the menu
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
// Data class representing a dish item
data class Dish(
    val name: String,
    val nameHebrew: String,
    val description: String,
    val descriptionHebrew: String,
    val price: String,
    val imageRes: Int
)
