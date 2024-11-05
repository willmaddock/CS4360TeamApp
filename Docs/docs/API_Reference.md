# API Reference

This document provides a comprehensive reference for the API endpoints and functionality of the CS4360 Team App.

## Table of Contents
1. [Authentication](#authentication)
2. [User Management](#user-management)
3. [Firebase Integration](#firebase-integration)
4. [Google Maps API](#google-maps-api)
5. [OpenWeatherMap API](#openweathermap-api)
6. [Ticketmaster API](#ticketmaster-api)
7. [AI Studio API](#ai-studio-api)

---

## Authentication

### POST /api/auth/login
- **Description**: Allows users to log in by providing their credentials (username and password).
- **Request Body**:
    ```json
    {
      "username": "user123",
      "password": "password123"
    }
    ```
- **Response**:
    - **200 OK**: User is authenticated and returns a JWT token.
    - **401 Unauthorized**: Invalid credentials.

### POST /api/auth/logout
- **Description**: Logs out the current user.
- **Response**:
    - **200 OK**: User is logged out successfully.
    - **400 Bad Request**: If no user session exists.

---

## User Management

### GET /api/users/{id}
- **Description**: Fetches the details of a user by their ID.
- **Response**:
    - **200 OK**: Returns user data.
    - **404 Not Found**: User with the specified ID does not exist.

### PUT /api/users/{id}
- **Description**: Updates user information.
- **Request Body**:
    ```json
    {
      "name": "New User Name",
      "email": "newemail@example.com"
    }
    ```
- **Response**:
    - **200 OK**: User details updated successfully.
    - **400 Bad Request**: Invalid data provided.

---

## Firebase Integration

### POST /api/firebase/send-notification
- **Description**: Sends a push notification using Firebase Cloud Messaging.
- **Request Body**:
    ```json
    {
      "title": "New Notification",
      "body": "This is a test notification."
    }
    ```
- **Response**:
    - **200 OK**: Notification sent successfully.
    - **500 Internal Server Error**: Error in sending notification.

---

## Google Maps API

### GET /api/maps/geocode
- **Description**: Geocodes an address to latitude and longitude.
- **Request Query Parameters**:
    - `address`: The address to geocode.
- **Response**:
    - **200 OK**: Returns latitude and longitude of the address.
    - **400 Bad Request**: Invalid address.

---

## OpenWeatherMap API

### GET /api/weather/current
- **Description**: Fetches current weather data for a location.
- **Request Query Parameters**:
    - `location`: The location for which weather data is required.
- **Response**:
    - **200 OK**: Returns current weather details.
    - **400 Bad Request**: Invalid location.

---

## Ticketmaster API

### GET /api/ticketmaster/events
- **Description**: Fetches a list of events from Ticketmaster.
- **Request Query Parameters**:
    - `location`: City or location for events.
- **Response**:
    - **200 OK**: Returns a list of events.
    - **404 Not Found**: No events found for the specified location.

---

## AI Studio API

### GET /api/ai/predict
- **Description**: Sends data to AI Studio for prediction.
- **Request Body**:
    ```json
    {
      "input_data": [1, 2, 3, 4]
    }
    ```
- **Response**:
    - **200 OK**: Returns the predicted result.
    - **400 Bad Request**: Invalid input data.

---

## Notes
- All endpoints require proper authentication via API keys or JWT tokens.
- For more detailed information, refer to the respective API documentation provided by the service.