# Homeowners Association (HOA) System

## About
This project was done for the course *Software Engineering Methods CSE2115* at TU Delft by:
- Alex Brown
- Bram Snelten
- Jelt Jongsma
- Rafael Petouris
- Roland Bockholt
- Vladimir Petkov

The aim was to create a system of microservices to set up Homeowners Associations. 
The application adheres as close as possible to the requirements specified in Scenario: Home Owners Association.



## Microservices

This project contains three microservices:
- activity-microservice which is hosted on localhost:8004
- authentication-microservice which is hosted on localhost:8001
- hoa-microservice which is hosted on localhost:8003

The `activity-microservice` is responsible for handling all activities.
- Create activities
- Update activities
- Respond to activities

The `authentication-microservice` is responsible for maintaining users and authentication.
- Register users
- Update users
- Authenticate users

The `hoa-microservice` is responsible for handling all HOA related requests.
- Create & update HOAs
- Create & update proposals
- Create & update elections
- Handle reports

The `domain` and `application` packages contain the code for the domain layer and application layer. The code for the framework layer is the root package as *Spring* has some limitations on were certain files are located in terms of autowiring.

## Running the microservices

You can run the three microservices individually by starting the Spring applications. Then, you can use *Postman* to perform the different requests.

To use our postman methods with already defined requests, you can click on this link: https://app.getpostman.com/join-team?invite_code=5c38e995f1930f25c354db4005e99d7d.
The requests are organized by microservice and by endpoint. Not all endpoints have requests saved but do work. In the environments tab you can find our saved variables that are used in the requests. You need to update the tokens to authenticate the requests.

How to test the microservices is explained in the *docs/system-tests* folder. This way you can systematically test the microservice, while they're all running.
Each document is named after the endpoints they contain. They all have a part called *Prerequisites*, in here is explained what other steps need to be taken for this endpoint to give the desired response.

