## Activity Endpoints

**Prerequisites:**

- Registered User
    - User is registered using "/register" from authentication microservice
- User is a member of a HOA
    - User is registered in a HOA using "/create" or "/join" from the HOA microservice

---
### Register Activity ("/registerActivity")
---

**Request:**

Type: POST 

Header:
- Authorization: Bearer token

Body: JSON file containing,
- Description : String
- Date in the format "YYYY-MM-dd HH:mm": String

Example:
```JSON
{
    "description": "today",
    "date": "2022-20-12 12:30"
}
```

**Response:**
- 200 OK if post was successful 
    - "Activity created successfully! " +
 the activity string: "Activity: " + the generated id + ", organized by: " + your organizer username
  ", on " + the date you've provided + " at " + your HOA
  " with description: " + the description you've provided + ". Responses: " + the response list of the newly created activity (initially having just a response with you as GOING)

- 400 Bad Request 
    - if the date can not be parsed correctly
- 400 Bad Request 
    - if the user is not a member of any HOA
- 401 Unauthorized
    - if user is not part of an HOA
    - if user is not a board member

---
### Register Response ("/respond")
---

**Request:**

Type: POST 

Header:
- Authorization: Bearer token

Body: JSON file containing,
- ActivityId : int
- Response : ResponseOption
    - GOING
    - INTERESTED
    - NOT_INTERESTED

Example:
```JSON
{
    "activityId": "1",
    "response": "GOING" // or INTERESTED, NOT_INTERESTED
}
```

**Response:**
- 200 OK if post was successful 
    - the string of your response + " added to " + the string of your activity
- 400 Bad Request + UserDoesNotBelongToThisHoaException
    - if you are attempting to respond to an activity in an HOA you don't belong to
- 400 Bad Request + ActivityNotExistsException
    - if the activity you're trying to respond does not exist
- 401 Unauthorized
    - if user is not part of an HOA
    - if user is not a board member
    
---
### Find Activity by Id ("/byId/*{id}*")
---

**Request:**

Type: GET 

Header:
- Authorization: Bearer token

Path variables:
- id, the ID of the activity we're looking for

**Response:**
- 200 OK if get was successful 
    - "Activity " + the string of the activity found
- 400 Bad Request + ActivityNotExistsException
    - if the activity you're trying to find does not exist
- 401 Unauthorized
    - if user is not part of an HOA
    - if user is not a board member
---
### Get All Activities from your HOA ("/getAll")
---

**Request:**

Type: GET 

Header:
- Authorization: Bearer token

**Response:**
- 200 OK if get was successful 
    - "Activities: " + the string of the list of all activities found in your HOA
- 400 Bad Request + Exception
    - if no activities have been found in your HOA or you are not an HOA member
- 401 Unauthorized
    - if user is not part of an HOA
    - if user is not a board member

---
### Get All Upcoming Activities from your HOA ("/getAllUpcoming")
---

**Request:**

Type: GET 

Header:
- Authorization: Bearer token

**Response:**
- 200 OK if get was successful 
    - "Activities: " + the string of the list of all upcoming activities found in your HOA
- 400 Bad Request + Exception
    - if no (upcoming) activities have been found in your HOA or you are not an HOA member
- 401 Unauthorized
    - if user is not part of an HOA
    - if user is not a board member