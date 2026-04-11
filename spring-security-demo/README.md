# 🔐 Spring Boot JWT Authentication (Access + Refresh Token)

## 🚀 Overview

This project implements **JWT-based authentication** using:

* Spring Boot
* Spring Security
* MySQL
* Access Token + Refresh Token

---

## 🧱 Tech Stack

* Java 17+
* Spring Boot
* Spring Security
* MySQL
* JJWT (0.11.x)
* Postman (Testing)

---

## ⚙️ Setup Instructions

### 1️⃣ Clone Project

```bash
git clone <your-repo-url>
cd project-folder
```

---

### 2️⃣ Configure Database

Update `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/jwt_db?createDatabaseIfNotExist=true
    username: root
    password: root
```

---

### 3️⃣ Run Application

```bash
mvn spring-boot:run
```

App runs on:

```
http://localhost:8080
```

---

## 🧪 API TESTING (Postman)

---

## 🔹 1. Register User

**POST** `/register`

### Body (JSON)

```json
{
  "username": "ns3",
  "password": "1234"
}
```

### Response

```
User Registered
```

---

## 🔹 2. Login (Get Tokens)

**POST** `/login`

### Body

```json
{
  "username": "ns3",
  "password": "1234"
}
```

### Response

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs..."
}
```

👉 Copy the **accessToken**

---

## 🔹 3. Access Protected API

**GET** `/api/data`

### Headers

```
Authorization: Bearer <accessToken>
```

### Response

```
Protected Data ✅
```

---

## 🔹 4. Refresh Token

**POST** `/refresh`

### Body

```json
{
  "refreshToken": "your_refresh_token"
}
```

### Response

```json
{
  "accessToken": "new_access_token"
}
```

---

## 🔁 Flow Summary

```
Register → Login → Get Tokens
↓
Use Access Token → API Calls
↓
Expired → Use Refresh Token
↓
Get New Access Token
```

---

## ⚠️ Common Errors

### ❌ 401 Unauthorized

* Token missing or invalid
* Token expired

---

### ❌ Missing Bearer

Wrong:

```
Authorization: eyJhbGciOiJIUzI1NiIs...
```

Correct:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```

---

### ❌ Database Not Connecting

* Check MySQL running
* Verify username/password

---

## 💡 Tips

* Use Postman **Authorization tab → Bearer Token**
* Keep access token short-lived
* Store refresh token securely

---

## 🎯 Endpoints Summary

| Method | Endpoint  | Description          |
| ------ | --------- | -------------------- |
| POST   | /register | Register user        |
| POST   | /login    | Login & get tokens   |
| POST   | /refresh  | Refresh access token |
| GET    | /api/data | Protected API        |

---

## 🔐 Security Notes

* Passwords are encrypted using BCrypt
* JWT is signed (not encrypted)
* Do NOT store sensitive data in token

---

## 🎉 Done!

You now have a **fully working JWT authentication system** 🚀
