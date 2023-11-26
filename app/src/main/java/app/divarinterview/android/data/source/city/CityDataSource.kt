package app.divarinterview.android.data.source.city

import app.divarinterview.android.data.model.Cities
import app.divarinterview.android.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CityDataSource {
    suspend fun getGroupList(): Flow<Resource<Cities>>
}