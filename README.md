# Publicly deployed API URL:
https://json-dataset-api-dw5v.onrender.com/

# Swagger UI URL:
https://json-dataset-api-dw5v.onrender.com/swagger-ui/index.html

# JSON Dataset Management & Query API

A RESTful backend application built with Java and Spring Boot that allows users to create JSON datasets, store records in PostgreSQL, and retrieve records using dynamic grouping and sorting operations.

## Features

- Create datasets with multiple JSON records.
- Store JSON records using PostgreSQL JSONB.
- Retrieve records by dataset name.
- Dynamically group records using any top-level JSON field.
- Dynamically sort records in ascending or descending order.
- Combine grouping and sorting in a single query.
- Validate incoming requests and query parameters.
- Return structured error responses.
- Interactive API documentation using Swagger UI.
- Layered architecture with grouping and sorting strategy patterns.
- Unit and controller tests.

## Technologies Used

| Technology | Purpose |
|---|---|
| Java 21 | Backend programming language |
| Spring Boot 4 | Application framework |
| Spring Web MVC | REST API development |
| Spring Data JPA | Database operations |
| PostgreSQL | Relational database |
| JSONB | JSON record storage |
| Maven | Dependency management and build |
| Jakarta Validation | Request validation |
| Hibernate | ORM and JSON mapping |
| Springdoc OpenAPI | Swagger API documentation |
| JUnit 5 | Unit testing |
| Mockito | Mock-based testing |
| Git and GitHub | Version control |

## Project Architecture

```text
src
├── main
│   ├── java/com/assignment/jsondataset
│   │   ├── config
│   │   ├── controller
│   │   ├── dto
│   │   │   ├── request
│   │   │   └── response
│   │   ├── entity
│   │   ├── exception
│   │   ├── repository
│   │   ├── service
│   │   │   └── impl
│   │   └── strategy
│   └── resources
│       └── application.properties
└── test
    └── java/com/assignment/jsondataset
```

## Database Configuration

Create a PostgreSQL database named:

```sql
CREATE DATABASE json_dataset_db;
```

Configure your database credentials in:

`src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/json_dataset_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_POSTGRES_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.port=8080
```

Replace `YOUR_POSTGRES_PASSWORD` with your PostgreSQL password.

## Running the Application

Make sure Java 21, Maven, and PostgreSQL are installed and PostgreSQL is running.

Build the project:

```bash
mvn clean package
```

Run the application:

```bash
mvn spring-boot:run
```

The application runs at:

`http://localhost:8080`

## API Endpoints

### 1. Create a Dataset

**POST** `/api/datasets`

Request body:

```json
{
  "datasetName": "products",
  "records": [
    {
      "name": "Laptop",
      "category": "Electronics",
      "price": 50000
    },
    {
      "name": "Phone",
      "category": "Electronics",
      "price": 20000
    },
    {
      "name": "Chair",
      "category": "Furniture",
      "price": 5000
    }
  ]
}
```

### 2. Retrieve Dataset Records

**GET** `/api/datasets/products`

Returns all records belonging to the `products` dataset.

### 3. Group Records

**GET** `/api/datasets/products?groupBy=category`

Groups records using the `category` field.

### 4. Sort Records

**GET** `/api/datasets/products?sortBy=price&sortOrder=ASC`

Sorts records by price in ascending order.

For descending order:

`/api/datasets/products?sortBy=price&sortOrder=DESC`

### 5. Group and Sort Together

**GET** `/api/datasets/products?groupBy=category&sortBy=price&sortOrder=DESC`

Groups records by category and sorts the records by price within the resulting dataset order.

## API Documentation

Swagger UI:

`http://localhost:8080/swagger-ui/index.html`

OpenAPI specification:

`http://localhost:8080/v3/api-docs`

## Testing

Run all tests:

```bash
mvn test
```

The test suite covers service operations, controller endpoints, grouping, sorting, and exception handling.

## Design Patterns

**Strategy Pattern:** Grouping and sorting operations are implemented through separate strategy interfaces, allowing their implementations to be changed or extended independently.

**Layered Architecture:** The application separates controller, service, repository, entity, DTO, configuration, and exception-handling responsibilities.

## Future Enhancements

- Pagination and filtering support.
- Nested JSON field queries.
- Dataset update and deletion operations.
- Authentication and authorization.
- Cloud deployment and CI/CD integration.

