# Weather App UI - Jetpack Compose 🌦️

This document provides an overview of the Jetpack Compose UI code for a modern, clean weather application screen. The code is designed to be state-driven, responsive, and easy to understand. It allows users to search for a city and view its current weather conditions and a multi-day forecast.

---

## ✨ Features

* **City Search:** An `OutlinedTextField` and a "Search" `Button` for user input.
* **State Handling:** The UI gracefully handles different states of the data-fetching process:
    * **Idle:** An initial prompt to the user.
    * **Loading:** A `CircularProgressIndicator` to show that data is being fetched.
    * **Success:** Displays the full weather details.
    * **Error:** Shows a user-friendly error message if the API call fails.
* **Dynamic Weather Details:** Displays the current temperature, city name, date, and weather condition.
* **Condition-Based Icons:** A utility function maps API weather condition codes to appropriate local drawable icons (e.g., sunny, cloudy, rainy).
* **Additional Information:** Shows details like precipitation and wind speed.
* **Weekly Forecast:** A horizontal row displays the forecast for the upcoming days.

---

## 📸 Screenshots

The UI adapts based on the current state of the weather data fetch.

| State       | Description                                                                 | Screenshot Preview                                                      |
| ----------- | --------------------------------------------------------------------------- | ----------------------------------------------------------------------- |
| **Initial** | The default view when the app is first opened, prompting the user for input. | ``                        |
| **Loading** | Shown after the user presses "Search", while the data is being fetched.       | ``                              |
| **Success** | The main view displaying all the fetched weather data for the searched city.  | ``                     |
| **Error** | Displayed if the city is not found or if there is a network error.          | ``                     |

*(Note: Replace the bracketed text with actual image URLs or paths in your repository.)*

---

## 🛠️ Code Breakdown

The UI is structured into several modular and reusable composable functions.

### `WeatherPage(@Composable)`

This is the main screen and the entry point for the UI. Its responsibilities include:
* Managing the state of the `city` search field.
* Observing the `weatherResult` `LiveData` from the `WeatherViewModel`.
* Arranging the main UI elements: the search bar and the results area.
* Acting as a router to display the correct UI (`Loading`, `Error`, `Success`, or `null`) based on the `NetworkResponse` state.

### `WeatherDetails(@Composable)`

This composable is responsible for displaying all the weather information when the API call is successful.
* It takes a `WeatherModel` object as its primary data source.
* It formats and displays the temperature, location name, date, and condition text.
* It centrally displays a large weather icon using the `mapCodeToIcon` helper.

### Helper Functions

* **`mapCodeToIcon(code: Int)`**: A utility function that takes a weather condition code from the API and returns the corresponding local drawable resource ID.
* **`formatDate(dateString: String)`**: A helper that parses the `yyyy-MM-dd` date format from the API and converts it into a more readable day-of-the-week and date format.

---

## ⚙️ Dependencies & Assumptions

This UI code assumes the following components are available in the project:

1.  **`WeatherViewModel`**: A ViewModel that exposes a `LiveData<NetworkResponse<WeatherModel>>` named `weatherResult` and contains the `getData(city: String)` method.
2.  **`NetworkResponse` Sealed Class**: A wrapper class to represent the different states of a network request (e.g., `Loading`, `Success`, `Error`).
3.  **API Data Models**: Kotlin data classes (`WeatherModel`, `ForecastDay`, etc.) that match the structure of the weather API's JSON response.
4.  **Drawable Resources**: The project must have the required vector drawables in the `res/drawable` folder (e.g., `ic_sunny.xml`, `ic_cloudy.xml`, etc.).

---



        // Call the main composable
        WeatherPage(viewModel = weatherViewModel)
    }
}
