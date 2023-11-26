package app.divarinterview.android.data.repository.city

import app.divarinterview.android.data.model.Cities
import app.divarinterview.android.utils.Resource
import kotlinx.coroutines.flow.Flow


interface CityRepository {
    suspend fun getGroupList(): Flow<Resource<Cities>>
}

