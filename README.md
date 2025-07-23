# E-commerce API

A full-stack e-commerce application built with Spring Boot (Java) for the backend and a static HTML/CSS/JS frontend. This project demonstrates user authentication, product management, shopping cart, and order processing.

## Features

- **User Authentication**: Register, login, and role-based access (Customer/Admin).
- **Product Management**: CRUD operations for products (Admin only).
- **Product Search & Filter**: Search by name/category, filter by category.
- **Shopping Cart**: Add, update, remove items; view cart and total.
- **Order Management**: Place orders, view order history, cancel orders.
- **Responsive Frontend**: Modern, mobile-friendly UI in `index.html`.

## Technologies Used

- **Backend**: Spring Boot, Spring Security, JPA/Hibernate, H2/other DB
- **Frontend**: HTML, CSS, JavaScript (vanilla)
- **Build Tool**: Maven

## Getting Started

### Prerequisites
- Java 17+
- Maven

### Setup & Run

1. **Clone the repository**
   ```sh
   git clone <repo-url>
   cd ecommerceapi
   ```
2. **Build the project**
   ```sh
   mvn clean install
   ```
3. **Run the application**
   ```sh
   mvn spring-boot:run
   ```
   The backend will start at `http://localhost:8080`.

4. **Access the Frontend**
   Open `http://localhost:8080/index.html` in your browser.

### Default Admin User
- Username: `admin`
- Password: `admin123`

## API Endpoints

### Auth
- `POST /api/auth/register` — Register new user
- `POST /api/auth/login` — Login and get JWT token

### Products
- `GET /api/products` — List all products
- `GET /api/products/{id}` — Get product by ID
- `POST /api/products` — Add product (Admin)
- `PUT /api/products/{id}` — Update product (Admin)
- `DELETE /api/products/{id}` — Delete product (Admin)
- `GET /api/products/search?q=...` — Search products
- `GET /api/products/categories` — List categories
- `GET /api/products/category/{category}` — Products by category

### Cart
- `GET /api/cart` — Get current user's cart
- `POST /api/cart/add` — Add item to cart
- `PUT /api/cart/update` — Update cart item quantity
- `DELETE /api/cart/remove?productId=...` — Remove item
- `DELETE /api/cart/clear` — Clear cart

### Orders
- `POST /api/orders/create` — Place order
- `GET /api/orders/user/{userId}` — Get user's orders
- `PUT /api/orders/{orderId}/cancel?userId=...` — Cancel order

## Project Structure

```
src/
  main/
    java/com/example/ecommerceapi/
      controller/   # REST controllers
      entity/       # JPA entities
      repository/   # Spring Data repositories
      service/      # Business logic
      security/     # JWT & security config
      util/         # Utility classes
    resources/
      static/index.html  # Frontend
      application.properties
  test/
    java/com/example/ecommerceapi/  # Unit tests
```

## Customization
- Update `application.properties` for DB config, JWT secret, etc.
- Edit `index.html` for frontend changes.



