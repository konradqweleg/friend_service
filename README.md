# Friend Service

The Friend Service is a backend application that manages user relationships and friendships. It provides functionality for creating, retrieving, and verifying friendships between users. The service is built using Spring WebFlux for reactive programming and integrates with other services such as the User Service for user validation.

## Technologies Used
- Java
- Spring WebFlux
- Reactor
- Gradle
- PostgreSQL

## Getting Started

### Prerequisites
- Java 17 or higher
- Gradle 7.0 or higher
- PostgreSQL database
- Running instance of User Service (for user validation)

### Installation
1. Clone the repository:

   ```bash
   git clone https://github.com/konradqweleg/friend-service.git
   cd friend-service

2. Update the application.properties file with the necessary service and database connection details:
    ```properties
    spring.datasource.url=?
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    user.service.url=http://?:?/user-service
    ```
3. Build the project using Gradle:
    ```bash
    ./gradlew build
    ```
4. Run the application:
    ```
   ./gradlew bootRun
    ```


## API Endpoints
### API Endpoints for Friendship Management

#### 1. **Get a User's Friends**
**Endpoint**: `GET /api/v1/friends/{userId}`  
Retrieves a list of all friends for the specified user.

**Path Variables**:
- `userId`: The ID of the user whose friends are to be retrieved.

**Response**:
- **200 OK** with a list of friends.
- **404 Not Found** if no friends are found for the user.

**Example Response**:
   ```json
   [
     {
       "id": 2,
       "name": "John Doe",
       "email": "john@example.com"
     },
     {
       "id": 3,
       "name": "Jane Smith",
       "email": "jane@example.com"
     }
   ]
 ```

#### 2. **Create a friendship**
**Endpoint**: `POST /api/v1/friends` 
Creates a friendship between two users.
**Request Body:**:
 ```json
{
"firstFriendId": 1,
"secondFriendId": 2
}
```

**Response**:
- **200 OK**  if the friendship is successfully created..