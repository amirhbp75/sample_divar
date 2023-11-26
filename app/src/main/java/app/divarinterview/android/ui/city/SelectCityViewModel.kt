package app.divarinterview.android.ui.city

import androidx.lifecycle.viewModelScope
import app.divarinterview.android.R
import app.divarinterview.android.common.BaseExceptionMapper
import app.divarinterview.android.common.BaseViewModel
import app.divarinterview.android.data.model.City
import app.divarinterview.android.data.repository.city.CityRepository
import app.divarinterview.android.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@HiltViewModel
class SelectCityViewModel @Inject constructor(
    private val cityRepository: CityRepository
) : BaseViewModel() {

    private var _cityListState = MutableStateFlow<List<City>>(emptyList())
    val cityListState: StateFlow<List<City>> = _cityListState

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
                                it.errorCode,
                                it.message,
                                message
                            )
                        )
                    }
                }
            }
        }
    }


}