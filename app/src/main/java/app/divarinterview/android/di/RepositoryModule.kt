package app.divarinterview.android.di

import app.divarinterview.android.data.repository.city.CityRepository
import app.divarinterview.android.data.repository.city.CityRepositoryImpl
import app.divarinterview.android.data.repository.post.PostRepository
import app.divarinterview.android.data.repository.post.PostRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideCityRepository(cityRepositoryImpl: CityRepositoryImpl): CityRepository

    @Binds
    @Singleton
    abstract fun providePostRepository(postRepositoryImpl: PostRepositoryImpl): PostRepository
}