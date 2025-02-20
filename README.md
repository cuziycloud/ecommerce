# JewelryStore

## Introduction
JewelryStore is a comprehensive software solution designed to streamline the management of a jewelry store. By integrating various design patterns, this project ensures a robust, maintainable, and scalable system that enhances operational efficiency. The system provides essential functionalities such as inventory management, customer handling, order processing, and secure transactions.

This project is not only a practical implementation of software engineering principles but also a demonstration of how design patterns can be leveraged to improve modularity, flexibility, and maintainability. With a structured architecture, JewelryStore simplifies complex processes, making it easier for store owners to manage their businesses effectively.

## Features
- **Product Management**: Add, update, delete, and categorize jewelry items.
- **Customer Management**: Store and manage customer details.
- **Order Processing**: Track and process purchases efficiently.
- **Payment Integration**: Support multiple payment methods.
- **User Authentication**: Secure access with role-based authentication.
## Applied Design Patterns
This project incorporates several design patterns, including:
- **Singleton**: Ensuring a single instance for database connections.
- **Strategy**: Implementing different pricing and discount strategies.
- **Observer**: Notifying customers about new arrivals and discounts.
- **Factory Method**: Dynamically creating jewelry items.
- **Decorator**: Enhancing jewelry products with additional features.
...
## Installation
Follow these steps to set up the project locally:
1. Clone the repository:
   ```sh
   git clone https://gitlab.duthu.net/522h0083/jewelrystore.git
   ```
2. Navigate to the project directory:
   ```sh
   cd jewelrystore
   ```
3. Install dependencies:
   ```sh
   mvn install
   ```
4. Run the application:
   ```sh
   mvn spring-boot:run
   ```

## Usage
- Log in as an administrator to manage products and orders.
- Browse and add jewelry items to the cart.
- Proceed to checkout and choose a payment method.
- View order history and track shipments.

## Authors
Developed by **Binh Linh Van** as part of the Design Pattern coursework.

## License
This project is licensed under the MIT License.

## Project Status
Actively maintained with ongoing improvements. Contributions and feedback are welcome!
