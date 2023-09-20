# bank_application_project_v1
share bank_application_project on GitHub

Project Description: 
Backend Development of a Banking Microservice 

Objective: 
The main objective of this project is to create a robust backend microservice for banking operations without a frontend component. 
The microservice will expose a RESTful API to perform internal banking transactions and manage customer accounts.

Technologies: 
Spring Boot: Used for application development and streamlined setup and deployment. 
Spring Security: Used for providing both authentication and authorization to the application.
Hibernate and JPA: Utilized for database interaction and object-relational mapping. 
MySQL: Utilized as the database for storing application data. 
Liquibase: Employed for managing database migrations. 
Spring Test, Junit, Mockito: Utilized for creating mock objects and performing unit testing. 
Swagger: Used for documenting the API and generating interactive documentation. 
SLF4J: Employed for logging and event registration within the application.

Bank application functionality: 
 The Bank Application has separate functionality for client and admin after authentication in the Bank Application. 

An admin has access to information about 
-	managers, products, bank accounts, clients, transactions 
-	and has possibility to create new manager, product, bank accounts, clients,  but cannot execute transactions

A client has:
-	access only to information about the list of his bank accounts, and the list of all his transactions,
-	possibility to create new transactions, possibility to update and delete  transactions with status DRAFT_INVALID, DRAFT_VALID,
   and can not delete transactions with other status,
-	transaction can be execute only after AUTHORIZATION and  if bank account has enough amount of the account balance,
-	not possibility create a new bank account,
-	and do not have access any other information.
