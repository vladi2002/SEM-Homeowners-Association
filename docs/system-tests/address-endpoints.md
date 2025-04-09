## Address Endpoints

**Prerequisites:**

- Registered User
    - User is registered using "/register" from authentication microservice

---
### **Update address** ("/update-address"):
---

**Request:**

Type: POST

Header: *No requirements*

Body: JSON file containing,
- Net ID : String
- (Optional) Country : String
- (Optional) City : String
- (Optional) Street : String
- (Optional) House number : int
- (Optional) Postal code : String

Example:

```JSON
{
    "netId": "netId",
    "country": "Netherlands",
    "city": "Delft",
    "street": "Mekelweg",
    "houseNumber": 4,
    "postalCode": "2628CD"
}
```

**Response:**

- 200 OK 
    - if update was successful
- 404 Not Found 
    - if net ID does not exist
- 400 Bad Request 
    - if something else went wrong / is missing

---
### Get Address ("/get-address")
---

**Request:**

Type: POST 

Header: *No requirements*

Body: JSON file containing,
- Net ID : String

Example:
```JSON
{
    "netId": "netId"
}
```

**Response:**
- 200 OK if get was successful
```JSON
{
    "country": "Netherlands",
    "city": "Delft",
    "street": "Mekelweg",
    "houseNumber": 4,
    "postalCode": "2628CD"
}
```
- 404 Not Found 
    - if net ID does not exist
- 400 Bad Request 
    - if something else went wrong / is missing