## Proposal Endpoints

**Prerequisites:**
- Registered user
    - User is registered using "/register" from authentication microservice
- Authenticated user
    - User is authenticated using "/authenticate" from authentication microservice
- Existing HOA
    - HOA is created using "/create" from HOA microservice

---
### Create Proposal ("/create-proposal")
---

**Prerequisites:**
- User is board member of an HOA

**Request:**

Type: POST

Header:
- Authorization : Bearer token

Body: JSON file containing,
- Proposal ID : String
- Hoa ID : String
- Proposal : String
- Proposal type : String
    - GENERAL
    - RULE
- (Optional) Rule ID : int

Examples: 
```JSON
// General proposal
{
    "proposalId": "proposalId",
    "hoaId": "hoaId",
    "proposal": "This is a general proposal.",
    "proposalType": "GENERAL"
}

// Rule proposal
{
    "proposalId": "newProposalId",
    "hoaId": "hoaId",
    "proposal": "This is a rule proposal.",
    "proposalType": "RULE",
    "ruleId": 1
}
```

**Response:**
- 200 OK if creation was successful
- 404 Not Found
    - if HOA does not exist
    - if user does not exist
    - if rule ID does not exist
- 401 Unauthorized
    - if user is not part of an HOA
    - if user is not a board member
- 400 Bad Request
    - if proposal type is incorrect
    - if proposal already exists
    - if rule ID is invalid (can not be 0)
    - if something else went wrong / is missing

---
### Vote Proposal ("/vote-proposal")
---

**Prerequisites:**
- User is board member of an HOA
- Existing proposal
    - Created using "/create-proposal"

**Request:**

Type: POST

Header: 
- Authorization : Bearer token

Body: JSON file containing,
- Proposal ID : String
- Hoa ID : String
- Decision : String
    - ACCEPT
    - REJECT
    - ABSTAIN

Example:
```JSON
{
    "proposalId": "proposalId",
    "hoaId": "hoaId",
    "decision": "ACCEPT" // or REJECT, ABSTAIN
}
```

**Response:**
- 200 OK if vote was successfully handled
- 404 Not Found
    - if HOA does not exist
    - if proposal does not exist
    - if user does not exist
- 401 Unauthorized
    - if user is not part of an HOA
    - if user is not a board member
- 400 Bad Request
    - if voting is not opened yet (less than 2 weeks old)
    - if voting has closed (opened for 3 days)
    - if something else went wrong / is missing

---
### Get Proposal ("get-proposal/*{hoaId}*/*{proposalId}*")
---

**Prerequisites:**
- Existing proposal
    - Created using "/create-proposal"

**Request:**

Type: GET

Header: 
- Authorization: Bearer token

Path variables:
- *hoaId*, the ID of the HOA
- *proposalId*, the ID of the proposal

**Response:**

- 200 OK if proposal exists
```JSON
// General proposal "/get-proposal/hoaId/proposalId"
{
    "proposal": "This is a general proposal.",
    "type": "GENERAL",
    "accept": 1,
    "reject": 0,
    "abstain": 0,
    "creationDate": "22-DECEMBER-2022",
    "ruleId": 0 
}

// Rule proposal "/get-proposal/hoaId/newProposalId"
{
    "proposal": "This is a rule proposal.",
    "type": "RULE",
    "accept": 0,
    "reject": 0,
    "abstain": 0,
    "creationDate": "22-DECEMBER-2022",
    "ruleId": 1
}
```
- 404 Not Found
    - if proposal does not exist
- 400 Bad Request
    - if something else went wrong / is missing