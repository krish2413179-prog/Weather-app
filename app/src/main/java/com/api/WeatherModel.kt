package com.api


data class WeatherModel(
    val current: Current,
    val location: Location,
    val forecast: Forecast?
)


data class Forecast(
    val forecastday: List<ForecastDay>
)


data class ForecastDay(
    val date: String,
    val day: Day,
)


data class Day(
    val maxtemp_c: Double,
    val mintemp_c: Double,
    val condition: Condition
)

