# GitHub Repository Searcher

A Spring Boot application that allows users to search for GitHub repositories using the GitHub REST API, stores searched repository details in PostgreSQL database and provides REST API endpoints to retrieve stored repositories based on filter criteria..

## Features

- Search GitHub repositories by name.
- Optionally filter GitHub searches by programming language.
- Sort GitHub search results by stars, forks, or last updated date.
- Store repository details in PostgreSQL.
- Update existing repository records instead of creating duplicates.
- Retrieve saved repositories with optional language and minimum-star filters.
- Handle empty search results and GitHub API errors.
- Explore API endpoints through Swagger UI.

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- GitHub REST API
- Maven
- JUnit 5 and Mockito
- Swagger UI / OpenAPI

## Prerequisites

- Java 17 or compatible JDK
- PostgreSQL
- Maven, or the included Maven Wrapper
- Git

## Configuration

Configure your PostgreSQL connection in `src/main/resources/application.properties`.

Example:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```


Set the following environment variables to match your local PostgreSQL configuration:

- DB_URL — for example, jdbc:postgresql://localhost:5432/github_repository_searcher
- DB_USERNAME — your PostgreSQL username.
- DB_PASSWORD — your PostgreSQL password.

Do not commit database passwords or other secrets to GitHub.

## Running the Application

1. Clone the repository:

   ```bash
   git clone <YOUR_GITHUB_REPOSITORY_URL>
   cd <YOUR_PROJECT_DIRECTORY>
   ```

2. Configure the PostgreSQL database and environment variables.

3. Run the application on Windows:

Method 1: Run directly from VS Code

- Open the project folder in VS Code.
- Navigate to: src/main/java/com/project/github_repository_searcher/GithubRepositorySearcherApplication.java
- Open the file and click the Run button above the main() method.

- Alternatively, right-click the file and select Run Java.


Method 2: Run using Maven Wrapper

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   On macOS or Linux:

   ```bash
   ./mvnw spring-boot:run
   ```


## API Documentation

### Swagger UI

- When running locally, open:
`http://localhost:8080/swagger-ui/index.html`

### OpenAPI specification:

`http://localhost:8080/v3/api-docs`
- If you configure a different server port, use that port instead of 8080.



## API Endpoints

### 1. Search and Save Repositories

**POST** `/api/github/search`

Request body:

```json
{
  "query": "react",
  "language": "JavaScript",
  "sort": "stars"
}
```
Parameters:

- query: Repository name or partial name to search for.
- language: Optional programming language filter.
- sort: Sorting preference — stars, forks, or updated.

The API searches GitHub and saves or updates the returned repositories in PostgreSQL.


### 2. Retrieve Saved Repositories

**GET** `/api/github/repositories`

Optional query parameters:

| Parameter | Description |
|---|---|
| `language` | Filter by programming language |
| `minStars` | Minimum number of stars |
| `sort` | Sort by `stars`, `forks`, or `updated`; defaults to `stars` |

Example:

```http
GET /api/github/repositories?language=JavaScript&minStars=100&sort=stars
```


## Running Tests

Run the test suite on Windows:

```powershell
.\mvnw.cmd test
```

On macOS or Linux:

```bash
./mvnw test
```
### Test Coverage

The automated tests cover the following scenarios:

- Service layer tests

   - Handling empty GitHub API search results.
   - Saving newly discovered repositories.
   - Updating existing repositories without creating duplicates.
   - Passing language filters, minimum-star criteria, and sorting options to the repository layer.

- Controller layer tests

   - Testing the repository search endpoint (POST /api/github/search).
   - Testing the saved repositories endpoint (GET /api/github/repositories).
   - Verifying HTTP responses for API requests.

Service tests use Mockito to isolate business logic by mocking dependencies. Controller tests use Spring Boot's MockMvc to test HTTP endpoints without starting a real web se


## Project Structure

```text
src/
├── main/
│   ├── java/com/project/github_repository_searcher/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── exception/
│   │   ├── repository/
│   │   └── service/
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/project/github_repository_searcher/
        ├── controller/
        └── service/
```


## Error Handling

The application handles GitHub HTTP errors and empty search results. API error responses are processed through a global exception handler.

## Author

Rakesh Kumar Meher