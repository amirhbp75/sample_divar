package app.divarinterview.android.data.source.city

import app.divarinterview.android.service.remote.ApiService
import app.divarinterview.android.service.remote.callApi
import com.google.gson.JsonObject
import javax.inject.Inject

class CityRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) : CityDataSource {

    override suspend fun getGroupList() = callApi {
        apiService.getCityList()
    }

    override suspend fun findUserCurrentCity(lat: Double, long: Double) = callApi {
        apiService.findCity(JsonObject().apply {
            addProperty("lat", lat)
            addProperty("long", long)
        })
    }

    override suspend fun selectCity(id: Int, name: String) {
        TODO("Not yet implemented")
    }
}