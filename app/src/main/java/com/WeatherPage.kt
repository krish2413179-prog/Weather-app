package com


import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.api.NetworkResponse
import com.api.WeatherModel
import com.example.weatherapp.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun WeatherPage(viewModel: WeatherViewModel) {
    var city by remember { mutableStateOf("") }
    val weatherResult by viewModel.weatherResult.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text(text = "Enter a City") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { viewModel.getData(city) }) {
                Text(text = "Search")
            }
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.TopCenter
        ) {
            when (val result = weatherResult) {
                is NetworkResponse.Error -> Text(text = result.message)
                NetworkResponse.Loading -> CircularProgressIndicator()
                is NetworkResponse.Success -> WeatherDetails(data = result.data)
                null -> Text(text = "Search for a city to see the weather.")
            }
        }
    }
}

@Composable
fun WeatherDetails(data: WeatherModel) {
    val simpleDateFormat = SimpleDateFormat("EEEE HH:mm", Locale.getDefault())
    val currentDate = simpleDateFormat.format(Date())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
             horizontalAlignment = Alignment.Start
    ) {
     
        val temp = data.current.temp_c.toDoubleOrNull()?.roundToInt() ?: 0
        Text(
            text = "$temp°C",
            fontSize = 72.sp,
            fontWeight = FontWeight.SemiBold,
            fontStyle = FontStyle.Italic

        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = data.location.name,
            fontSize = 36.sp,
            color = Color.Blue
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$currentDate\n${data.current.condition.text}",
            fontSize = 18.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

      Column ( modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp),
    horizontalAlignment = Alignment.CenterHorizontally){
    Image(
        painter = painterResource(id = mapCodeToIcon(data.current.condition.code)),
        contentDescription = "Weather condition icon",
        modifier = Modifier.size(180.dp)

    )
}
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
           
            val precipitationValue = data.current.precip_mm.toDoubleOrNull() ?: 0.0
            val precipitationText = String.format(Locale.US, "%.1f mm", precipitationValue)

            val windValue = data.current.wind_kph.toDoubleOrNull()?.roundToInt() ?: 0

            Text(text = "Precipitation: $precipitationText", fontSize = 16.sp)
            Text(text = "Wind: $windValue m/s", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(32.dp))

        if (data.forecast != null) {
            WeeklyForecast(forecastDays = data.forecast.forecastday)
        }
    }
}



@Composable
fun WeeklyForecast(forecastDays: List<com.api.ForecastDay>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        forecastDays.forEach { forecastDay ->
            val (dayOfWeek, date) = formatDate(forecastDay.date)
            ForecastDay(day = dayOfWeek, date = date)
        }
    }
}

private fun formatDate(dateString: String): Pair<String, String> {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = inputFormat.parse(dateString) ?: return Pair("N/A", "N/A")
    val dayOfWeekFormat = SimpleDateFormat("E", Locale.getDefault())
    val dateFormat = SimpleDateFormat("MM/dd", Locale.getDefault())
    return Pair(dayOfWeekFormat.format(date), dateFormat.format(date))
}

@Composable
fun ForecastDay(day: String, date: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = day, fontWeight = FontWeight.Bold)
        Text(text = date, color = Color.Gray, fontSize = 14.sp)
    }
}

@DrawableRes
private fun mapCodeToIcon(code: Int): Int {
    return when (code) {
        1000 -> R.drawable.ic_sunny
        1003, 1006, 1009 -> R.drawable.ic_cloudy
        1063, 1180, 1183, 1186, 1189, 1192, 1195 -> R.drawable.ic_rainy
        1087, 1273, 1276 -> R.drawable.ic_thunder
        1066, 1114, 1210, 1213, 1216, 1219, 1222, 1225 -> R.drawable.ic_snowy
        else -> R.drawable.ic_cloudy
    }
}
