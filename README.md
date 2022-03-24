# inventory-api
**Overview: **

This project is a Hotel Inventory API designed to create hotels and associated reservations. The user can create a hotel, search for available hotels, search for all hotels within the database, all reservations within the database, make reservations to a specific hotel, delete reservations as well as hotels. Necessarry validation is incorporated within the API that prevents illogical operations from occuring such as making an overlapping reservation to a hotel or deleting a hotel that contains reservations.

**Detailed Overview: **

**Valid API Calls: **

**  Use Case: Create Hotel/Inventory **
  - Endpoint: /inventories
  - Verb: POST
  - Description: Create a hotel inventory object. 
  - Validation (Throws an InvalidRequestException when): 
    - Type is not DELUXE, LUXURY, or SUITE
    - Date is not YYYY-MM-DD
    - Only one date is recieved
    - availableTo date comes before availableFrom date
    - Name is null 
  - Request Body: 
      { 
      "name" : "String", 
      "type" : String(LUXURY, DELUXE, SUITE), 
      "description" : String,   
      "availableFrom" : "YYYY-MM-DD", 
      "availableTo" : "YYYY-MM-DD" 
      }

**  Use Case: Update a hotel inventory object**
  - Endpoint: /inventories
  - Verb: PATCH
  - Description: Updates an existing hotel inventory object.
  - Validation (Throws an InvalidRequestException when): 
    - ID does not exist 
    - Type is not DELUXE, LUXURY, or SUITE
    - Date is not YYYY-MM-DD
    - Only one date is recieved
    - availableTo date comes before availableFrom date
    - Name is null 
    - Request Body: 
      { 
      "id" : Integer, 
      "name" : "String", 
      "type" : String(LUXURY, DELUXE, SUITE), 
      "description" : String,   
      "availableFrom" : "YYYY-MM-DD", 
      "availableTo" : "YYYY-MM-DD" 
      }

**  Use Case: Get a user specifiec hotel inventory object. **
  - Endpoint: /inventories/{id}
  - Verb: GET
  - Description: Get a user specified inventory object. 
  - Validation (Throws an InvalidRequestException when): 
    -  ID does not exist 
  -  Request Body: 
    -  N/A

**  Use Case: Get all existing hotel inventories **
  - Endpoint: /inventories
  - Verb: GET
  - Description: Get all existing hotel inventories 
  - Validation (Throws an InvalidRequestException when): 
    - N/A
  -  Request Body: 
    -  N/A

**  Use Case: Delete a user specified hotel inventory. **
  - Endpoint: /inventories/{id}
  - Verb: DELETE
  - Description: 
  - Validation (Throws an InvalidRequestException when): 
    - ID does not exist 
    - Reservations for this hotel inventory object exist (A Reservations object that contains a foreign key associated with the user inputted ID exists) 
  - Request Body: 
    -  Path Variable Integer "id" 

**  Use Case: Create a reservation**
  - Endpoint: /reservations
  - Verb: POST
  - Description: Creates a hotel reservation with an existing hotel inventory. 
  - Validation (Throws an InvalidRequestException when): 
    - The hotel inventory object ID does not exist (The foreign key does not exist) 
    - Check in and check out dates are not in YYYY-MM-DD format
    - Check in and check out dates do not exist
    - Check in date comes after check out date
    - Number of guests is not an Integer 
    - A reservation object with overlapping dates already exists for the hotel inventory object
  - Request Body: 
    - {
    "inventoryId": Integer (existing hotel inventory id),
    "checkIn": "YYYY-MM-DD",
    "checkOut": "YYYY-MM-DD",
    "guests": Integer
    }

**  Use Case: Get all reservations. **
  - Endpoint: /reservations
  - Verb: GET
  - Description: Get all exsiting hotel reservations. 
  - Validation (Throws an InvalidRequestException when): 
    - N/A
  -  Request Body: 
    - N/A


**  Use Case: Get a specified hotel reservation. **
  - Endpoint: /reservations/{id}
  - Verb: GET
  - Description: Get an exsiting user specified Hotel reservation 
  - Validation (Throws an InvalidRequestException when): 
    - ID does not exist 
  - Request Body: 
    -  Path variable Integer "id"

**  Use Case: Delete a user specified reservation. **
  - Endpoint: /reservations/{id}
  - Verb: DELETE
  - Description: Delete an existing user specified reservation. 
  - Validation (Throws an InvalidRequestException when): 
    - ID does not exist 
  - Request Body: 
    - Path variable Integer "id" 

**  Use Case: Get all available hotel inventories. **
  - Endpoint: /inventories/availabilitySearch?dateFrom={from}&dateTo={to}
  - Verb: GET
  - Description: Gets all available hotel inventories between specified dates. This endpoint takes into account pre-exisiting reservations and hotel availability dates and only returns hotel inventories that do not have overlapping reservations and do not have availibility dates that start or end between the user specified dates. 
  - Validation (Throws an InvalidRequestException when): 
    - Dates are not in YYYY-MM-DD format
    - dateFrom comes after dateTo
    - One or no dates are inputted 
  - Request Body: 
    - Path Variables dateTo and dateFrom, in YYYY-MM-DD format

The Hotel Inventory API allows a user to create a hotel by sending a JSON to an endpoint in the API. The Hotel JSON will contain information such as a name, type, description and available dates. Validation occurs within this project which prevents a user from inputting incorrect information, such as an "availableTo" date that comes before a "availableFrom" date. An ID is automatically assigned to the hotel in the SQL database and returned to the user.

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
