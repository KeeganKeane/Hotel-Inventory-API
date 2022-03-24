# inventory-api
**Overview: **

This project is a Hotel Inventory API designed to create hotels and associated reservations. The user can create a hotel, search for available hotels, search for all hotels within the database, all reservations within the database, make reservations to a specific hotel, delete reservations as well as hotels. Necessarry validation is incorporated within the API that prevents illogical operations from occuring such as making an overlapping reservation to a hotel or deleting a hotel that contains reservations.

**Detailed Overview: **

Valid API Calls: 

Use Case: Create Hotel/Inventory 
Endpoint: /inventories
Verb: POST
Description: Create a hotel inventory object. 

Use Case: Update a hotel inventory object
Endpoint: /inventories
Verb: PATCH
Description: Updates an existing hotel inventory object.

Use Case: Get a user specifiec hotel inventory object. 
Endpoint: /inventories/{id}
Verb: GET
Description: Get a user specified inventory object. 

Use Case: Get all existing hotel inventories 
Endpoint: /inventories
Verb: GET
Description: Get all existing hotel inventories 

Use Case: 
Endpoint: 
Verb: 
Description: 

Use Case: 
Endpoint: 
Verb: 
Description: 

Use Case: 
Endpoint: 
Verb: 
Description: 

Use Case: 
Endpoint: 
Verb: 
Description: 

Use Case: 
Endpoint: 
Verb: 
Description: 

Use Case: 
Endpoint: 
Verb: 
Description: 

Use Case: 
Endpoint: 
Verb: 
Description: 


The Hotel Inventory API allows a user to create a hotel by sending a JSON to an endpoint in the API. The Hotel JSON will contain information such as a name, type, description and available dates. Validation occurs within this project which prevents a user from inputting incorrect information, such as an "availableTo" date that comes before a "availableFrom" date. An ID is automatically assigned to the hotel in the SQL database and returned to the user.

An example JSON is as follows.

{ 
  "name" : "Example Hotel" 
  "type" : "LUXURY" 
  "description" : "This is the most luxurious hotel in the tri-state area." 
  "availableFrom" : "2022-02-01"
  "availableTo" : "2024-02-01" 
}


**Tech Stack: **

- API Creation 
  - Java
  - MySQL 
  - SpringBoot 
  - Hibernate 
  - JPA 
  - Lombok 
- Testing 
  - JUnit 5
  - Mockito 
  - H2
- User Input Testing 
  - Swagger UI
  - Postman 
