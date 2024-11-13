# Project Management System

A Project Management System built with Java Spring Boot, JWT authentication, file uploads, and more. This system helps manage projects, users, and tasks efficiently. It allows managers to assign tasks, monitor progress, and manage the project lifecycle. The system includes role-based access for managers and members.


## Technologies 

* Java Spring Boot: Backend framework.
* Spring Security: For authentication and authorization.
* PostgreSQL: Database for persisting data, managed with PgAdmin.
* Docker: Containerization for easy setup and deployment.
* Lombok: To reduce boilerplate code in models.
* JWT for secure authentication and authorization.


## Project Concept and Goal

###  Core Idea:
The project management system is designed to allow managers to create and manage projects, assign tasks to team members, and track project progress. The system supports role-based access, ensuring that managers can perform administrative tasks while team members focus on their assigned tasks.

###  Problems It Solves:
* Efficient Task Assignment: Helps managers assign tasks to the right people and track their progress.
* Role-based Access: Ensures that each user has the appropriate permissions based on their role (Manager or Member).
###  Why It’s Important:
This system improves project organization, enhances team collaboration, and streamlines workflows by providing a central hub for project management tasks.

## API Endpoints

| Method | Endpoint                | Description                       | Request Body               | Response                      | Auth Required |
|--------|-------------------------|-----------------------------------|----------------------------|-------------------------------|---------------|
| POST   | `/auth/users/register`                | Register a new user                            | UserDto (JSON)             | Created UserDto object        | No            |
| POST   | `/auth/users/login`                   | Login a user                                   | LoginRequest (JSON)        | JWT Token or error message    | No            |
| POST   | `/auth/users/verify`                  | Verify user email with code                    | Code (String)              | Success/Failure message       | No            |
| POST   | `/auth/users/request-password-reset`  | Request password reset                         | Email (String)             | Success/Failure message       | No            |
| POST   | `/auth/users/reset-password`          | Reset password                                 | Token, New Password        | Success/Failure message       | No            |
| POST   | `/auth/users/change-password`         | Change user's password                         | Old and New Passwords      | Success/Failure message       | Yes           |
| POST   | `/auth/users/deactivate`              | Deactivate a user                              | User ID (Long)             | Success/Failure message       | Yes           |
| PUT    | `/api/profiles`                       | Edit profile with optional file upload         | ProfileDto and file        | Updated ProfileDto object     | Yes           |
| GET    | `/api/profiles/{profileId}`           | Get profile by ID                              | N/A                        | ProfileDto object             | Yes           |
| GET    | `/api/profiles`                       | Get all profiles (Admin only)                  | N/A                        | List of ProfileDto objects    | Yes           |
| POST   | `/api/profiles/{id}/uploadProfilePic` | Upload profile picture                         | File (Multipart)           | Success/Failure message       | Yes           |
| GET    | `/api/profiles/files/{filename}`      | Serve the requested file                       | N/A                        | File as Resource              | Yes           |
| GET    | `/files/`                             | List all uploaded files                        | N/A                        | List of file URLs             | Yes           |
| GET    | `/files/{filename}`                   | Serve specific file                            | N/A                        | File as Resource              | Yes           |
| POST   | `/files/`                             | Upload files                                   | Files (Multipart)          | Success/Failure message       | Yes           |
| GET    | `/api/types`           | Retrieve all type records         | N/A                        | List of TypeDto objects or error message | Yes           |
| POST   | `/api/types`           | Create a new type record          | TypeDto (JSON)            | Created TypeDto object or error message | Yes           |
| GET    | `/api/types/{typeId}`  | Retrieve a type record by ID      | N/A                        | TypeDto object or error message | Yes           |
| GET    | `/api/types/name/{typeName}` | Retrieve a type record by name | N/A                        | TypeDto object or error message | Yes           |
| PUT    | `/api/types`           | Update an existing type record     | TypeDto (JSON)            | Updated TypeDto object or error message | Yes           |
| DELETE | `/api/types/{typeId}`  | Delete a type record by ID        | N/A                        | Success/Failure message       | Yes           |
| GET    | `/api/project`            | Get all project records          | N/A                  | List of ProjectDto objects      | Yes (Manager) |
| GET    | `/api/project/{projectId}`| Get project by ID                | N/A                  | ProjectDto object               | Yes (Manager) |
| POST   | `/api/project`            | Create a new project             | ProjectDto (JSON)    | Created ProjectDto object       | Yes (Manager) |
| PUT    | `/api/project`            | Edit an existing project         | ProjectDto (JSON)    | Updated ProjectDto object       | Yes (Manager) |
| DELETE | `/api/project/{projectId}`| Delete a project by ID           | N/A                  | Success message or error        | Yes (Manager) |
| GET    | `/api/task/project/{projectId}`       | Get all tasks by project ID                   | N/A                   | List of TaskDto objects           | No                   |
| GET    | `/api/task/{taskId}/project/{projectId}` | Get a task by task ID and project ID        | N/A                   | TaskDto object                    | No                   |
| POST   | `/api/task`                           | Create a new task                             | TaskDto (JSON)        | Created TaskDto object            | Yes (Manager)       |
| PUT    | `/api/task`                           | Update an existing task                       | TaskDto (JSON)        | Updated TaskDto object            | Yes (Manager)       |
| DELETE | `/api/task/{taskId}/project/{projectId}` | Delete a task by task ID and project ID     | N/A                   | Success message or error          | Yes (Manager)       |
| PUT    | `/api/task/{taskId}/status`           | Update task status (completed or not)         | TaskDto (JSON)        | Updated TaskDto object with status | No                   |
| GET    | `/api/task/assigned`                  | Get tasks assigned to the current user        | N/A                   | List of TaskDto objects           | Yes (Logged-in User) |

## ERD

![ERD](https://media.git.generalassemb.ly/user/53368/files/129e9cc1-ca91-4b0b-87de-8e4115c676e7)