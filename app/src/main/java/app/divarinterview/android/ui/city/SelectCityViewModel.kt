package app.divarinterview.android.ui.city

import app.divarinterview.android.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class SelectCityViewModel : BaseViewModel() {

    init {
        getCitiesList()
    }

    private fun getCitiesList() {
        windowLoadingState.value = true
    }


}