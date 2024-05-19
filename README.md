
# Bank-Service-API

REST API application about bank service based on Spring Boot. Test task from "Effective Mobile" company ([Effective Mobile](https://effective-mobile.ru/)).

## Project Description

This project involves creating a service for bank operations. In our system, each client has exactly one bank account with an initial deposit amount. Clients can transfer funds between accounts, and interest is accrued on their balances.

## Functional Requirements

1. **Users and Accounts**:
   - Each user has exactly one bank account.
   - Must have at least one contact method, either a phone number or an email.
   - Each bank account starts with an initial deposit.
   - Users also have a birthdate and full name (FIO) recorded.

2. **User Registration**:
   - A public API (with open access) allows for creating new users by specifying a username, password, initial amount, phone, and email.
   - The username, phone, and email must be unique.

3. **Account Balances**:
   - A user's account balance cannot go negative under any circumstances.

4. **Update Contact Information**:
   - Users can update their phone numbers and/or emails unless they are already taken by another user.

5. **Delete Contact Information**:
   - Users can delete their phone and/or email, but they cannot delete the last remaining contact method.

6. **Immutable User Data**:
   - Users cannot change other recorded data.

7. **Search API**:
   - Allows for searching any client with filtering, pagination, and sorting.
   - Filters include:
      - Birthdate greater than a specified date.
      - Exact match for phone number.
      - Partial match for FIO using a 'like' pattern (`{text-from-request-param}%`).
      - Exact match for email.

8. **Authenticated Access**:
   - Access to the API requires authentication, except for the service API used for creating new clients.

9. **Interest Accrual**:
   - Every minute, each client's balance increases by 5% but does not exceed 207% of the initial deposit.

10. **Fund Transfers**:
   - Implement the functionality to transfer funds from the authenticated user's account to another user's account.
   - Include all necessary validations and ensure thread safety.

## Non-Functional Requirements

- **OpenAPI/Swagger**: Include API documentation.
- **Logging**: Implement logging throughout the application.
- **Authentication**: Use JWT for authentication.
- **Testing**: Develop tests to cover the money transfer functionality.

## Technology Stack

- Java 17
- Spring Boot 3
- PostgreSQL Database
- Maven
- REST API
- Optional technologies (Redis, ElasticSearch, etc.) at your discretion.
- No frontend required

## Deliverables

Provide the result as a public repository on GitHub.

## Setup and Running the Application

To set up the application for running, follow these steps:

1. Create a copy of `src/main/resources/db/db-config.yaml.original` and rename it to `db-config.yaml`.
2. Configure `db-config.yaml` with the correct database path, login, and password.
3. Create a copy of `src/main/resources/jwt/jwt-config.yaml.original` and rename it to `jwt-config.yaml`.
4. Configure `jwt-config.yaml` with a secret key. The key length should be at least 256 bits.
5. Run the application. The database will automatically populate with test data.
6. Navigate to [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) to test the application.
