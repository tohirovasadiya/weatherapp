package uz.ttpu.weatherpatterns.mvp.presenter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import uz.ttpu.weatherpatterns.data.repository.WeatherRepository
import uz.ttpu.weatherpatterns.mvp.WeatherContract

class WeatherPresenter(
    private val view: WeatherContract.View,
    private val repository: WeatherRepository,
    private val scope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main),
) {
    fun onCitySelected(city: String) {
        // TODO 1: view.showLoading()
        view.showLoading()
        // TODO 2: scope.launch { ask the repository for `city` }
        scope.launch {
            val result = repository.getWeather(city)
            // TODO 3: success -> view.showWeather(weather)
            //         failure -> view.showError(message)
            result.onSuccess { weather ->
                view.showWeather(weather)
            }.onFailure { e ->
                view.showError(e.message ?: "Unknown error")
            }
        }
    }

    fun destroy() {
        // TODO: cancel the scope so that no callback reaches a destroyed View
        scope.cancel()
    }
}
