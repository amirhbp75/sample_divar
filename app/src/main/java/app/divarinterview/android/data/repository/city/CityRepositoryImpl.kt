package app.divarinterview.android.data.repository.city

import app.divarinterview.android.data.source.city.CityDataSource
import javax.inject.Inject
import javax.inject.Named


class CityRepositoryImpl @Inject constructor(
    @Named("cityRemote") private val remoteDataSource: CityDataSource,
    @Named("cityLocal") private val localDataSource: CityDataSource
) :
    CityRepository {
    override suspend fun getGroupList() = remoteDataSource.getGroupList()

    override suspend fun findUserCurrentCity(lat: Double, long: Double) =
        remoteDataSource.findUserCurrentCity(lat, long)

    override suspend fun selectCity(id: Int, name: String) {
        localDataSource.selectCity(id, name)
    }
}

