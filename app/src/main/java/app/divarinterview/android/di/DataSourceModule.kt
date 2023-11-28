package app.divarinterview.android.di

import app.divarinterview.android.common.container.PreferencesContainer
import app.divarinterview.android.data.source.city.CityDataSource
import app.divarinterview.android.data.source.city.CityLocalDataSource
import app.divarinterview.android.data.source.city.CityRemoteDataSource
import app.divarinterview.android.data.source.post.PostDataSource
import app.divarinterview.android.data.source.post.PostRemoteDataSource
import app.divarinterview.android.service.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    @Singleton
    @Named("cityRemote")
    fun provideCityRemoteDataSource(apiService: ApiService): CityDataSource {
        return CityRemoteDataSource(apiService)
    }

    @Provides
    @Singleton
    @Named("cityLocal")
    fun provideCityLocalDataSource(preferencesContainer: PreferencesContainer): CityDataSource {
        return CityLocalDataSource(preferencesContainer)
    }

    @Provides
    @Singleton
    @Named("postRemote")
    fun providePostRemoteDataSource(apiService: ApiService): PostDataSource {
        return PostRemoteDataSource(apiService)
    }
}