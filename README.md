# EatHit 🍴

**EatHit** is an innovative food ordering and table reservation app, developed as part of a first project in the Android Studio Kotlin development course at HIT.  
It combines modern design, interactive features, and seamless data management to deliver a user-friendly experience for managing menus, carts, and reservations.

---

## Features 🚀

*   **Dynamic Menus**: Browse categorized food items (Meals, Drinks, Desserts) and manage your cart in real-time.
*   **Smart Reservations**: Fill out a user-friendly form to book tables with custom preferences for date, time, and seating.
*   **Order Form**: Enter delivery details, apply coupons, and select payment options dynamically.
*   **Interactive Feedback**: Users receive real-time feedback for every action, such as toast messages and dynamic updates.
*   **Interactive Animations**: Engaging Lottie animations for transitions, confirmation screens, and interactive buttons.
*   **Bilingual Interface**: Fully supports Hebrew and English with automatic text direction (RTL and LTR).
*   **Persistent Cart**: Keeps items saved even if you close the app, resetting only after completing a purchase.
*   **Accessibility**: Fully accessible with screen reader compatibility and descriptive content labels.
*   **Dark Mode Compatibility**: Seamlessly switches between light and dark modes, maintaining consistent color contrast.

---

## Screenshots 📸

| **Screen**               | **Description**                                                           |
|---------------------------|---------------------------------------------------------------------------|
| **Splash Screen**         | Eye-catching animation with the app logo to welcome users.              |
| **Navigation Screen**     | Quick access to the menu or reservation screen with clear options.       |
| **Menu Screen**           | Displays categorized food items (Meals, Drinks, Desserts) with visuals. |
| **Cart Screen**           | Allows users to view, edit, and manage items with dynamic price updates. |
| **Reservation Form**      | Intuitive form to enter reservation details (time, date, guests, etc.). |
| **Order Form Screen**     | Enables users to input delivery information and payment options.        |
| **Confirmation Screen**   | Summarizes the order/reservation details and includes a rating dialog. |
| **Food Confirmation Screen** | A custom confirmation screen for food orders.                     |
| **Error Dialogs**         | Friendly messages for invalid actions like missing form fields.         |

---

## Technologies 🛠️

*   **Programming Language**: Kotlin.
*   **Development Environment**: Android Studio.
*   **UI Design**: XML layouts with Material Design principles.
*   **Animations**: Lottie for rich and interactive animations.
*   **Data Storage**: SharedPreferences for lightweight persistence.
*   **Localization**: Supports Hebrew and English with automatic text direction (RTL and LTR).
*   **Accessibility**: Designed with screen reader compatibility and content descriptions.

---

## Installation 📦

1.  Clone the repository:
    ```bash
    git clone https://github.com/username/EatHit.git
    ```
2.  Open the project in **Android Studio**.
3.  Sync Gradle to download dependencies.
4.  Run the app on an emulator or connected device.

### Requirements:

*   **Minimum Android Version**: API Level 21 (Android 5.0 Lollipop).
*   **Dependencies**:
    *   Lottie 3.0.7 for animations.
    *   AndroidX libraries for RecyclerView and Material Design components.

---

## Development Process 🧩

*   **Timeframe**: The project was developed over four weeks, focusing on UI design, functionality, and localization.
*   **Challenges & Solutions**:
    *   **RTL Support**: Handling bidirectional text for Hebrew and English required creating separate layout configurations.
    *   **Cart Synchronization**: Managing real-time updates and persistence across sessions was achieved using SharedPreferences and reactive UI updates.
    *   **Animations**: Customizing Lottie animations to align seamlessly with the app's design and transitions.

---

## Test Coverage 🧪

*   **Unit Testing**:
    *   Verified cart calculations for total cost, discounts, and quantity changes.
*   **UI Testing**:
    *   Tested navigation flow, form validations, and transitions between screens.

---

## Future Enhancements 🚀

*   **Push Notifications**: Reminders for reservations and special promotions.
*   **API Integration**: Adding Google Maps for restaurant locations and integrating online payment systems like PayPal.
*   **User Profiles**: Allow users to save preferences, favorite items, and view order history.
*   **Advanced Recommendations**: Suggest menu items based on user preferences and order history.
*   **Enhanced Test Coverage**: Expanding automated tests for edge cases and accessibility.

---

## Lessons Learned 🎓

*   **Responsive Design**: Adapting layouts to work seamlessly in both portrait and landscape modes.
*   **Reusable Code**: Creating utility functions for animations and navigation saved time and improved consistency.
*   **Localization**: Managing RTL and LTR text directions for a polished bilingual experience.
*   **User-Centric Development**: Designing with the user in mind by providing intuitive feedback and accessible features.


---

## Acknowledgments 💡

This project was developed as part of HIT's Android development course.  
It was a rewarding experience that combined technical challenges with creative problem-solving.

If you have feedback, ideas, or just want to connect, feel free to reach out! 😊
