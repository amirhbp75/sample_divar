package app.divarinterview.android.data.repository.city

import app.divarinterview.android.data.source.city.CityDataSource
import javax.inject.Inject


class CityRepositoryImpl @Inject constructor(private val remoteDataSource: CityDataSource) :
    CityRepository {
    override suspend fun getGroupList() = remoteDataSource.getGroupList()

    override suspend fun findUserCurrentCity(lat: Double, long: Double) =
        remoteDataSource.findUserCurrentCity(lat, long)
}

