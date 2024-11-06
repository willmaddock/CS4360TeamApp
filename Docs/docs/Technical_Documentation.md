# CS4360 Team App - Technical Documentation

## Overview
The CS4360 Team App is designed to provide real-time parking availability, event planning, ticketing, weather and traffic integration, and multi-language support. This section covers the architecture, system design, and key components involved in the app's development.

## App Architecture

The app follows a client-server architecture, where the mobile app serves as the client, interacting with various external services and APIs to retrieve and display real-time data such as parking availability, event tickets, and weather conditions.

### Key Components:
- **Client (Mobile App):**
    - Developed in **Android Studio** using Java/Kotlin.
    - Integrated with **Firebase** for user authentication and data storage.
    - Uses various external APIs such as **Google Maps**, **Ticketmaster**, **OpenWeatherMap**, and custom predictive models for parking availability.

- **Backend Services:**
    - **Firebase:** Used for user authentication, real-time database storage, and cloud functions.
    - **Google Maps API:** Provides location data for parking availability and directions.
    - **OpenWeatherMap API:** Supplies weather data for weather-related alerts.
    - **Ticketmaster API:** Allows users to view and purchase event tickets.

## Data Models

### 1. **Parking Lot Model:**
- **Attributes:**
    - `id`: Unique identifier for the parking lots and garages.
    - `location`: Geographical coordinates of the parking lots and garages.
    - `status`: Availability status (e.g., "Available", "Occupied").
    - `cost`: Cost of parking at the spot.
    - `event_id`: Reference to an event for special pricing or availability.

- **Functions:**
    - Fetch available parking spots using historical data and AI simulations to predict real-time availability.
    - Filter parking spots by cost, availability, and proximity to the event.

### 2. **Event Model:**
- **Attributes:**
    - `event_id`: Unique identifier for the event.
    - `name`: Name of the event.
    - `location`: Location where the event is taking place (can be mapped using Google Maps API).
    - `date_time`: Date and time of the event.
    - `ticket_price`: Price of the tickets for the event.

- **Functions:**
    - Display events available near the user.
    - Link events to relevant parking spots in parking lots or parking garages.
    - Provide purchasing options via the integrated Ticketmaster API (optional).

### 3. **Weather Model:**
- **Attributes:**
    - `location`: Location associated with the weather data.
    - `temperature`: Current temperature at the location.
    - `condition`: Weather condition (e.g., "Rain", "Snow").

- **Functions:**
    - Fetch real-time weather updates from the OpenWeatherMap API.
    - Display relevant weather alerts (e.g., severe weather warnings) to help users plan their parking accordingly.

### 4. **User Model:**

- **Attributes:**
  - `preferred_language`: Specifies the language preference for the app interface.

- **Functions:**
  - View the cost of parking at various lots or garages.
  - Check the availability of parking spaces at selected lots or garages.
  - View detailed information about parking lots or garages, including addresses and ratings.
  - Apply proximity filters to search for parking options near the user's current location.
  - View upcoming Denver Nuggets events.
  - Modify the language preference for the app interface.

## Key External Integrations

1. **Google Maps API:**
    - Provides real-time location data for parking spots and event venues.
    - Displays directions and real-time traffic updates to users.

2. **Ticketmaster API:**
    - Allows the app to display and sell tickets for local events.
    - Supports integration for users to purchase tickets directly within the app.

3. **OpenWeatherMap API:**
    - Supplies up-to-date weather forecasts.
    - Enables weather-related alerts and integration into the app's event and parking planning tools.

4. **Firebase:**
    - Handles user authentication, data storage, and real-time updates.
    - Supports user account management and data persistence.

## Data Flow

1. The user opens the app and the **location** is determined using **Google Maps** API.
2. The app queries the **parking model** to retrieve available spots near the user’s location.
3. The app checks for any events in the area by querying the **event model**.
4. It integrates the **OpenWeatherMap API** to display real-time weather information.
5. The app allows users to filter parking spots by availability, price, and event-related costs.
6. For event tickets, the app interacts with the **Ticketmaster API** to fetch available tickets for local events and displays them for purchase.

## Key Technologies

- **Android Studio**: Main development environment for building the app.
- **Java/Kotlin**: Primary programming languages for Android development.
- **Firebase**: Used for real-time data storage, authentication, and notifications.
- **Google Maps API**: For real-time location tracking and parking spot mapping.
- **OpenWeatherMap API**: To fetch weather data for better parking and event planning.
- **Ticketmaster API**: To integrate ticket purchasing and event management functionalities.

## System Design

The system is designed for scalability with a focus on real-time updates. Firebase ensures seamless synchronization of user data across devices, while the integration with external APIs provides the necessary data to ensure the app’s core features function correctly.

### Future Improvements:
- **Improved AI Models** for better parking availability predictions.
- **Additional APIs** for local events or specific campus data integration.