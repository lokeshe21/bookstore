# Bookstore Application
## Features

The Bookstore Application is a simple Spring Boot application that manages products in a bookstore.

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Getting Started](#getting-started)
- [Build and Run](#build-and-run)
- [Endpoints](#endpoints)
- [Payload](#payload)
- [Dependencies](#dependencies)

## Overview

The application consists of three main components:
- **Domain Class**: Represents the structure of a product in the bookstore.
- **Service Class**: Implements business logic for product management.
- **Controller Class**: Defines API endpoints for interacting with products.

## Features

- Create a new product.
- Retrieve a product by ID.
- Update an existing product.
- Delete a product.
- Apply discount or tax to a product's price.
- Retrieve all products.

## Getting Started
To get started with the Bookstore Application, clone the repository:

git clone https://github.com/lokeshe21/bookstore.git

## Build and Run
Make sure you have Java 17 and Gradle installed. You can build and run the application using the provided Gradle wrapper:

./gradlew build

./gradlew bootRun

Swagger link : Swagger-UI[](http://localhost:8080/bookstore/swagger-ui/index.html#/)

## Endpoints

POST /api/products: Create a new product.

GET /api/products/{productId}: Retrieve a product by ID.

PUT /api/products/{productId}: Update an existing product.

DELETE /api/products/{productId}: Delete a product.

PUT /api/products/{productId}/{type}: Apply discount or tax to a product.

GET /api/products: Retrieve all products.

## Payload
POST /api/products: Create a new product.
{
"name": "Test Book",
"description": "Fictional book",
"price": 50,
"quantityAvailable": 100
}

GET /api/products/{productId}: Retrieve a product by ID.
productId: 1

PUT /api/products/{productId}: Update an existing product.
productId: 1
{
"productId": 1,
"name": "Test Book title change",
"description": "Fictional book",
"price": 50,
"quantityAvailable": 100
}

DELETE /api/products/{productId}: Delete a product.
productId: 1

PUT /api/products/{productId}/{type}: Apply discount or tax to a product.
{
"productId": 1,
"name": "Test Book title change",
"description": "Fictional book",
"price": 50,
"quantityAvailable": 100
}

PUT /api/products/{productId}/{type}: Apply discount or tax to a product.
For Tax:
productId: 1
type: tax
percentageValue: 10

For discount:
productId: 1
type: tax
percentageValue: 10


./gradlew bootRun

## Dependencies
- Spring Boot 3.2.2
- Springdoc OpenAPI 2.1.0
- Lombok
- MapStruct 1.5.5.Final


