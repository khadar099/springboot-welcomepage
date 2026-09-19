# KB STRIDE – E-Commerce Shoe Application

## 1. Application Overview

**KB STRIDE** is a Spring Boot-based e-commerce shoe application.

The application allows users to:

* Open the KB STRIDE website
* Create a user account
* Login using email and password
* Validate whether the user is registered
* Validate the user's password
* Redirect successfully authenticated users to the shopping page
* View shoes, product names and prices
* Add products to the shopping cart
* Track orders
* Store registered user information in an H2 database

The application is containerized using **Docker** and runs on **AWS EC2**.

---

# 2. Application Objective

The main objective of KB STRIDE is to build a simple e-commerce platform where customers can browse and eventually purchase shoes online.

The application is being developed incrementally.

### Current functionality

```text
Welcome Page
     ↓
Signup
     ↓
Login
     ↓
Authentication
     ↓
Shop
     ↓
Products
     ↓
Add to Cart
```

Future functionality can include:

```text
Cart
 ↓
Checkout
 ↓
Address
 ↓
Payment
 ↓
Order Creation
 ↓
Order Tracking
```

---

# 3. Technology Stack

| Technology      | Purpose                               |
| --------------- | ------------------------------------- |
| Java 17         | Application programming language      |
| Spring Boot     | Backend application framework         |
| Spring MVC      | Handles HTTP requests and controllers |
| Thymeleaf       | Server-side HTML rendering            |
| Spring Data JPA | Database interaction                  |
| Hibernate       | ORM / database mapping                |
| H2 Database     | User data storage                     |
| BCrypt          | Password encryption                   |
| Maven           | Build and dependency management       |
| HTML5           | Web page structure                    |
| CSS3            | Web page styling                      |
| Docker          | Application containerization          |
| AWS EC2         | Application hosting                   |
| Git/GitHub      | Source code management                |

---

# 4. Application Architecture

The application follows a simple layered architecture.

```text
                    USER
                      |
                      ↓
                Web Browser
                      |
                      ↓
             Thymeleaf HTML Pages
                      |
                      ↓
              Spring Controller
                      |
                      ↓
                UserService
                      |
                      ↓
              UserRepository
                      |
                      ↓
              Spring Data JPA
                      |
                      ↓
                 Hibernate
                      |
                      ↓
                 H2 Database
```

---

# 5. Project Structure

The main project structure is:

```text
springboot-welcomepage
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── khadar
│   │   │           └── welcomepage
│   │   │               │
│   │   │               ├── WelcomePageApplication.java
│   │   │               ├── WelcomeController.java
│   │   │               ├── User.java
│   │   │               ├── UserRepository.java
│   │   │               └── UserService.java
│   │   │
│   │   └── resources
│   │       │
│   │       ├── templates
│   │       │   ├── welcome.html
│   │       │   ├── login.html
│   │       │   ├── signup.html
│   │       │   ├── shop.html
│   │       │   └── track-order.html
│   │       │
│   │       ├── static
│   │       │   └── images
│   │       │
│   │       └── application.properties
│   │
│   └── test
│       └── resources
│           └── application.properties
│
├── Dockerfile
├── pom.xml
└── README.md
```

---

# 6. Main Java Components

## 6.1 WelcomePageApplication.java

This is the main Spring Boot application class.

It starts the Spring Boot application and initializes the Spring application context.

Example:

```java
@SpringBootApplication
public class WelcomePageApplication {

    public static void main(String[] args) {
        SpringApplication.run(
            WelcomePageApplication.class,
            args
        );
    }
}
```

---

# 7. WelcomeController.java

`WelcomeController` handles HTTP requests from the browser.

It connects the frontend pages with the backend services.

### Main endpoints

| HTTP Method | URL            | Purpose          |
| ----------- | -------------- | ---------------- |
| GET         | `/`            | Welcome page     |
| GET         | `/login`       | Login page       |
| POST        | `/login`       | Process login    |
| GET         | `/signup`      | Signup page      |
| POST        | `/signup`      | Register user    |
| GET         | `/shop`        | Shop page        |
| GET         | `/track-order` | Track order page |

---

## 7.1 Welcome Page

```text
GET /
```

Returns:

```text
welcome.html
```

This is the starting page of the application.

---

## 7.2 Signup

The signup form collects:

* Full Name
* Email
* Password

The form sends a POST request:

```text
POST /signup
```

The controller passes the information to `UserService`.

---

## 7.3 Signup Validation

The application checks:

### Invalid email

If the email format is invalid:

```text
Please enter a correct email address.
```

### Existing email

If the email already exists:

```text
Email is already registered.
```

### Successful registration

The user is redirected to:

```text
/login?signupSuccess=true
```

The login page then displays:

```text
Account created successfully. Please login.
```

---

# 8. UserService.java

`UserService` contains the application's business logic related to users.

It handles:

* User registration
* Email validation
* Duplicate email checking
* Password encryption
* Login validation
* Password verification

---

# 9. Password Security

Passwords are **not stored as plain text**.

The application uses:

```text
BCryptPasswordEncoder
```

During signup:

```text
User enters password
        ↓
BCrypt encryption
        ↓
Encrypted password
        ↓
H2 Database
```

For example:

```text
User password:

MyPassword123

        ↓

BCrypt

        ↓

Encrypted hash stored in database
```

During login, BCrypt compares the entered password against the stored encrypted password.

---

# 10. Login Flow

The login form accepts:

* Email
* Password

The request is:

```text
POST /login
```

The controller calls:

```text
UserService.loginUser()
```

The service searches the database using the email address.

---

## 10.1 User Not Registered

If the email doesn't exist:

```text
User is not registered.
Please sign up first.
```

The user remains on the login page.

---

## 10.2 Incorrect Password

If the email exists but the password is incorrect:

```text
Invalid password.
Please try again.
```

---

## 10.3 Successful Login

If both email and password are correct:

```text
Login successful
       ↓
redirect:/shop
```

The user is taken to the KB STRIDE shopping page.

---

# 11. User.java

`User.java` is the JPA entity representing a registered customer.

Current fields:

| Field       | Purpose                    |
| ----------- | -------------------------- |
| `id`        | Unique user ID             |
| `fullName`  | Customer name              |
| `email`     | Customer email             |
| `password`  | BCrypt encrypted password  |
| `createdAt` | Account creation timestamp |

The entity maps to the database table:

```text
users
```

---

# 12. UserRepository.java

`UserRepository` communicates with the database through Spring Data JPA.

Important methods include:

```java
boolean existsByEmailIgnoreCase(String email);
```

Used to check whether an email is already registered.

And:

```java
Optional<User> findByEmailIgnoreCase(String email);
```

Used during login to find a registered user.

---

# 13. Database

The application uses:

```text
H2 Database
```

The database is configured as a file-based database.

Current database location:

```text
/app/data/kbstride
```

The database contains the registered user information.

---

# 14. H2 Persistence

Docker uses a volume:

```text
kbstride-data
```

The volume is mounted to:

```text
/app/data
```

Docker command:

```bash
docker run -d \
  --name kbstride-container \
  -p 8181:8181 \
  -v kbstride-data:/app/data \
  kbstride
```

This allows database data to survive container recreation.

```text
Docker Container
      |
      ↓
/app/data
      |
      ↓
Docker Volume
      |
      ↓
kbstride-data
```

Therefore, removing and recreating the container does not automatically remove the database volume.

---

# 15. H2 Console

The application provides the H2 database console.

URL:

```text
/h2-console
```

Database configuration:

```text
JDBC URL:
jdbc:h2:file:/app/data/kbstride

Username:
sa

Password:
```

The H2 console can be used to inspect the database and registered users during development.

---

# 16. Thymeleaf

The frontend uses **Thymeleaf**.

HTML files are stored under:

```text
src/main/resources/templates/
```

Current pages:

```text
welcome.html
login.html
signup.html
shop.html
track-order.html
```

Thymeleaf allows the backend to send information to the HTML pages.

For example:

```html
<div th:if="${error}" th:text="${error}">
</div>
```

This displays backend error messages on the page.

---

# 17. Signup Page

The signup page contains:

```text
KB STRIDE

Create Account

Full Name
Email
Password

SIGN UP
```

Mobile number is currently **not required**.

---

# 18. Login Page

The login page contains:

```text
KB STRIDE

Email
Password

LOGIN
```

It also displays:

* Signup success message
* User not registered message
* Invalid password message

---

# 19. Shop Page

After successful login, the user is redirected to:

```text
/shop
```

The shop page displays:

* KB STRIDE branding
* Product collection
* Shoe names
* Prices
* Product images
* Add to Cart button
* Cart navigation
* Track Order navigation

Example:

```text
KB STRIDE

OWN EVERY STEP.

-------------------------------------

KB Stride Urban Runner

₹2,499

[ ADD TO CART ]

-------------------------------------

KB Stride Classic Walk

₹2,999

[ ADD TO CART ]

-------------------------------------
```

The initial product names and prices are sample products and can later be replaced with actual inventory.

---

# 20. Product Images

Product images are stored under:

```text
src/main/resources/static/images/
```

Example:

```text
static
└── images
    ├── shoe1.jpg
    ├── shoe2.jpg
    ├── shoe3.jpg
    └── shoe4.jpg
```

They can be referenced from HTML using:

```html
<img src="/images/shoe1.jpg">
```

Spring Boot automatically serves files from the `static` directory.

---

# 21. Application Configuration

The main configuration is stored in:

```text
src/main/resources/application.properties
```

Important settings include:

```properties
spring.application.name=welcome-page
server.port=8181
```

Therefore, the application listens on:

```text
8181
```

---

# 22. Docker

The application is containerized using Docker.

The Dockerfile uses:

```text
Eclipse Temurin Java 17
```

The application JAR is copied into the container.

Example:

```dockerfile
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

RUN mkdir -p /app/data

COPY target/welcome-page-0.0.1-SNAPSHOT.jar /app/welcome-page.jar

EXPOSE 8181

ENTRYPOINT ["java", "-jar", "welcome-page.jar"]
```

---

# 23. Docker Application Flow

```text
Source Code
     ↓
Maven Build
     ↓
JAR File
     ↓
Docker Image
     ↓
Docker Container
     ↓
Port 8181
     ↓
Browser
```

---

# 24. Building the Application

The application is built using Maven.

Command:

```bash
mvn clean package
```

This performs:

```text
Clean previous build
        ↓
Compile Java code
        ↓
Run tests
        ↓
Package application
        ↓
Generate JAR
```

The JAR is generated under:

```text
target/
```

---

# 25. Docker Build

After the Maven build:

```bash
docker build -t kbstride .
```

This creates the Docker image:

```text
kbstride
```

---

# 26. Running the Container

The container is started with:

```bash
docker run -d \
  --name kbstride-container \
  -p 8181:8181 \
  -v kbstride-data:/app/data \
  kbstride
```

### Port mapping

```text
EC2 Port 8181
      ↓
Docker Port 8181
      ↓
Spring Boot Port 8181
```

---

# 27. AWS EC2 Deployment

The application is deployed on an AWS EC2 instance.

The EC2 security group needs to allow inbound traffic on:

```text
TCP 8181
```

The application can then be accessed through:

```text
http://<EC2-PUBLIC-IP>:8181
```

---

# 28. Complete Application Flow

The complete current flow is:

```text
                    KB STRIDE
                        |
                        ↓
                  Welcome Page
                        |
             ┌──────────┴──────────┐
             ↓                     ↓
           SIGN UP                LOGIN
             |                     |
             ↓                     ↓
       Full Name               Email
       Email                   Password
       Password                   |
             |                     ↓
             ↓              UserService
       UserService                |
             |                    ↓
             ↓              UserRepository
       BCrypt Password            |
       Encryption                 ↓
             |                H2 Database
             ↓                    |
       H2 Database                ↓
             |              ┌─────┴─────┐
             |              ↓           ↓
             |        Not Registered  Wrong Password
             |              ↓           ↓
             |          Error Message  Error Message
             |
             ↓
       Signup Successful
             |
             ↓
           LOGIN
             |
             ↓
       Correct Credentials
             |
             ↓
           /shop
             |
             ↓
       Shoe Products
             |
             ↓
         Add to Cart
```

---

# 29. Error Handling

The application currently handles the following user scenarios.

| Scenario                        | Application Response                   |
| ------------------------------- | -------------------------------------- |
| Invalid email                   | Please enter a correct email address   |
| Existing email during signup    | Email is already registered            |
| Successful signup               | Redirect to login with success message |
| Unregistered email during login | User is not registered                 |
| Incorrect password              | Invalid password                       |
| Correct login                   | Redirect to shop                       |

---

# 30. Test Database Configuration

For Maven tests, an in-memory H2 database is used.

Test configuration:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=create-drop
```

This prevents automated tests from attempting to access the production-style file database path.

---

# 31. Security Considerations

Current security implementation includes:

* Password hashing using BCrypt
* Email uniqueness
* Email format validation
* Password verification during login
* Database persistence

### Future security improvements

The application can later implement:

* Spring Security
* Session-based authentication
* Logout functionality
* CSRF protection
* Password strength validation
* Forgot password
* Email verification
* Role-based access
* HTTPS
* Secure cookies
* Rate limiting
* Account lockout after repeated failed attempts

---

# 32. Current Application Status

### Completed

* [x] Spring Boot application
* [x] Welcome page
* [x] Signup page
* [x] Login page
* [x] User registration
* [x] Email validation
* [x] Duplicate email validation
* [x] BCrypt password encryption
* [x] Login validation
* [x] Unregistered user validation
* [x] Invalid password validation
* [x] Signup success redirect
* [x] Shop page
* [x] Product display
* [x] Product prices
* [x] Add to Cart UI
* [x] H2 database
* [x] Docker containerization
* [x] Docker volume for database persistence
* [x] AWS EC2 deployment
* [x] Port 8181 configuration

---

# 33. Future Development

The following functionality can be added next:

## Shopping Cart

```text
Add to Cart
     ↓
Cart
     ↓
Increase / Decrease Quantity
     ↓
Remove Product
     ↓
Calculate Total
```

## Checkout

```text
Cart
 ↓
Checkout
 ↓
Customer Address
 ↓
Order Summary
 ↓
Payment
```

## Orders

```text
Order Created
      ↓
Order ID
      ↓
Order Status
      ↓
Track Order
```

## Admin Portal

An admin portal can later be created for:

* Add products
* Edit products
* Delete products
* Update prices
* Manage inventory
* View customers
* View orders
* Update order status

---

# 34. High-Level Architecture

```text
                         AWS EC2
                           |
                           ↓
                    Docker Container
                           |
                    Port 8181
                           |
                           ↓
                   Spring Boot App
                           |
              ┌────────────┼────────────┐
              ↓            ↓            ↓
         Controllers    Services      Thymeleaf
              |            |            |
              └────────────┼────────────┘
                           ↓
                    Spring Data JPA
                           |
                           ↓
                       Hibernate
                           |
                           ↓
                      H2 Database
                           |
                           ↓
                     Docker Volume
```

---

# 35. Summary

KB STRIDE is a Spring Boot-based e-commerce application designed for selling shoes online.

The current application demonstrates a complete basic customer flow:

```text
Customer
   ↓
Welcome Page
   ↓
Create Account
   ↓
BCrypt Password Encryption
   ↓
H2 Database
   ↓
Login
   ↓
User Validation
   ↓
Shop
   ↓
Products
   ↓
Add to Cart
```

The application is built using Java 17, Spring Boot, Thymeleaf, Spring Data JPA, Hibernate, H2, Maven and Docker, and is deployed on AWS EC2.

The application has been designed so that additional e-commerce functionality such as cart management, checkout, payment, order management and administration can be added incrementally.
