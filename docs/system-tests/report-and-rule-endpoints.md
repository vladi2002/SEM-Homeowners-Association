## Report and Rule Endpoints

**Prerequisites:**
- User is registered
    - Using "/register" from authentication service
- HOA created
    - Using "/create"

---
### Get rules ("/get-rules")
---

**Prerequisites:**
- There needs to be a rule in the database
- User joined HOA
    - Using "/join" or "/create"

**Request:**

Type: GET

Header:
- Authorization : Bearer token

Pathvariables: *No variables*

**Response:**

- 200 OK if successful
    ```JSON
    [{
        "id": 1,
        "ruleId": 1,
        "hoaId": "hoaId",
        "rule text": "Example rule"
    },
    {
        "id": 2,
        "ruleId": 2,
        "hoaId": "hoaId",
        "rule text": "Second example rule"
    }]
    ```
- 400 Bad Request
    - if user is not part of HOA
    - if something else went wrong