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