package uz.ttpu.weatherpatterns.mvc.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import uz.ttpu.weatherpatterns.di.WeatherModule
import uz.ttpu.weatherpatterns.mvc.controller.WeatherController
import uz.ttpu.weatherpatterns.mvc.model.WeatherModel
import uz.ttpu.weatherpatterns.ui.WeatherScreen

// Question 6 Comment:
// Why does render() take no parameters, and what would change if the Controller passed the Weather into the View instead?
// Answer: render() takes no parameters in classic MVC because the View reads the state directly from the Model ("pull model").
// If the Controller passed the Weather object directly into the View, the architecture would transform from MVC into MVP,
// where the View is passive ("push model") and the Controller acts as a Presenter pushing data into the View.
class MvcActivity : ComponentActivity(), WeatherView {
    private val model = WeatherModel(WeatherModule.repository)
    private val controller = WeatherController(model, view = this)
    private var status by mutableStateOf("Pick a city")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                WeatherScreen(status = status, onCitySelected = controller::onCitySelected)
            }
        }
    }

    override fun render() {
        // READ the Model (isLoading, error, weather) and set `status`
        // "Loading…" | "Error: <message>" | "Tashkent: 24°C, Sunny"
        status = when {
            model.isLoading -> "Loading…"
            model.error != null -> "Error: ${model.error}"
            model.weather != null -> {
                val w = model.weather!!
                "${w.city}: ${w.temperatureC}°C, ${w.condition}"
            }
            else -> "Pick a city"
        }
    }
}
