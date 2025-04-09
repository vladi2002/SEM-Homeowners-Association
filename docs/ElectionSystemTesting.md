# System tests
## Election Endpoints

All endpoints need to be called with an authentication token of a user that is stored in the user microservice.

---
 **Create Election** ("/election/create"):
---

**Prerequisites:**

A HOA exists in the Hoa microservice that the user is a part of.

**Request:**

Type: POST

Header: *No requirements*

Body: JSON file containing,
- election ID : String
- Hoa ID : String

Example:

```JSON
{
  "electionId": "The name of the new election",
  "hoaId": "The ID of the HOA"
}
```

**Response:**

- 200 OK
    - if creation was successful
- 401 Not Authorized
    - if the authentication token is incorrect
- 400 Bad Request
    - if the Hoa doens't exist or an election with the same ID already exists

---
**Apply for boardmember position** ("/apply-board"):
---

**Prerequisites:**

A HOA exists in the Hoa microservice that the user is a part of.

**Request:**

Type: POST

Header: *No requirements*

Body: None


**Response:**

- 200 OK
  - if applying was successful
- 401 Not Authorized
  - if the authentication token is incorrect
- 400 Bad Request
  - if the Hoa doens't exist or the users has already applied for the board


---
**Vote for an election** ("/election/vote"):
---

**Prerequisites:**

A HOA exists in the Hoa microservice that the user is a part of, 
an election exists in that hoa and a user has applied for the board.

**Request:**

Type: POST

Header: *No requirements*

Body: JSON file containing,
- election ID : String
- Hoa ID : String
- applicant ID : String

Example:

```JSON
{
  "electionId": "The name of the election",
  "hoaId": "The ID of the HOA",
  "applicantId": "The ID of the applicant"
}
```


**Response:**

- 200 OK
  - if voting was successful
- 401 Not Authorized
  - if the authentication token is incorrect
  - 400 Bad Request if:
    - the Hoa doesn't exist
    - the user votes for themselves
    - the user has already voted in this hoa
    - the election doesn't exist in this hoa
    - the applicant doesn't exist/isn't appling for the board
    - the applicant isn't a part of the HOA




---
**Ending an election** ("/election/result"):
---

**Prerequisites:**

A HOA exists in the Hoa microservice that the user is a part of and
an election exists in that hoa.

**Request:**

Type: POST

Header: *No requirements*

Body: JSON file containing,
- election ID : String
- Hoa ID : String

Example:

```JSON
{
  "electionId": "The name of the election",
  "hoaId": "The ID of the HOA",
}
```


**Response:**

- 200 OK
  - if ending the election was successful
- 401 Not Authorized
  - if the authentication token is incorrect
  - 400 Bad Request if:
    - the Hoa doesn't exist
    - no-one has voted in the election
    - the election doesn't exist in this hoa




