package app.divarinterview.android.data.source.city

import app.divarinterview.android.common.container.PreferencesContainer
import app.divarinterview.android.common.container.UserContainer
import app.divarinterview.android.data.model.Cities
import app.divarinterview.android.data.model.City
import app.divarinterview.android.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CityLocalDataSource @Inject constructor(
    private val preferences: PreferencesContainer
) : CityDataSource {

    override suspend fun getGroupList(): Flow<Resource<Cities>> {
        TODO("Not yet implemented")
    }

    override suspend fun findUserCurrentCity(lat: Double, long: Double): Flow<Resource<City>> {
        TODO("Not yet implemented")
    }

    override suspend fun selectCity(id: Int, name: String) {
        preferences.cityId = id
        preferences.cityName = name

        UserContainer.updateCity(id, name)
    }
}