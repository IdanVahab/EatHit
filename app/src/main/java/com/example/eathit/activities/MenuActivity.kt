package com.example.eathit.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eathit.R
import com.example.eathit.managers.CartItem
import com.example.eathit.managers.CartManager
import java.util.Locale

/**
 * Displays a menu with categories (Meals, Drinks, Desserts) and their respective items.
 * Users can select items to add to the cart with animations and localized support.
 */
class MenuActivity : AppCompatActivity() {

    private lateinit var cartManager: CartManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Initialize cart manager
        cartManager = CartManager(this)

        // Initialize UI elements
        initButtons()
    }

    /**
     * Initialize and set up all buttons and categories.
     */
    private fun initButtons() {
        val cartButton: Button = findViewById(R.id.cartButton)
        val backButton: Button = findViewById(R.id.backButtonMenu)
        val dishesContainer: LinearLayout = findViewById(R.id.dishesContainer)
        val foodCard: FrameLayout = findViewById(R.id.foodCard)
        val drinksCard: FrameLayout = findViewById(R.id.drinksCard)
        val dessertCard: FrameLayout = findViewById(R.id.dessertCard)

        // Cart button - Navigate to CartActivity
        cartButton.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        // Back button - Return to the previous activity
        backButton.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        // Set up category cards
        setupCategoryCard(foodCard, dishesContainer) { getMeals() }
        setupCategoryCard(drinksCard, dishesContainer) { getDrinks() }
        setupCategoryCard(dessertCard, dishesContainer) { getDesserts() }
    }

    /**
     * Sets up a category card with click listeners and animations.
     */
    private fun setupCategoryCard(
        card: FrameLayout,
        container: LinearLayout,
        getDishes: () -> List<Dish>
    ) {
        card.setOnClickListener {
            animateCard(card)
            showDishes(getDishes(), container)
        }
    }

    /**
     * Animates a card with scaling effects.
     */
    private fun animateCard(card: FrameLayout) {
        card.animate()
            .scaleX(1.1f)
            .scaleY(1.1f)
            .setDuration(150)
            .withEndAction {
                card.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(150)
                    .start()
            }.start()
    }

    /**
     * Displays a list of dishes in the provided container.
     */
    private fun showDishes(dishes: List<Dish>, container: LinearLayout) {
        container.removeAllViews()
        container.visibility = View.VISIBLE
        container.alpha = 0f
        container.animate().alpha(1f).setDuration(500).start()

        val locale = Locale.getDefault().language

        dishes.forEach { dish ->
            val dishView = LayoutInflater.from(this).inflate(R.layout.menu_item_card, container, false)

            // Bind data to the dish card
            val dishImage: ImageView = dishView.findViewById(R.id.dishImage)
            val dishName: TextView = dishView.findViewById(R.id.dishName)
            val dishDescription: TextView = dishView.findViewById(R.id.dishDescription)
            val dishPrice: TextView = dishView.findViewById(R.id.dishPrice)

            dishImage.setImageResource(dish.imageRes)
            dishName.text = if (locale == "he") dish.nameHebrew else dish.name
            dishDescription.text = if (locale == "he") dish.descriptionHebrew else dish.description
            dishPrice.text = dish.price

            dishView.setOnClickListener {
                addDishToCart(dish, locale)
                animateDishSelection(dishView)
            }

            container.addView(dishView)
        }
    }

    /**
     * Adds a dish to the cart and displays a toast message.
     */
    private fun addDishToCart(dish: Dish, locale: String) {
        val toastText = if (locale == "he") "${dish.nameHebrew} נוסף לעגלה!" else "${dish.name} added to cart!"
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
        cartManager.addItem(CartItem(dish.name, dish.price.replace("₪", "").toDouble(), 1))
        Log.d("CartManager", "Current cart items: ${cartManager.getItems()}")
    }

    /**
     * Animates the selection of a dish.
     */
    private fun animateDishSelection(dishView: View) {
        dishView.animate()
            .scaleX(1.05f)
            .scaleY(1.05f)
            .setDuration(150)
            .withEndAction {
                dishView.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(150)
                    .start()
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
/**
 * Data class representing a dish item.
 */
data class Dish(
    val name: String,
    val nameHebrew: String,
    val description: String,
    val descriptionHebrew: String,
    val price: String,
    val imageRes: Int
)
