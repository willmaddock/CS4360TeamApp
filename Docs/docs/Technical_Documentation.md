# CS4360 Team App - Technical Documentation

## Overview
The CS4360 Team App is a comprehensive mobile solution providing real-time parking availability, event planning, ticketing, weather and traffic integration, and multi-language support. This documentation covers the app's architecture, system design, and key components in its development.

## App Architecture

The app uses a client-server model, where the mobile app acts as the client, interacting with various external services and APIs to display real-time data, including parking availability, event tickets, and weather conditions.

### Key Components:
- **Client (Mobile App):**
  - Built in **Android Studio** using Java/Kotlin.
  - Integrated with **Firebase** for user authentication and data storage.
  - Utilizes APIs like **Google Maps**, **Ticketmaster**, **OpenWeatherMap**, and custom predictive models for parking availability.

- **Backend Services:**
  - **Firebase:** Manages user authentication, real-time database storage, and cloud functions.
  - **Google Maps API:** Provides location data and directions for parking.
  - **OpenWeatherMap API:** Supplies weather data for user alerts.
  - **Ticketmaster API:** Displays event tickets and supports purchasing.

## Data Models

### 1. **Parking Lot Model**
- **Attributes:**
  - `id`: Unique identifier for each parking lot or garage.
  - `location`: Coordinates of the parking location.
  - `status`: Availability status (e.g., "Available", "Occupied").
  - `cost`: Parking cost.
  - `event_id`: Event reference for special pricing or availability.

- **Functions:**
  - Predict real-time parking availability using historical data and AI.
  - Filter parking options by cost, availability, and proximity to events.

### 2. **Event Model**
- **Attributes:**
  - `event_id`: Unique event identifier.
  - `name`: Event name.
  - `location`: Event location, mapped with Google Maps API.
  - `date_time`: Event date and time.
  - `ticket_price`: Event ticket price.

- **Functions:**
  - Display nearby events.
  - Link events to relevant parking spots.
  - Integrate Ticketmaster API for purchasing tickets.

### 3. **Weather Model**
- **Attributes:**
  - `location`: Location for the weather data.
  - `temperature`: Current temperature.
  - `condition`: Weather condition (e.g., "Rain", "Snow").

- **Functions:**
  - Retrieve real-time weather data from OpenWeatherMap API.
  - Display weather alerts to guide user parking plans.

### 4. **User Model**

- **Attributes:**
  - `preferred_language`: User's language preference.

- **Functions:**
  - View costs and availability for various parking lots.
  - Get event details and set proximity filters for parking searches.
  - View Denver Nuggets events and modify language settings.

## Key External Integrations

1. **Google Maps API:** For real-time location tracking, parking maps, and directions.
2. **Ticketmaster API:** Displays local event tickets and allows in-app purchases.
3. **OpenWeatherMap API:** Provides weather forecasts and alerts.
4. **Firebase:** Manages authentication, data storage, and notifications.

## Data Flow

1. The app determines the user's **location** using **Google Maps** API.
2. Queries the **Parking Lot Model** for available spots near the user.
3. Checks for events in the area via the **Event Model**.
4. Fetches weather updates using the **OpenWeatherMap API**.
5. Filters parking spots by availability, cost, and event-based pricing.
6. Integrates **Ticketmaster API** for viewing and purchasing event tickets.

## Key Technologies

- **Android Studio**: Development environment.
- **Java/Kotlin**: Programming languages.
- **Firebase**: Real-time data and notifications.
- **Google Maps API**: Location tracking and mapping.
- **OpenWeatherMap API**: Weather data.
- **Ticketmaster API**: Ticket purchasing and event management.

## System Design

The app is designed for scalability and real-time updates. Firebase ensures seamless data synchronization, while external APIs support core app functionalities.

### Future Improvements:
- **Enhanced AI Models** for better parking availability predictions.
- **Additional APIs** for local events and campus-specific data.