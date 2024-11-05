
# CS4360 Team App Setup Instructions

Follow the steps below to set up the project in Android Studio, configure Firebase, and add API keys.

## Step 1: Clone the GitHub Repository
1. Open a terminal or Git Bash.
2. Clone the repository by running:
   ```bash
   git clone https://github.com/willmaddock/CS4360TeamApp.git
   ```
3. Open the cloned project in Android Studio.

## Step 2: Install Android Studio
1. If Android Studio is not already installed, download and install it from the [official website](https://developer.android.com/studio).

## Step 3: Set Up Firebase
1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Create a new Firebase project or use an existing one.
3. Add an Android app to the Firebase project with the package name:
   ```
   com.example.cs4360app
   ```
4. Download the `google-services.json` file.
5. Move the `google-services.json` file into your Android project at:
   ```
   app/google-services.json
   ```

## Step 4: Set Up API Keys (Google Maps, Firebase, AI Studio)
1. Go to the [Google Cloud Console](https://console.cloud.google.com/).
   - Create a new project or use an existing one.
   - Enable the **Google Maps SDK for Android** and generate an API key.
2. Go to [AI Studio](https://aistudio.google.com/app/apikey) and generate an API key for AI Studio.
3. Copy the generated API keys; these will be needed in the next step.

## Step 5: Set Up API Key for OpenWeatherMap
1. Go to the [OpenWeatherMap API](https://openweathermap.org/api/one-call-3).
2. Scroll down to the “How to Start” section and sign up for an account.
3. Obtain your API key.

## Step 6: Set Up API Key for Ticketmaster
1. Visit the [Ticketmaster API](https://developer.ticketmaster.com/products-and-docs/apis/getting-started/) to obtain an API key.
2. Follow the instructions to generate your API key.

## Step 7: Create the `secrets.xml` File
1. Navigate to the following directory inside your Android project:
   ```
   app/src/main/res/values/
   ```
2. Create a new file named `secrets.xml` with the following contents:

   ```xml
   <!-- res/values/secrets.xml -->
   <resources xmlns:tools="http://schemas.android.com/tools">
       <!-- Secure API Keys -->
       <string name="google_maps_key" tools:ignore="Typos">YOUR_API_KEY_HERE</string>
       <string name="default_web_client_id" tools:ignore="Typos">YOUR_API_KEY_HERE</string>
       <!-- Additional Secure Keys -->
       <string name="google_app_key" tools:ignore="MissingTranslation">YOUR_API_KEY_HERE</string>
       <string name="ticket_master_key" translatable="false" tools:ignore="MissingTranslation">YOUR_API_KEY_HERE</string>
       <string name="open_weather_map" translatable="false" tools:ignore="MissingTranslation">YOUR_API_KEY_HERE</string>
   </resources>
   ```

3. Replace the placeholders (`YOUR_API_KEY_HERE`) with the actual API keys you generated.

## Step 8: Sync the Project with Gradle
1. After adding the `google-services.json` file and the `secrets.xml` file, open Android Studio.
2. Click **Sync Project with Gradle Files** to ensure everything is correctly configured.

## Step 9: Run the Application
1. Once the Gradle sync is complete, build and run the project.
2. If you encounter any issues, review the error messages in Android Studio to ensure all API keys and configuration files are correctly set.

## Final Notes
- Each team member must use their own API keys for Google Maps, Firebase, OpenWeatherMap, Ticketmaster, and AI Studio.
- Ensure all placeholders in `secrets.xml` are replaced with your personal API keys.
- If any issues arise during the setup, reach out to the team for assistance.

---

This README file provides all necessary instructions for setting up and running the CS4360 Team App.
