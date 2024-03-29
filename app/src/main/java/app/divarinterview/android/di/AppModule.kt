package app.divarinterview.android.di

import android.app.Application
import app.divarinterview.android.service.image.FrescoImageLoadingService
import app.divarinterview.android.service.image.ImageLoadingService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideImageLoadingService(): ImageLoadingService {
        return FrescoImageLoadingService()
    }

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(app: Application): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(app)
    }
}