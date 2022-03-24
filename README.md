# inventory-api
**Overview: **

This project is a Hotel Inventory RESTful API designed to create hotels and associated reservations. The user can create a hotel, search for available hotels, search for all hotels within the database, all reservations within the database, make reservations to a specific hotel, delete reservations as well as hotels. Necessarry validation is incorporated within the API that prevents illogical operations from occuring such as making an overlapping reservation to a hotel or deleting a hotel that contains reservations.

The inventory-api follows a standard "User <-> Controller <-> Validator (Only interacts with Controller) <-> Service <-> Repository <-> Database" API schema. Where the data flows down from the User to the database and then back to the User. 

**Detailed Overview: **

****Valid API Calls: ****

****  Use Case: Create Hotel/Inventory ****
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

****  Use Case: Update a hotel inventory object****
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

****  Use Case: Get a user specific hotel inventory object. ****
  - Endpoint: /inventories/{id}
  - Verb: GET
  - Description: Get a user specified inventory object. 
  - Validation (Throws an InvalidRequestException when): 
    -  ID does not exist 
  -  Request Body: 
    -  N/A

****  Use Case: Get all existing hotel inventories ****
  - Endpoint: /inventories
  - Verb: GET
  - Description: Get all existing hotel inventories 
  - Validation (Throws an InvalidRequestException when): 
    - N/A
  -  Request Body: 
    -  N/A

****  Use Case: Delete a user specified hotel inventory. ****
  - Endpoint: /inventories/{id}
  - Verb: DELETE
  - Description: 
  - Validation (Throws an InvalidRequestException when): 
    - ID does not exist 
    - Reservations for this hotel inventory object exist (A Reservations object that contains a foreign key associated with the user inputted ID exists) 
  - Request Body: 
    -  Path Variable Integer "id" 

****  Use Case: Create a reservation****
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

****  Use Case: Get all reservations. ****
  - Endpoint: /reservations
  - Verb: GET
  - Description: Get all exsiting hotel reservations. 
  - Validation (Throws an InvalidRequestException when): 
    - N/A
  -  Request Body: 
    - N/A


****  Use Case: Get a specified hotel reservation. ****
  - Endpoint: /reservations/{id}
  - Verb: GET
  - Description: Get an exsiting user specified Hotel reservation 
  - Validation (Throws an InvalidRequestException when): 
    - ID does not exist 
  - Request Body: 
    -  Path variable Integer "id"

****  Use Case: Delete a user specified reservation. ****
  - Endpoint: /reservations/{id}
  - Verb: DELETE
  - Description: Delete an existing user specified reservation. 
  - Validation (Throws an InvalidRequestException when): 
    - ID does not exist 
  - Request Body: 
    - Path variable Integer "id" 

****  Use Case: Get all available hotel inventories. ****
  - Endpoint: /inventories/availabilitySearch?dateFrom={from}&dateTo={to}
  - Verb: GET
  - Description: Gets all available hotel inventories between specified dates. This endpoint takes into account pre-exisiting reservations and hotel availability dates and only returns hotel inventories that do not have overlapping reservations and do not have availibility dates that start or end between the user specified dates. 
  - Validation (Throws an InvalidRequestException when): 
    - Dates are not in YYYY-MM-DD format
    - dateFrom comes after dateTo
    - One or no dates are inputted 
  - Request Body: 
    - Path Variables dateTo and dateFrom, in YYYY-MM-DD format

**Database**
  The relational database I used for this project was created using SQL using mySQL. The database structure contains two tables, an Inventory table that contains the hotel inventory object and a Reservations table. The Reservations table contains reservations objects and is associated with the Inventory table through the Inventory table's Id by storing it as a foreign key in its "Inventory Id" value. The table structures and values are as follows: 
 
  - Inventory 
    - Id: Integer (Primary Key) 
    - Name: VarChar
    - Type: VarChar
    - Description: VarChar
    - Availiable From: Date 
    - Available To: Date 
    - Status: Boolean 
  - Reservations 
    -  Id: Integer (Primary Key) 
    -  Inventory Id: Integer (Foreign Key) 
    -  Check in Date: Date 
    -  Check out Date: Date
    -  Number of Guests: Integer 
    -  Status: Boolean 
  
  The database was created with the commands as follows: 
  
  
DROP TABLE INVENTORY IF EXISTS;
DROP TABLE RESERVATION IF EXISTS;
 
CREATE TABLE INVENTORY (
    id INT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(30) NOT NULL,
    TYPE VARCHAR (10) NOT NULL,
    DESCRIPTION VARCHAR(255),
    DT_AVAILABLE_FROM DATE,
    DT_AVAILABLE_TO DATE,
    STATUS BOOLEAN
)  ENGINE=INNODB;
 
CREATE TABLE RESERVATION (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    DT_CHECK_IN DATE NOT NULL,
    DT_CHECK_OUT DATE NOT NULL,
    GUESTS INT default 1,
    STATUS BOOLEAN,
    INVENTORY_ID INT,
    FOREIGN KEY (INVENTORY_ID) REFERENCES INVENTORY (ID)
) ENGINE=INNODB;
 
ALTER TABLE RESERVATION
ADD CONSTRAINT unique_reservation
UNIQUE (INVENTORY_ID, DT_CHECK_IN, DT_CHECK_OUT, STATUS);
  
**Testing **
The tests I created for this project incorporated both JUNit and Integration tests. I used Mockito is several unit tests and H2 in memory databases for the integration tests. More elaborate descriptions of the tests and their functionalities can be found in the test folder within this project. 

**Tech Stack: **

- API Creation:
  - Java
  - MySQL 
  - SpringBoot 
  - Hibernate 
  - JPA 
  - Lombok 
- Testing:
  - JUnit 5
  - Mockito 
  - H2
- User Input Testing:
  - Swagger UI
  - Postman 
