package app.divarinterview.android.di

import app.divarinterview.android.data.source.city.CityDataSource
import app.divarinterview.android.data.source.city.CityRemoteDataSource
import app.divarinterview.android.service.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    @Singleton
    fun provideCityRemoteDataSource(apiService: ApiService): CityDataSource {
        return CityRemoteDataSource(apiService)
    }
}