package app.divarinterview.android.data.source.city

import app.divarinterview.android.service.remote.ApiService
import app.divarinterview.android.service.remote.callApi
import javax.inject.Inject

class CityRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) : CityDataSource {

    override suspend fun getGroupList() = callApi {
        apiService.getCityList()
    }
}