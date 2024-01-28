# Bookstore Application
## Features

The Bookstore Application is a simple Spring Boot application that manages products in a bookstore.

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Endpoints](#endpoints)
- [Build and Run](#build-and-run)
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

git clone https://github.com/your-username/bookstore-application.git

## Usage
Make sure you have Java 17 and Gradle installed. You can build and run the application using the provided Gradle wrapper:

./gradlew build

./gradlew bootRun

## Endpoints

POST /api/products: Create a new product.

GET /api/products/{productId}: Retrieve a product by ID.

PUT /api/products/{productId}: Update an existing product.

DELETE /api/products/{productId}: Delete a product.

PUT /api/products/{productId}/{type}: Apply discount or tax to a product.

GET /api/products: Retrieve all products.

## Build and Run
Build the project using:

./gradlew build

Run the application:

./gradlew bootRun

## Dependencies
- Spring Boot 3.2.2
- Springdoc OpenAPI 2.1.0
- Lombok
- MapStruct 1.5.5.Final


