## User Endpoints

---
### Register a user ("/register")
---

**Request:**

Type: POST

Header: *No requirements*

Body: JSON file containing,
- netId : String
- password : String

Example:
```JSON
{
    "netId": "netId",
    "password": "password"
}
```

**Response:**

- 200 OK if successful
- 400 Bad Request
    - if registration failed

---
### Authenticate a user ("/authenticate")
---

**Prerequisites:**
- User is registered
    - Using "/register"

**Request:**

Type: POST

Header: *No requirements*

Body: JSON file containing,
- netId : String
- password : String

Example:
```JSON
{
    "netId": "netId",
    "password": "password"
}
```

**Response:**

- 200 OK if successful
```JSON
{
    "token": "Example token"
}
```
- 401 Unauthorized
    - if user is disabled
    - if credentials are incorrect

---
### Update Password ("/updatePassword")
---

**Prerequisites:**
- User is registered
    - Using "/register"

**Request:**

Type: POST

Header: *No requirements*

Body: JSON file containing,
- netId : String
- oldPassword : String
- newPassword : String

Example:
```JSON
{
    "netId": "netId",
    "oldPassword": "password",
    "newPassword": "newPassword"
}
```

**Response:**

- 200 OK if successful
- 401 Unauthorized
    - if user is disabled
    - if credentials are incorrect
- 400 Bad Request
    - if something else is wrong