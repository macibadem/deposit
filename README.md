# Deposit Project

This repository contains the backend microservices for the **Deposit Project**, which includes services such as the API Gateway, Account Service, Customer Service, Transaction Service, and Query Service. The project is fully containerized using Docker, allowing for easy setup and deployment in a Docker environment.

## Table of Contents
- [Project Overview](#project-overview)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
    - [Clone the Repository](#clone-the-repository)
    - [Run the Services](#run-the-services)
- [Microservices](#microservices)
- [Development Notes](#development-notes)

## Project Overview

The Deposit Project is designed as a microservices-based backend system. Each service is independently deployable and communicates with others via APIs and message broker. The system includes:
- **API Gateway**: Routes requests to the appropriate service.
- **Account Service**: Manages account-related operations.
- **Customer Service**: Handles customer data.
- **Transaction Service**: Manages account transactions.
- **Query Service**: Facilitates queries from other modules.

The project is built with Docker for containerization, simplifying deployment and scaling.

## Prerequisites

Before running the project, ensure you have the following installed on your local machine:

1. **Git**: To clone the repository.
2. **Docker**: To run the services in containers.
3. **Docker Compose**: To manage the multi-container application.

You can verify installations with the following commands:
```bash
git --version
docker --version
docker-compose --version
```

Run the Services

1. Start the Docker Environment: Ensure Docker is running on your machine, then execute the following command in the project directory to start all services:
```bash
docker-compose up --build
```
This will:

Build the Docker images for all microservices.
Start the containers.

2. Access the Services: Each service is configured with specific ports in the docker-compose.yaml file. For example:
- API Gateway: http://localhost:8080
- Other services will be available on their configured ports.

3.Stop the Environment: To stop the containers, use:
```bash
docker-compose down
```

How to Run the System as a Whole

1. Run Services in Local IDE Environment:
- To run services in your local IDE environment (e.g., IntelliJ or Eclipse), you can simply execute the start-system-dev.bat (Windows) or start-system-dev.sh (Linux/Mac) file.
- This will start the Kafka container required for the account-service to push events to Kafka for creating transactions in the transaction-service.

2. Run Services as Docker Containers:
- If you don't want to use your IDE and prefer to run all services as Docker containers, you can use the start-system.bat (Windows) or start-system.sh (Linux/Mac) file.
- Running this script will spin up the entire application as Docker containers.
- Once the system is running, you can call the endpoints mentioned in the How to Test the Services section.


How to Test the Services

To test the services, follow these steps:
 
Testing Through the Front End

Login:
- reach UI at localhost:3000
- Enter a valid username (admin) and password (123456) in the Login section.
- Click the Login button.

Create Account:
- Enter an initial credit amount (e.g., 10.0) in the Create Account section.
- Click the Create Account button.

Fetch User Info
- After account creation click the Fetch User Info button in the User Info section.

Testing Through end-points

1. Login to the System
   Before accessing other services, you need to log in using an already registered customer account.

- Endpoint:
```bash
POST http://localhost:8080/api/v0/auth/login
```
- Request Body:
```bash
{
"username": "admin",
"password": "123456"
}
```
- Response: If the login is successful, you will receive a JWT token in the response. This token must be included in the Authorization header for subsequent requests.

2. Create an Account
   After obtaining the JWT token, you can create an account with the following endpoint:

- Endpoint:
```bash
  POST http://localhost:8080/api/gateway/account/v0/accounts
```
- Request Body:
```bash
{
    "initialCredit": "10.0"
}
```
This will create a new account with an initial credit of 10.0. Additionally, a transaction will be recorded for this account creation.

- Authorization: Include the token retrieved from the login endpoint in the Authorization header as a Bearer token:
```bash
Authorization: Bearer <your-jwt-token>
```

3. View User Information
   To fetch the logged-in user's information, use the following endpoint:

- Endpoint:
```bash
  GET http://localhost:8080/api/gateway/query/v0/queries/user-info
```
- Authorization: Again, include the token in the Authorization header as a Bearer token
```bash
Authorization: Bearer <your-jwt-token>
```
This endpoint will return the details of the currently logged-in user.

