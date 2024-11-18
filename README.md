# Android App: Restaurant Ordering Mobile App

Kotlin Online Shop is an interactive Android application that allows users to create and customize orders for ice cream sundaes. The app provides a user-friendly interface for selecting sizes, flavors, hot fudge amounts, and various toppings while dynamically calculating the total price. It includes a database for order history and navigational options for additional functionality.


## Features

- **Dynamic Pricing**: Prices are updated in real-time based on user selections.
- **Customizable Sundaes**:
  - Select size: Small, Medium, Large
  - Choose from a variety of flavors
  - Adjust the amount of hot fudge (0-3 oz)
  - Add/remove toppings like peanuts, M&Ms, strawberries, and more
- **Predefined "The Works" Option**: One-click setup for a large sundae with all toppings and maximum fudge.
- **Order Management**:
  - Place new orders
  - Reset form to default
  - View order history

<div style="display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px;">
    <img src="https://github.com/user-attachments/assets/89fa11e4-5d64-407c-ab71-f060f54a6dc4" alt="kotline5" style="width: 30%; height: auto;">
    <img src="https://github.com/user-attachments/assets/2db14a77-ba8b-49d0-9aba-b23088c1c185" alt="kotlin3" style="width: 30%; height: auto;">
    <img src="https://github.com/user-attachments/assets/fac33f2d-a5a7-49f8-b2c9-9b369c67e1dd" alt="kotlin2" style="width: 30%; height: auto;">
    <img src="https://github.com/user-attachments/assets/5723f510-86cf-46e5-903f-f111167f5fff" alt="kotlin1" style="width: 30%; height: auto;">
</div>

- **Navigation Menu**:
  - Access "About" page
  - View order history
- **Validation**: Ensures valid input selections for size, flavor, and price before allowing orders to be placed.

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/kotlin-online-shop.git
2. Open the project in Android Studio.
3. Sync the Gradle files and build the project.
4. Run the app on an emulator or a physical device.

## Usage

1. Launch the app and start customizing your sundae:
   • Use dropdown menus to select size and flavor.
   • Use the seek bar to adjust the amount of hot fudge.
   • Check the boxes for your desired toppings.
2. The total price is updated dynamically.
3. Place your order by clicking the "Order" button.
4. Use the navigation menu to view additional options like the "About" page or your order history.

## Project Structure

`MainActivity`: Handles the UI logic for creating and managing orders.
Enums:
`Size`: Represents sundae sizes and their prices.
`HotFudge`: Represents fudge amounts and their prices.
`Topping`: Represents available toppings and their prices.
`Database Handler`: Manages order persistence in the local database.
`Navigation Menu`: Provides additional features like an "About" page and order history.

Future Enhancements
1. Add user accounts for personalized order histories.
2. Introduce payment gateway integration.
3. Expand menu options with more customizable items.

## License
This project is licensed under the [MIT License](https://opensource.org/license/mit).
