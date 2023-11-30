package app.divarinterview.android.ui.city

import androidx.lifecycle.viewModelScope
import app.divarinterview.android.R
import app.divarinterview.android.common.BaseExceptionMapper
import app.divarinterview.android.common.BaseViewModel
import app.divarinterview.android.data.model.Centroid
import app.divarinterview.android.data.model.City
import app.divarinterview.android.data.repository.city.CityRepository
import app.divarinterview.android.service.location.LocationTracker
import app.divarinterview.android.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@HiltViewModel
class SelectCityViewModel @Inject constructor(
    private val cityRepository: CityRepository,
    private val locationTracker: LocationTracker
) : BaseViewModel() {

    private var _cityListState = MutableStateFlow<List<City>>(emptyList())
    val cityListState: StateFlow<List<City>> = _cityListState

    private var _userCurrentCity = MutableStateFlow<City?>(null)
    val userCurrentCity: StateFlow<City?> = _userCurrentCity

    private var cities: List<City> = emptyList()

    init {
        getCitiesList()
    }

    private fun getCitiesList() {
        viewModelScope.launch {
            cityRepository.getGroupList().collect {
                when (it) {
                    is Resource.Loading -> {
                        windowLoadingState.value = true
                    }

                    is Resource.Success -> {
                        it.data?.cities?.let { list ->
                            cities = list
                            _cityListState.value = list
                            windowLoadingState.value = false
                        } ?: kotlin.run {
                            EventBus.getDefault().post(
                                BaseExceptionMapper.httpExceptionMapper(
                                    0,
                                    R.string.error_fetch_data
                                )
                            )
                        }
                    }

                    is Resource.Error -> {
                        var message: String? = null
                        it.errorBodyJson?.let { errorBody ->
                            if (errorBody.has("message")) {
                                message = errorBody.getString("message")
                            }
                        }

                        EventBus.getDefault().post(
                            BaseExceptionMapper.httpExceptionMapper(
                                errorCode = it.errorCode,
                                localMessage = it.message,
                                serverMessage = message
                            )
                        )
                    }
                }
            }
        }
    }

    fun getUserCity() {
        viewModelScope.launch {
            locationTracker.getCurrentLocation()?.let { location ->
                cityRepository.findUserCurrentCity(location.latitude, location.longitude).collect {
                    when (it) {
                        is Resource.Loading -> {
                            _userCurrentCity.value = City(Centroid(0.0, 0.0), -1, "", 0, "")
                        }

                        is Resource.Success -> {
                            it.data?.let { city ->
                                _userCurrentCity.value = city
                            } ?: {
                                _userCurrentCity.value = null
                                EventBus.getDefault().post(
                                    BaseExceptionMapper.httpExceptionMapper(
                                        0,
                                        R.string.error_fetch_data
                                    )
                                )
                            }

                        }

                        is Resource.Error -> {
                            _userCurrentCity.value = null
                            EventBus.getDefault().post(
                                BaseExceptionMapper.httpExceptionMapper(
                                    0,
                                    R.string.select_city_error_404_not_found
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    fun searchInList(searchQuery: String) {
        val filteredCities = cities.filter {
            it.name.contains(searchQuery)
        }.sortedBy { city ->
            val indexOfQuery = city.name.indexOf(searchQuery)
            if (indexOfQuery >= 0) indexOfQuery else Int.MAX_VALUE
        }
        _cityListState.value = filteredCities
    }

    fun selectCity(city: City) {
        viewModelScope.launch {
            cityRepository.selectCity(city.id, city.name)
        }
    }
}