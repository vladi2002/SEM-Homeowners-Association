## HOA Endpoints

**Prequisites:**
- User is registered
    - Using "/register" from authentication microservice

---
### Create HOA ("/create")
---

**Request:**

Type: POST

Header: 
- Authorization : Bearer token

Body: JSON file containing,
- id : String
- country : String
- city : String

Example:
```JSON
{
    "id": "hoaId",
    "country": "Netherlands",
    "city": "Delft"
}
```

**Response:**

- 200 OK if successful
- 404 Not Found
    - if authorization header is incorrect
- 400 Bad Request
    - if user is not an HOA user
    - if something went wrong

---
### Find HOAs ("/find")
---

**Request:**

Type: GET

Header:
- Authorization : Bearer token

Body: JSON file containing,
- country : String
- city : String

Example:
```JSON
{
    "country": "Netherlands",
    "city": "Delft"
}
```

**Response:**

- 200 OK if successful
    - String containing all HOAs in that city
- 404 Not Found 
    - if no HOAs are found in the given city

---
### Join HOA ("/join")
---

**Prerequisites:**
- HOA created
    - Using "/create"

**Request:**

Type: POST

Header: 
- Authorization : Bearer token

Body: JSON file containing,
- hoaId : String

Example: 
```JSON
{
    "hoaId": "hoaId"
}
```

**Response:**

- 200 OK if successful
    - String: netId + " joined hoa " + hoaId
- 404 Not Found
    - if HOA does not exist
    - if user is not an HOA user
    - if something else went wrong

---
### Leave HOA ("/leave")
---

**Prerequisites:**
- HOA created
    - Using "/create"
- User joined HOA
    - Using "/join" or "/create"

**Request:**

Type: POST 

Header:
- Authorization : Bearer token

Body: *No body*

**Response:**

- 200 OK if successful
    - String: netId + " has left the HOA"
- 404 Not Found
    - if user is not part of HOA

---
### Find HOA ("/find-hoa/*{username}*)
---

**Prerequisites:**
- HOA created
    - Using "/create"

**Request:**

Type: GET

Header:
- Authorization : Bearer token

Pathvariables:
- *username*, username of user whose HOA needs to be found

**Response:**

- 200 OK if successful
    - String: hoaId
- 404 Not Found
    - if user is not part of HOA

---
### Find board ("/find-board/*{hoa}*)
---

**Prerequisites:**
- HOA created
    - Using "/create"

**Request:**

Type: GET

Header:
- Authorization : Bearer token

Pathvariables:
- *hoa*, hoa ID of the board you want to find

**Response:**

- 200 OK if successful
    - String containing all board members of the given HOA
- 404 Not Found
    - if HOA does not exist 