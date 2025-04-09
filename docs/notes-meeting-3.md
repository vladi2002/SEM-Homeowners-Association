# Notes meeting 3 (Week 2.4) 8.12.2022

### General points
- Bounded contexts need to be smaller
    - HOA needs to be more seperated
    - More granularity
- We can make a new draft for the report and send it, if TA has time he will look at it
- Use REST API for endpoints
- Make sure we address TA feedback somewhere (Reports etc.)
- User/authentication microservice can be generic, just explain clearly in report why we chose this
- PMD might give errors, because no @Transient annotation
    - If any errors don't make sense, we can remove them

### Questions about UML diagram and architecture
- Add boxes around components for clarity using ports etc.
    - Add a box for frontend too, not to implement, but to show what endpoints are used by a potential frontend
- Add communications between microservices, using lollypops etc.
- Possibly implement a gateway to mock frontend for testing and demos
- Use 1 lollypop and list all endpoints for a microservice from that
- No need to explain all endpoints in the report

### Next weeks
- Assigment 1: 16 december
    - 2 design patterns need to be implemented
        - Decorator pattern (Voting)
        - Facade pattern (Activity)
    - Architecture done
- Week 2.6 friday: *Final* Deadline for project
