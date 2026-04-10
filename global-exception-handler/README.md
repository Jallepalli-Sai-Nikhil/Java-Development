# 🧠 Core Concepts Explained (Detailed)

---

## 🔹 1. ResponseEntity

### 📘 Theory

`ResponseEntity` is a Spring class used to represent the **entire HTTP response** returned by a REST API. It provides control over three important parts of a response: the response body, the HTTP status code, and optional headers.

In REST API design, returning correct HTTP status codes is very important. By default, Spring Boot returns `200 OK` for successful responses, but real-world applications require more control. This is where `ResponseEntity` becomes essential.

---

### 🛠 Practical Usage

Example:

```java
return ResponseEntity.ok(userResponseDTO);
```

For creating a resource:

```java
return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
```

For error responses:

```java
return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Invalid request");
```

---

### 🎯 Why It Matters

Using `ResponseEntity` ensures that:

* APIs follow proper HTTP standards
* Clients receive meaningful status codes
* Responses are flexible and customizable

It is widely used in production systems where APIs need to communicate clearly with frontend or external services.

---

## 🔹 2. Global Exception Handler

### 📘 Theory

A Global Exception Handler is a centralized mechanism in Spring Boot used to handle exceptions across the entire application using `@ControllerAdvice`.

Without it, developers would need to write try-catch blocks in every controller method, which leads to repetitive and messy code. Instead, Spring allows us to define a single class that handles all exceptions in one place.

---

### 🛠 Practical Usage

Example:

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {

        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(404)
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
```

---

### 🎯 Why It Matters

Global exception handling provides:

* Centralized error management
* Consistent error response format
* Cleaner controller code (no try-catch blocks)

It is a critical component in building scalable and maintainable backend systems.

---

## 🔹 3. Builder Pattern (Lombok)

### 📘 Theory

The Builder Pattern is a creational design pattern used to construct objects step by step. In Spring Boot, Lombok provides this functionality using the `@Builder` annotation.

Instead of using constructors with multiple parameters, the builder pattern allows developers to set fields individually in a readable and flexible way.

---

### 🛠 Practical Usage

Example:

```java
User user = User.builder()
        .name("Ns3")
        .email("ns3@gmail.com")
        .build();
```

Without builder:

```java
User user = new User(null, "Ns3", "ns3@gmail.com");
```

---

### 🎯 Why It Matters

Using the builder pattern:

* Improves readability of object creation
* Avoids confusion with constructor parameters
* Works well with large objects having many fields

It is commonly used in DTOs, entities, and response objects in real-world applications.

---

# 🌐 API Design (Detailed Explanation)

---

## 🔹 1. Create User (POST /users)

### 📘 Theory

This API is used to create a new user in the system. It accepts input data (name and email) and stores it in the database. According to REST principles, POST is used for resource creation.

---

### 🛠 Practical Usage

Request:

```json
{
  "name": "Ns3",
  "email": "ns3@gmail.com"
}
```

Response:

```json
{
  "id": 1,
  "name": "Ns3",
  "email": "ns3@gmail.com"
}
```

Status Code:
`201 CREATED`

---

## 🔹 2. Get User By ID (GET /users/{id})

### 📘 Theory

This API retrieves a specific user based on their unique ID. GET is used because it is a read-only operation.

---

### 🛠 Practical Usage

Request:

```
GET /users/1
```

Response:

```json
{
  "id": 1,
  "name": "Ns3",
  "email": "ns3@gmail.com"
}
```

If user not found:

* Exception is thrown
* Handled by Global Exception Handler

---

## 🔹 3. Get All Users (GET /users)

### 📘 Theory

This API fetches all users from the database. It is useful for listing or displaying multiple records.

---

### 🛠 Practical Usage

Response:

```json
[
  {
    "id": 1,
    "name": "Ns3",
    "email": "ns3@gmail.com"
  }
]
```

Status Code:
`200 OK`

---

## 🔹 4. Update User (PUT /users/{id})

### 📘 Theory

This API updates an existing user’s details. PUT is used because it replaces or updates a resource.

---

### 🛠 Practical Usage

Request:

```json
{
  "name": "Updated Name",
  "email": "updated@gmail.com"
}
```

Response:

```json
{
  "id": 1,
  "name": "Updated Name",
  "email": "updated@gmail.com"
}
```

---

## 🔹 5. Delete User (DELETE /users/{id})

### 📘 Theory

This API deletes a user from the database using their ID. DELETE method is used for removing resources.

---

### 🛠 Practical Usage

Request:

```
DELETE /users/1
```

Response:

```json
"User deleted successfully"
```

Status Code:
`200 OK`

---

# 🔄 Application Flow

```id="flow123"
Client → Controller → Service → Repository → Database
              ↓
        Exception (if occurs)
              ↓
   Global Exception Handler
              ↓
       ResponseEntity → Client
```

---

# 🚀 Final Summary

This project demonstrates how to build a clean and scalable REST API using:

* ResponseEntity for proper HTTP communication
* Global Exception Handling for centralized error management
* Builder Pattern for clean object creation
* REST APIs following standard conventions

Together, these concepts form the foundation of real-world backend development.
