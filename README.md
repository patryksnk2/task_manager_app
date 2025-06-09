# Task Manager REST API

A robust and scalable RESTful API server for comprehensive task management, built with Java 17+, Spring Boot 3.x, Spring Security (JWT), Hibernate (JPA), and an embedded H2 database. This backend service supports user management, task assignment, notifications, and robust role-based access control (RBAC).

---

## Table of Contents

- [Project Overview](#project-overview)  
- [Features](#features)  
- [Architecture](#architecture)  
- [Technology Stack](#technology-stack)  
- [Database Structure](#database-structure)  
- [Roles & Permissions](#roles--permissions)  
- [Getting Started](#getting-started)  
- [API Endpoints Overview](#api-endpoints-overview)  
- [Testing](#testing)  
- [Challenges & Solutions](#challenges--solutions)  
- [Future Plans](#future-plans)  
- [Contact](#contact)  

---

## Project Overview

The Task Manager REST API provides backend functionality for managing tasks, users, and notifications. The system includes secure authentication, RBAC, task tagging, hierarchical tasks, and extensible notification features. Designed for statelessness and scalability, it is ideal for integration with frontend clients or other services.

---

## Features

- **User Registration & Authentication**  
  Secure registration and login with data validation and password encryption.

- **Role-Based Access Control (RBAC)**  
  Two roles: ADMIN (full access) and USER (limited access to own resources).

- **Task Management**  
  Create, edit, assign, and manage hierarchical tasks (parent/child relationships).

- **Comments and Threads**  
  Add comments to tasks, with support for threaded discussions.

- **Tags & Attributes**  
  Define, assign, and filter tasks by tags and attributes (status, priority, etc.).

- **Notifications**  
  In-app notifications, with extensibility for email or other channels.

- **Security**  
  Stateless JWT authentication, endpoint-level authorization, and encrypted credentials.

- **Stateless API**  
  No server-side sessions; scalable and cloud-ready.

---

## Architecture

The project follows a layered architecture:

- **Presentation Layer:** REST API (Spring MVC)  
- **Service Layer:** Business logic and orchestration  
- **Persistence Layer:** Data access via JPA/Hibernate  
- **Security Layer:** Spring Security with JWT authentication  
- **Database:** Embedded H2 (can be swapped for MySQL/MariaDB if needed)

---

## Technology Stack

- Java 17+  
- Spring Boot 3.x  
- Spring Security (JWT)  
- Hibernate / JPA  
- Maven / Gradle  
- H2 Database (embedded, for development/testing)  
- JUnit 5, Mockito (unit and integration testing)

---

## Database Structure

| Table                | Description                                         |
|----------------------|-----------------------------------------------------|
| `users`              | User accounts                                       |
| `roles`              | User roles (ADMIN, USER)                            |
| `user_roles`         | Assignment of roles to users                        |
| `tasks`              | Tasks, including title, description, status        |
| `task_attributes`    | Task attributes (e.g., status, priority)            |
| `tags`               | Task tags                                          |
| `task_tags`          | Task-tag assignments                               |
| `task_assigned_users`| User-task assignments                              |
| `task_comments`      | Comments and threads on tasks                        |
| `notifications`      | In-app notifications for users                       |

*Note: The schema is designed for easy extensibility and integrity.*

---

## Roles & Permissions

| Role  | Permissions                                                  |
|-------|--------------------------------------------------------------|
| ADMIN | Full access: user, task, tag, notification, and configuration management |
| USER  | Limited to own tasks: view, comment, create, and filter tasks by tags |

---

## Getting Started

### Prerequisites

- Java 17 or newer  
- Maven or Gradle  

### Installation & Run

1. Clone the repository:  
   ```bash
   git clone https://github.com/patryksnk2/task_manager_app.git
   cd task_manager_app
