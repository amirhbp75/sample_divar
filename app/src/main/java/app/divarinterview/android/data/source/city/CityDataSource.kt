package app.divarinterview.android.data.source.city

import app.divarinterview.android.data.model.Cities
import app.divarinterview.android.data.model.City
import app.divarinterview.android.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CityDataSource {
    suspend fun getGroupList(): Flow<Resource<Cities>>

    suspend fun findUserCurrentCity(lat: Double, long: Double): Flow<Resource<City>>

    suspend fun selectCity(id: Int, name: String)

}