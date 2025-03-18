# Game Library API

## Overview

The Game Library API allows users to manage game rentals, user accounts, and reviews. The API provides functionalities for adding, retrieving, updating, and deleting users, games, and reviews.

## Base URL

```
http://localhost:8080
```

## Endpoints

### Users

#### Delete All Users

```
DELETE /Users/DeleteAll
```

#### Get All Users

```
GET /Users/GetAll
```

#### Add a New User

```
POST /Users/Add
Content-Type: application/json
```

**Request Body:**

```json
{
    "fName": "John",
    "lName": "Doe",
    "userName": "johndoe",
    "password": "securepassword123",
    "birthDate": "1990-01-01",
    "balance": 10.00,
    "email": "johndoe@example.com",
    "phoneNumber": "1234567890"
}
```

#### Get User By ID

```
GET /Users/Find/{id}
```

#### Add Money to User

```
PATCH /Users/AddMoney/user={id}&sum={amount}
```

---

### Games

#### Get All Games

```
GET /games/GetAll
```

#### Get Games by Genre

```
GET /games/genre/{genre}
```

#### Add a New Game

```
POST /games/Add
Content-Type: application/json
```

**Request Body:**

```json
{
    "title": "Cyberpunk 2078",
    "genre": "RPG",
    "price": 2.99,
    "totalCopies": 50,
    "availableCopies": 50,
    "maxPlayers": 1,
    "addedDate": "2024-03-04",
    "purchasePrice": 59.99,
    "ageCategory": "MATURE"
}
```

#### Get Game By ID

```
GET /games/Find/{id}
```

#### Rent a Game

```
PATCH /games/Rent/game={gameId}&user={userId}&for={days}
```

#### Extend Rent Period

```
PATCH /games/Extend/game/{gameId}/user/{userId}/start/{date}/for/{days}
```

#### Delete All Rents

```
DELETE /games/DeleteAllRents
```

#### Delete All Games

```
DELETE /games/DeleteAllGames
```

---

### Reviews

#### Add a Review (Valid User Who Rented the Game)

```
POST /games/AddReview
Content-Type: application/json
```

**Request Body:**

```json
{
    "userId": "5",
    "gameId": "1",
    "reviewText": "Joc fantastic! Grafica superbă și gameplay captivant!",
    "rating": 5
}
```

#### Get Reviews for a Game

```
GET /games/{gameId}/reviews
```

#### Validation Cases

- **User who has not rented the game attempts to add a review** ➝ Should fail.
- **User attempts to add a duplicate review** ➝ Should fail.
- **Fetching reviews for a game with no reviews** ➝ Should return an empty list.
- **Adding a review with an invalid rating** ➝ Should fail.
- **Adding a review with an empty text field** ➝ Should fail.
- **Adding a review with a non-existent user** ➝ Should fail.
- **Adding a review for a non-existent game** ➝ Should fail.

---

## Notes

- All endpoints return JSON responses.
- Authentication and security measures should be implemented in a production environment.
- Error handling should be improved to provide meaningful error messages.



