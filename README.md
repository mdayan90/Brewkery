# ☕ Brewkery – Coffee & Bakery Ordering Android App

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-blue.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-brightgreen.svg)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM-orange.svg)](https://developer.android.com/topic/architecture)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

A native Android coffee and artisan bakery ordering application built for the **ClickRetina Private Limited** take-home assessment.

- **Assignment Prototype Reference**: [https://vivekshah138.github.io/Brewkery/](https://vivekshah138.github.io/Brewkery/)
- **REST API Endpoint**: `https://raw.githubusercontent.com/VivekShah138/Brewkery/main/data.json`

---

## 📱 Application Flow & Screenshots

The application follows the official four-screen interactive workflow:

```
Screen 1: Home / Menu  ──(Tap Item)──►  Screen 2: Item Detail & Customizer
          ▲                                       │
          │                                  (Add to Cart)
    (Back to Menu)                                │
          │                                       ▼
Screen 4: Order Status  ◄──(Place Order)──  Screen 3: Cart & Checkout
```

### Screen 1: Home / Menu
* **Store Announcement Banner**: Displays store status ("Open Now"), delivery time ("20 - 30 mins"), and delivery fee ("$2.50 Delivery").
* **Active Order Notification**: Floating status badge when an order is brewing (`#BWK-XXXX • PREPARING`) that links directly to the Order Status screen.
* **Instant Search**: Real-time filtering by name, tagline, and description with empty search state handling.
* **Dynamic Category Chips**: Horizontally scrollable row including "All Items" and categories fetched directly from the API (`Hot Coffee`, `Cold Brews`, `Artisan Bakery`).
* **Product Cards**: High-definition image loading with Coil, bestselling/seasonal badges, rating, review count, price, and "Customize" action button.
* **Floating Cart Summary**: Persistent bottom pill when $\ge 1$ item is in cart with item count, subtotal, and "View Cart" navigation.

### Screen 2: Item Detail & Customization
* **Hero Image**: Large curved image with back button and promotional badges.
* **Product Details**: Title, tagline, rating, calorie count, preparation time, and comprehensive description.
* **Ingredients**: Styled ingredient tags.
* **Interactive Customizations**:
  * **Size Options**: Selectable radio cards with dynamic extra pricing (e.g. `+$0.65`, `+$1.25`).
  * **Milk & Spread Options**: Selectable option cards with add-on pricing (e.g. `+$0.50`).
  * **Sweetness & Temperature**: Selectable sweetness and preparation temperature levels.
* **Dynamic Price Recalculation**: Bottom sticky bar updates the total in real time:
  $$\text{Total Price} = (\text{Base Price} + \text{Size Extra} + \text{Milk Extra}) \times \text{Quantity}$$
* **Quantity Stepper & Cart Dispatch**: Add customized item to cart and transition seamlessly.

### Screen 3: Cart & Checkout
* **Cart Items Listing**: Thumbnail, name, customization summary breakdown, line totals, and item removal button.
* **Quantity Controls**: Inline increment/decrement steppers per item.
* **Monetary Breakdown**:
  * $\text{Subtotal} = \sum \text{Item Totals}$
  * $\text{Delivery Fee} = \$2.50$
  * $\text{Tax (8\%)} = \text{Subtotal} \times 0.08$
  * $\text{Final Total} = \text{Subtotal} + \text{Delivery Fee} + \text{Tax}$
* **Empty Cart State**: Friendly illustration and "Browse Menu" call to action.

### Screen 4: Order Status
* **Confirmation Header**: "Brewing in Progress!" illustration and celebratory notification.
* **Ticket ID**: Monospace ticket generator (e.g. `#BWK-8429`).
* **Status Badge**: Prominent `PREPARING` badge.
* **Order Summary**: Complete breakdown of ordered items, customizations, and final total paid.
* **Return Navigation**: "Back to Menu" with the active order banner preserved.

---

## 🛠️ Technology Stack & Libraries

* **Language**: Kotlin 2.2.10
* **UI**: Jetpack Compose (Material 3)
* **Architecture**: MVVM + Repository Pattern
* **Local Database / Persistence**: Room 2.8.4 (SQLite)
* **Networking**: Retrofit 3.0.0 + OkHttp Logging Interceptor
* **JSON Serialization**: Gson 2.12.1 + Converter Gson
* **Image Loading**: Coil Compose 2.7.0
* **Concurrency & Reactive Streams**: Kotlin Coroutines & StateFlow
* **Navigation**: Navigation Compose 2.8.8
* **Unit Testing**: JUnit 4

---

## 🏗️ Architecture & Package Structure

```
com.example.brewkery/
│
├── data/
│   ├── local/
│   │   ├── BrewkeryDatabase.kt
│   │   ├── dao/
│   │   │   ├── CartDao.kt
│   │   │   └── OrderDao.kt
│   │   └── entity/
│   │       ├── CartItemEntity.kt
│   │       └── ActiveOrderEntity.kt
│   ├── model/
│   │   ├── ShopMeta.kt
│   │   ├── Category.kt
│   │   ├── MenuItem.kt
│   │   ├── Customizations.kt
│   │   ├── CartItem.kt
│   │   └── Order.kt
│   ├── remote/
│   │   ├── BrewkeryApiService.kt
│   │   └── RetrofitClient.kt
│   └── repository/
│       ├── BrewkeryRepository.kt
│       └── CartRepository.kt
│
├── utils/
│   └── PriceCalculator.kt
│
├── ui/
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   ├── components/
│   │   ├── StoreHeaderBanner.kt
│   │   ├── BrewkerySearchBar.kt
│   │   ├── CategoryChipsRow.kt
│   │   ├── ProductCard.kt
│   │   ├── CartBottomBar.kt
│   │   └── LoadingAndErrorStates.kt
│   └── screens/
│       ├── home/
│       │   ├── HomeScreen.kt
│       │   ├── HomeViewModel.kt
│       │   └── HomeUiState.kt
│       ├── detail/
│       │   ├── ItemDetailScreen.kt
│       │   ├── ItemDetailViewModel.kt
│       │   ├── ItemDetailViewModelFactory.kt
│       │   └── ItemDetailUiState.kt
│       ├── cart/
│       │   ├── CartScreen.kt
│       │   ├── CartViewModel.kt
│       │   └── CartUiState.kt
│       └── order/
│           └── OrderStatusScreen.kt
│
├── navigation/
│   ├── Screen.kt
│   └── AppNavGraph.kt
│
└── MainActivity.kt
```

---

## 🧪 Unit Testing

Unit tests for calculation logic in `PriceCalculatorTest.kt`:
* Base price calculation with zero customizations.
* Dynamic price calculation with size and milk add-ons.
* Quantity multiplication.
* 8% tax calculation and half-up rounding.
* Subtotal, delivery fee ($2.50), and tax final total formula.
* Empty cart edge case handling.
* Currency string formatting.

Run unit tests via command line:
```bash
./gradlew testDebugUnitTest
```
*Results: 8 passed, 0 failed.*

---

## 🤖 AI-Assisted Development Log

In accordance with the assignment guidelines, this project was developed using Gemini Pro within Android Studio, driven by detailed developer prompts, specifications, and full architectural guidance.

### 1. AI Tools Used
* **Gemini Pro** (DeepMind / Android Studio Agent)
* **Android Studio Ladybug Feature Drop / Narwhal Preview**

### 2. Actual Prompts & Guidance Provided

* **1. UI Development**:
  > *"Help me create a clean and modern coffee shop app UI using Jetpack Compose. I want the design to look attractive and professional while keeping it close to the provided reference."*

* **2. API Integration**:
  > *"Integrate the provided REST API using Retrofit. Explain how to fetch the menu data, create the required Kotlin data classes, and display the products in my app."*

* **3. Code Review**:
  > *"Take a look at my existing Kotlin code and suggest improvements. I want to keep it readable, avoid unnecessary complexity, and follow good Android development practices."*

### 3. What AI Got Right
* Accurately mapped the polymorphic customizations object from the official endpoint where bakery items use `"sizes"` for item count and `"milk_options"` for spreads/toppings without breaking schema integrity.
* Designed a clean, reactive `CartRepository` utilizing Kotlin `StateFlow` so that adding items or updating quantities immediately synchronizes with the floating cart pill on the Home screen and the cart screen.
* Properly implemented the `$2.50` delivery fee and `8%` tax calculations with `BigDecimal.HALF_UP` rounding to avoid precision discrepancies.

### 4. Real Bug Identified & Fixed
* **Issue**: In `ItemDetailUiState.kt`, the sealed interface definition originally declared `data class Success` without explicitly implementing `: ItemDetailUiState`.
* **Symptom**: During `compileDebugKotlin`, the Kotlin compiler produced an `Assignment type mismatch: expected ItemDetailUiState, actual ItemDetailUiState.Success`.
* **Fix**: Updated `data class Success(...) : ItemDetailUiState` and resolved the type hierarchy cleanly.

---

## 📦 How to Build the Debug APK

1. Clone this repository:
   ```bash
   git clone <YOUR_REPO_URL>
   cd Brewkery
   ```
2. Open the project in **Android Studio**.
3. Build the debug APK via Gradle:
   ```bash
   ./gradlew assembleDebug
   ```
   Or in Android Studio: **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**.
4. The generated APK will be in:
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```
