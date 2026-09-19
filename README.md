# springboot-welcomepage
springboot-welcomepage
# KB STRIDE – Spring Boot Application Setup & Signup Implementation

## 1. Project Overview

**Project Name:** KB STRIDE
**Application:** Spring Boot Web Application
**Technology:** Java 17 + Spring Boot + Thymeleaf
**Build Tool:** Maven
**Containerization:** Docker
**Database:** H2 Database
**Application Port:** 8181

KB STRIDE is a Spring Boot-based web application for the KB STRIDE footwear brand.

The application currently provides:

* KB STRIDE landing/welcome page
* Login page
* Signup page
* Track Order page
* User registration
* H2 database integration
* Password encryption using BCrypt
* Duplicate email validation
* Email format validation
* Docker containerization
* Persistent H2 database using Docker volume

---

# 2. Technology Stack

| Component            | Technology                  |
| -------------------- | --------------------------- |
| Programming Language | Java 17                     |
| Framework            | Spring Boot 3.4.1           |
| Web Framework        | Spring MVC                  |
| UI                   | Thymeleaf + HTML/CSS        |
| Build Tool           | Maven                       |
| Database             | H2                          |
| ORM                  | Spring Data JPA / Hibernate |
| Password Encryption  | BCrypt                      |
| Containerization     | Docker                      |
| Application Port     | 8181                        |
| Database Type        | File-based H2               |

---

# 3. Project Structure

Current project structure:

```text
springboot-welcomepage/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── khadar/
│   │   │           └── welcomepage/
│   │   │               ├── WelcomePageApplication.java
│   │   │               ├── WelcomeController.java
│   │   │               ├── User.java
│   │   │               ├── UserRepository.java
│   │   │               └── UserService.java
│   │   │
│   │   └── resources/
│   │       ├── static/
│   │       ├── templates/
│   │       │   ├── welcome.html
│   │       │   ├── login.html
│   │       │   ├── signup.html
│   │       │   └── track-order.html
│   │       │
│   │       └── application.properties
│   │
│   └── test/
│
├── Dockerfile
├── pom.xml
└── README.md
```

---

# 4. Initial Application

The application was initially created as a Spring Boot web application.

The initial functionality included:

```text
/
├── Welcome Page
├── Login
├── Signup
└── Track Order
```

The controller initially provided GET mappings for these pages.

Example:

```java
@GetMapping("/")
public String welcome() {
    return "welcome";
}

@GetMapping("/login")
public String login() {
    return "login";
}

@GetMapping("/signup")
public String signup() {
    return "signup";
}

@GetMapping("/track-order")
public String trackOrder() {
    return "track-order";
}
```

---

# 5. Website Routes

Current application routes:

| URL            | Purpose                |
| -------------- | ---------------------- |
| `/`            | KB STRIDE Welcome Page |
| `/login`       | Login Page             |
| `/signup`      | Signup Page            |
| `/track-order` | Track Order Page       |
| `/h2-console`  | H2 Database Console    |

Example:

```text
http://<EC2-IP>:8181/
```

Signup:

```text
http://<EC2-IP>:8181/signup
```

Login:

```text
http://<EC2-IP>:8181/login
```

Track Order:

```text
http://<EC2-IP>:8181/track-order
```

---

# 6. Maven Dependencies

The project uses Maven for dependency management.

Important dependencies added during the implementation:

### Spring Web

Used to create REST/web controllers and handle HTTP requests.

```xml
<artifactId>spring-boot-starter-web</artifactId>
```

### Thymeleaf

Used for server-side HTML rendering.

```xml
<artifactId>spring-boot-starter-thymeleaf</artifactId>
```

### Spring Data JPA

Used to communicate with the database through JPA repositories.

```xml
<artifactId>spring-boot-starter-data-jpa</artifactId>
```

### H2

Used as the database.

```xml
<groupId>com.h2database</groupId>
<artifactId>h2</artifactId>
```

### Spring Security Crypto

Used specifically for password hashing using BCrypt.

```xml
<groupId>org.springframework.security</groupId>
<artifactId>spring-security-crypto</artifactId>
```

---

# 7. H2 Database Implementation

The application initially did not have a database.

H2 database was introduced to store signup information.

We selected a **file-based H2 database** instead of an in-memory database.

Current configuration:

```properties
spring.datasource.url=jdbc:h2:file:/app/data/kbstride
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```

The database files are stored under:

```text
/app/data/
```

Database name:

```text
kbstride
```

---

# 8. JPA / Hibernate Configuration

JPA is used to map Java objects to database tables.

Current configuration:

```properties
spring.jpa.hibernate.ddl-auto=update
```

This allows Hibernate to automatically create/update database tables based on the entity definition.

SQL logging is enabled:

```properties
spring.jpa.show-sql=true
```

H2 dialect is explicitly configured:

```properties
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

The explicit dialect configuration was added because Hibernate was initially unable to determine the database dialect from JDBC metadata during application startup/testing.

---

# 9. User Entity

A `User` entity was created to represent registered users.

File:

```text
src/main/java/com/khadar/welcomepage/User.java
```

The entity contains:

| Field     | Purpose                   |
| --------- | ------------------------- |
| id        | Unique user ID            |
| fullName  | User's full name          |
| email     | Login identifier          |
| mobile    | User mobile number        |
| password  | BCrypt encrypted password |
| createdAt | Registration timestamp    |

Database table:

```text
users
```

The email field is configured as unique.

```java
@Column(nullable = false, unique = true)
private String email;
```

A database-level unique constraint is also configured.

This helps prevent duplicate email addresses.

---

# 10. User Registration Timestamp

The application automatically records the registration time.

```java
@PrePersist
protected void onCreate() {
    createdAt = LocalDateTime.now();
}
```

This means the application does not require the signup page to send the registration timestamp.

It is automatically generated before the user is inserted into the database.

---

# 11. User Repository

File:

```text
src/main/java/com/khadar/welcomepage/UserRepository.java
```

The repository extends:

```java
JpaRepository<User, Long>
```

This provides standard database operations such as:

* Save
* Find
* Delete
* Update
* Find by ID

Additional methods were added for email validation:

```java
boolean existsByEmailIgnoreCase(String email);

Optional<User> findByEmailIgnoreCase(String email);
```

The `IgnoreCase` functionality ensures that email comparison is case-insensitive.

For example:

```text
user@gmail.com
USER@gmail.com
User@gmail.com
```

are treated as the same email address.

---

# 12. User Service

File:

```text
src/main/java/com/khadar/welcomepage/UserService.java
```

The service layer handles registration business logic.

Registration flow:

```text
Signup Form
     |
     v
WelcomeController
     |
     v
UserService
     |
     +---- Validate Email
     |
     +---- Check Duplicate Email
     |
     +---- Encrypt Password
     |
     +---- Save User
     |
     v
H2 Database
```

---

# 13. Email Validation

The application validates the email before saving the user.

Example validation pattern:

```java
^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$
```

If the email is invalid, the application returns:

```text
Please enter a correct email address.
```

Example:

```text
abc
abc@
abc@gmail
```

These should be rejected.

---

# 14. Duplicate Email Validation

Before registering a user, the application checks whether the email already exists.

```java
if (userRepository.existsByEmailIgnoreCase(email)) {
    return "EMAIL_EXISTS";
}
```

If the email already exists, the user receives:

```text
Email is already registered.
```

This check is case-insensitive.

---

# 15. Password Security

Passwords are **not stored as plain text**.

BCrypt is used to encrypt/hash the password before saving it.

Example:

```java
BCryptPasswordEncoder passwordEncoder =
        new BCryptPasswordEncoder();
```

The password is then encoded:

```java
String encryptedPassword =
        passwordEncoder.encode(password);
```

The encrypted value is stored in the database.

Example:

```text
User enters:

MyPassword123
```

Database stores something similar to:

```text
$2a$10$................................
```

The original password is not stored in the database.

---

# 16. Signup Request Flow

When the user submits the signup form:

```text
Browser
   |
   | POST /signup
   v
WelcomeController
   |
   v
UserService
   |
   +--> Validate email
   |
   +--> Check existing email
   |
   +--> Encrypt password
   |
   v
UserRepository
   |
   v
H2 Database
```

If registration succeeds:

```text
/signup
    |
    v
redirect:/login?signupSuccess=true
```

---

# 17. WelcomeController

The controller was enhanced from simple page navigation to handle signup POST requests.

The signup endpoint now supports:

```java
@PostMapping("/signup")
```

It receives:

```text
fullName
email
mobile
password
```

The controller sends these values to `UserService`.

Possible results:

### Invalid email

```text
Please enter a correct email address.
```

### Existing email

```text
Email is already registered.
```

### Successful registration

User is redirected to:

```text
/login?signupSuccess=true
```

---

# 18. Application Properties

Current configuration:

```properties
spring.application.name=welcome-page
server.port=8181

# ==========================================
# H2 Database
# ==========================================

spring.datasource.url=jdbc:h2:file:/app/data/kbstride
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# ==========================================
# JPA / Hibernate
# ==========================================

spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.show-sql=true

# ==========================================
# H2 Console
# ==========================================

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# ==========================================
# Thymeleaf
# ==========================================

spring.thymeleaf.cache=false
```

---

# 19. H2 Console

H2 console has been enabled for database verification.

URL:

```text
http://<EC2-IP>:8181/h2-console
```

JDBC URL:

```text
jdbc:h2:file:/app/data/kbstride
```

Username:

```text
sa
```

Password:

```text
```

The H2 console can be used to verify the `USERS` table and registered users.

---

# 20. Docker Implementation

The application is packaged as a JAR file and deployed using Docker.

Current Dockerfile:

```dockerfile
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

RUN mkdir -p /app/data

COPY target/welcome-page-0.0.1-SNAPSHOT.jar /app/welcome-page.jar

EXPOSE 8181

ENTRYPOINT ["java", "-jar", "welcome-page.jar"]
```

---

# 21. Docker Port Configuration

The application is intentionally configured to use port **8181**.

Spring Boot:

```text
8181
```

Docker:

```text
8181
```

Docker port mapping:

```text
8181:8181
```

Architecture:

```text
Internet / Browser
        |
        |
        v
EC2 Server
Port 8181
        |
        |
        v
Docker Container
Port 8181
        |
        |
        v
Spring Boot
Port 8181
```

---

# 22. Docker Image Build

After making code changes, the application is packaged:

```bash
mvn clean package
```

Docker image is then created:

```bash
docker build -t kbstride .
```

---

# 23. Running the Docker Container

The container is started using:

```bash
docker run -d \
  --name kbstride-container \
  -p 8181:8181 \
  -v kbstride-data:/app/data \
  kbstride
```

Explanation:

| Option                       | Purpose                                   |
| ---------------------------- | ----------------------------------------- |
| `-d`                         | Run container in background               |
| `--name kbstride-container`  | Container name                            |
| `-p 8181:8181`               | Map host port 8181 to container port 8181 |
| `-v kbstride-data:/app/data` | Persist H2 database                       |
| `kbstride`                   | Docker image                              |

---

# 24. H2 Database Persistence

A Docker volume is used:

```text
kbstride-data
```

The volume is mounted to:

```text
/app/data
```

Therefore:

```text
Docker Volume
     |
     v
/app/data
     |
     v
H2 Database
     |
     v
kbstride
```

This prevents the H2 database from being lost when the container is removed and recreated.

---

# 25. Application Deployment Flow

The current deployment flow is:

```text
Developer changes code
        |
        v
Maven Build
        |
        v
mvn clean package
        |
        v
JAR File
        |
        v
Docker Build
        |
        v
Docker Image
        |
        v
Docker Container
        |
        v
Spring Boot Application
        |
        v
Port 8181
```

Commands:

```bash
mvn clean package
```

```bash
docker build -t kbstride .
```

```bash
docker run -d \
  --name kbstride-container \
  -p 8181:8181 \
  -v kbstride-data:/app/data \
  kbstride
```

---

# 26. Current Registration Architecture

The current architecture is:

```text
                   KB STRIDE
                       |
                       v
                Spring Boot App
                       |
        +--------------+--------------+
        |              |              |
        v              v              v
    Welcome          Login         Signup
                                      |
                                      v
                              WelcomeController
                                      |
                                      v
                                 UserService
                                      |
                     +----------------+----------------+
                     |                                 |
                     v                                 v
               Email Validation                 Duplicate Check
                     |                                 |
                     +----------------+----------------+
                                      |
                                      v
                              BCrypt Password Hash
                                      |
                                      v
                               UserRepository
                                      |
                                      v
                                 H2 Database
```

---

# 27. Current Status

### Completed

* [x] Spring Boot application
* [x] Welcome page
* [x] Login page
* [x] Signup page
* [x] Track Order page
* [x] Docker containerization
* [x] H2 database integration
* [x] Spring Data JPA
* [x] User entity
* [x] User repository
* [x] User service
* [x] Email validation
* [x] Duplicate email validation
* [x] Case-insensitive email checking
* [x] BCrypt password hashing
* [x] H2 console
* [x] File-based H2 database
* [x] Docker volume for database persistence
* [x] Application configured to run on port 8181

### In Progress / Next

* [ ] Complete signup HTML integration with backend
* [ ] Test user registration end-to-end
* [ ] Verify users in H2
* [ ] Implement login authentication
* [ ] Password verification during login
* [ ] Login session management
* [ ] Logout functionality
* [ ] User account/profile page
* [ ] Order management
* [ ] Track order backend integration

---

# 28. End-to-End User Registration – Target Flow

The final signup flow will be:

```text
User opens KB STRIDE
        |
        v
Signup Page
        |
        v
Enter:
Full Name
Email
Mobile
Password
        |
        v
Click Sign Up
        |
        v
POST /signup
        |
        v
Validate Email
        |
        +---- Invalid
        |       |
        |       v
        |  Show error
        |
        v
Check Email
        |
        +---- Already Exists
        |       |
        |       v
        |  Show error
        |
        v
Hash Password
        |
        v
Save User
        |
        v
H2 Database
        |
        v
Redirect to Login
```

---

# 29. Important Security Notes

The application currently follows these basic security practices:

1. Passwords are not stored as plain text.
2. BCrypt is used for password hashing.
3. Email addresses are normalized to lowercase.
4. Duplicate email addresses are prevented.
5. Database persistence is handled through a Docker volume.
6. H2 console is currently enabled for development/testing.

The H2 console should be reviewed/disabled before production deployment if it is not required.

---

# 30. Next Development Phase

The next major functionality is **Login**.

Expected login flow:

```text
Login Page
    |
    v
Enter Email + Password
    |
    v
Find User by Email
    |
    v
Compare Password using BCrypt
    |
    +---- Incorrect
    |       |
    |       v
    |   Login Error
    |
    v
Create Login Session
    |
    v
User Dashboard / Home
```

The same email used during signup will be used as the login identifier.
