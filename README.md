# Kaan Karakaş - Library Management System

## 🚀 Project Overview

The Library Management System is a web application developed with Spring Boot 3, Java 21, and PostgreSQL. It provides an efficient way for librarians to manage books, users, and the borrowing/returning process. It features user authentication with JWT and role-based authorization for restricting access to certain functionalities. The application allows librarians to manage books, view and update user information, and track borrowing history. Patrons can borrow and return books, while librarians can generate overdue reports.

## 📌 Key features

### 1.User Management

  - **Register User**: Allows new users to register by providing username, email, password, and role.
  - **Get User by ID**: Retrieve user details by their ID.
  - **Update User**: Allows updating user information such as username, email, and password. It checks for uniqueness before updating.
  - **Soft Delete User**: Soft delete a user without permanently removing them from the database.
	
### 2.Book Management
  - **Add Book**: Add new books to the library with essential information like title, author, and genre.
  - **Get Book by ID**: Retrieve details of a book by its unique ID.
  - **Search Books**: Search for books based on filters and supports pagination to handle large datasets.
  - **Update Book**: Update existing book details, such as title, author, genre, and more.
  - **Soft Delete Book**: Mark a book as deleted without physically removing it from the database.
	
### 3.Borrow Management	
  - **Borrow Book**: Borrow a book by associating it with a user. This operation tracks the borrow date and return date.
  - **Return Book**: Return a borrowed book, marking the return date and updating the status.
  - **Get Borrowed Books**: Retrieve a list of books currently borrowed by a user.
  - **Check Book Availability**: Check if a book is available for borrowing based on its current status.

## Security

- **Roles**:
  - **LIBRARIAN**: Full access to manage books and users, as well as view and manage borrowed books.
  - **PATRON**: Can borrow books, return them, and view borrowed books.
  
- **Authentication**: All endpoints are secured using **JWT-based authentication**. Access is granted based on the user’s role, ensuring proper authorization.	

## 🛠️ Technology Stack Used

- **Spring Boot 3**: Backend framework for building the application.
- **Java 21**: Programming language for backend development.
- **PostgreSQL**: Database used for storing application data.
- **JWT**: For user authentication and authorization.
- **Swagger/OpenAPI**: For API documentation.
- **Docker** : For containerization.

## ⚡ Getting Started

### Prerequisites

- **Java 21 installed**

- **PostgreSQL installed and running**

- **Maven installed**

- **Git installed**

### Setup Instructions

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/KaanKarakass/Library-Management-System.git
   cd library-management-system
   ```
   
2. **Configure the Database:**   
   * Create a PostgreSQL database named library_information using schema librarymanagement.
   * Update the application.properties file with the following configuration:
   
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/library_information?currentSchema=librarymanagement
   spring.datasource.username=postgres
   spring.datasource.password=Admin12345
   spring.datasource.driver-class-name=org.postgresql.Driver
   server.port=8080
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

   jwt.secret=mysecretkeymysecretkeymysecretkeymysecretkey
   jwt.expiration=3600000
   logging.level.org.springframework.security=DEBUG
   logging.level.org.springframework.web=DEBUG
   logging.level.com.kaankarakas=DEBUG
   ```
3. **Build and Run the Application:**

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   
4. **Access the API Documentation:**

   * Swagger UI: `http://localhost:8080/swagger-ui/index.html`

### Docker Setup
The application can also be run in a Docker container using Docker Compose. The following setup will help you run the application and PostgreSQL together.

1. **Docker Compose Setup:**
   The docker-compose.yml file is already included in the project. This configuration creates two services: postgres for the database and app for the application.
   
2. **Build and Run with Docker Compose:**
   To build and run the application using Docker Compose:
   
   ```bash
   docker-compose up --build
   ```
   
   This will:

   Build the Docker image for the application.

   Start both the postgres and app containers.

   Map the application to port 8080 and the database to port 5432.
   
3. **Stopping the Docker Containers:**

   ```bash
   docker-compose down
   ```
   
   Now, you can access the application at http://localhost:8080 and the Swagger UI at http://localhost:8080/swagger-ui/index.html.
   
## API Documentation
   You can access the full API documentation using Swagger UI:

   The Swagger API documentation is available at:

   - **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
   - **Swagger JSON**: [swagger.json](swagger-doc/swagger.json)